package com.teambrmodding.assistedprogression.common.item;

import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author James Rogers - Dyonovan
 * @since 09/01/19
 */

public class MagnetItem extends Item implements IAdvancedToolTipProvider {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Defined values
    private static final int RANGE = 10;
    private static final float ATTRACT_SPEED = 0.0075F;

    // NBT Tags
    private static final String ACTIVE = "ActiveCharging";

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
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(CreeperEntity.class, bb));
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(TNTEntity.class, bb));

            // Need to expand, these next few are too fast for a small box
            bb.expand(RANGE * 2, RANGE * 2, RANGE * 2);

            // Projectiles
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(ArrowEntity.class, bb));
            entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(FireballEntity.class, bb));

            // Make sure the list is populated
            if (!entitiesInBox.isEmpty()) {
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
            compound.putBoolean(ACTIVE, (!stack.hasTag() || !stack.getTag().getBoolean(ACTIVE)));
            stack.setTag(compound);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(ACTIVE) && stack.getTag().getBoolean(ACTIVE);
    }

    /**
     * Get the tool tip to present when shift is pressed
     *
     * @param stack The itemstack
     * @return The list to display
     */
    @Nullable
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        return Collections.singletonList(I18n.format("item_cheap_magnet.desc"));
    }
}


