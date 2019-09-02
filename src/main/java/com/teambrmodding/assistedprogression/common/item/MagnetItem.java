package com.teambrmodding.assistedprogression.common.item;

import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MagnetItem extends Item {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Defined values
    public static final int    RANGE        = 10;
    public static final float ATTRACT_SPEED = 0.0075F;

    // NBT Tags
    public static final String ACTIVE = "ActiveCharging";

    public MagnetItem(String name) {
        super(new Properties()
                .maxStackSize(1)
                .group(ItemManager.itemGroupAssistedProgression));
        setRegistryName(name);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        /*if (!stack.hasTag()) {
            CompoundNBT compound = new CompoundNBT();
            compound.putBoolean(ACTIVE, false);
            stack.setTag(compound);
        }*/

        if (!worldIn.isRemote && stack.hasTag() && stack.getTag().contains(ACTIVE) && stack.getTag().getBoolean(ACTIVE) && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;

            AxisAlignedBB bb = new AxisAlignedBB(
                    player.posX - RANGE, player.posY - RANGE, player.posZ - RANGE,
                    player.posX + RANGE, player.posY + RANGE, player.posZ + RANGE);

            //Get Items First
            List<ItemEntity> itemEntities = worldIn.getEntitiesWithinAABB(ItemEntity.class, bb);
            ArrayList<ItemEntity> itemsToAdd = new ArrayList<>();
            for (ItemEntity item : itemEntities) {
                if (!item.cannotPickup())
                    itemsToAdd.add(item);
            }
            ArrayList<Entity> entitiesInBox = new ArrayList<>(itemsToAdd);

            //XP Orbs
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(ExperienceOrbEntity.class, bb));

            //Bad things also happen
            // Explosive stuff
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(CreeperEntity.class,   bb));
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(TNTEntity.class, bb));

            // Need to expand, these next few are too fast for a small box
            bb.expand(RANGE * 2, RANGE * 2, RANGE * 2);

            // Projectiles
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(ArrowEntity.class,         bb));
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(FireballEntity.class, bb));

            // Make sure the list is populated
            if(!entitiesInBox.isEmpty()) {
                for (Entity entity : entitiesInBox) {
                    // Create a vector pointing to the player
                    Vec3d motionVector = new Vec3d(
                            player.posX - entity.posX,
                            player.posY - entity.posY,
                            player.posZ - entity.posZ);
                    // Don't let it get above one, speed will be adjusted below
                    if (motionVector.length() > 1)
                        motionVector.normalize();

                    // Arrows need more pull, moving too fast already
                    boolean isArrow = entity instanceof ArrowEntity;
                    float speed = ATTRACT_SPEED + (isArrow ? 0.2F : 0F);

                    // Apply speed
                    motionVector.add(speed, speed, speed);

                    entity.setMotion(motionVector.x, motionVector.y, motionVector.z);
                    entity.velocityChanged = true;
                }
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        if (player.isSneaking() && player.getHeldItem(hand).getItem() instanceof MagnetItem) {
            ItemStack stack = player.getHeldItem(hand);

            CompoundNBT compound = new CompoundNBT();
            compound.putBoolean(ACTIVE, (stack.hasTag() ? !stack.getTag().getBoolean(ACTIVE) : true));
            stack.setTag(compound);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(ACTIVE) && stack.getTag().getBoolean(ACTIVE);
    }
}


