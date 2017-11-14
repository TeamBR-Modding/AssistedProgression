package com.teambrmodding.assistedprogression.common.items;

import com.teambr.bookshelf.common.items.EnergyContainingItem;
import com.teambr.bookshelf.util.EnergyUtils;
import com.teambrmodding.assistedprogression.managers.ConfigManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/13/17
 */
public class ItemMagnet extends BaseItem {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Defined values
    public static final int    RANGE        = 10;
    public static final float ATTRACT_SPEED = 0.075F;

    // NBT Tags
    public static final String ACTIVE = "ActiveCharging";

    private boolean isBasicMagnet;

    /**
     * Base constructor for all items
     *
     * @param itemName     The item name
     * @param maxStackSize Maximum stack size
     */
    public ItemMagnet(boolean basic, String name) {
        super(name, 1);
        isBasicMagnet = basic;
    }

    /*******************************************************************************************************************
     * Item                                                                                                            *
     *******************************************************************************************************************/

    /**
     * Called from ItemStack.setItem, will hold extra data for the life of this ItemStack.
     * Can be retrieved from stack.getCapabilities()
     * The NBT can be null if this is not called from readNBT or if the item the stack is
     * changing FROM is different then this item, or the previous item had no capabilities.
     *
     * This is called BEFORE the stacks item is set so you can use stack.getItem() to see the OLD item.
     * Remember that getItem CAN return null.
     *
     * @param stack The ItemStack
     * @param nbt NBT of this item serialized, or null.
     * @return A holder instance associated with this ItemStack where you can hold capabilities for the life of this item.
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        if(!isBasicMagnet)
            return new EnergyContainingItem(stack, ConfigManager.totalRFMagnet);
        else
            return super.initCapabilities(stack, nbt);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // Make sure we have a tag
        if(!stack.hasTagCompound()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean(ACTIVE, false);
            stack.setTagCompound(compound);
        }

        // Make sure we are a player and should even pull things
        if (!worldIn.isRemote && stack.getTagCompound().getBoolean(ACTIVE) && entityIn instanceof EntityPlayer) {
            // Cast
            EntityPlayer player = (EntityPlayer) entityIn;

            // If we have it, define, be careful down the line, always null check
            IEnergyStorage magnetEnergy = null;
            if(stack.hasCapability(CapabilityEnergy.ENERGY, null))
                magnetEnergy  = stack.getCapability(CapabilityEnergy.ENERGY, null);

            // If we have energy or if is basic, basic does not require power but pulls bad stuff too
            if((magnetEnergy != null && magnetEnergy.getEnergyStored() > 0) || isBasicMagnet) {
                // Setup box
                AxisAlignedBB bb = new AxisAlignedBB(
                        player.posX - RANGE, player.posY - RANGE, player.posZ - RANGE,
                        player.posX + RANGE, player.posY + RANGE, player.posZ + RANGE);

                ArrayList<Entity> entitiesInBox = new ArrayList<>();

                // Add items first
                List<EntityItem> itemEntities    = worldIn.getEntitiesWithinAABB(EntityItem.class, bb);
                ArrayList<EntityItem> itemsToAdd = new ArrayList<>();
                for(EntityItem item : itemEntities) {
                    if(!item.cannotPickup())
                        itemsToAdd.add(item);
                }
                entitiesInBox.addAll(itemsToAdd);

                // XP Orbs
                entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(EntityXPOrb.class, bb));

                // If basic, add negative things
                if(isBasicMagnet) {
                    // Explosive stuff
                    entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(EntityCreeper.class,   bb));
                    entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(EntityTNTPrimed.class, bb));

                    // Need to expand, these next few are too fast for a small box
                    bb.expand(RANGE * 2, RANGE * 2, RANGE * 2);

                    // Projectiles
                    entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(EntityArrow.class,         bb));
                    entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(EntitySmallFireball.class, bb));
                    entitiesInBox.addAll(worldIn.getEntitiesWithinAABB(EntityLargeFireball.class, bb));
                }

                // Make sure the list is populated
                if(!entitiesInBox.isEmpty()) {
                    for(Entity entity : entitiesInBox) {
                        // Create a vector pointing to the player
                        Vec3d motionVector = new Vec3d(
                                player.posX - entity.posX,
                                player.posY - entity.posY,
                                player.posZ - entity.posZ);
                        // Don't let it get above one, speed will be adjusted below
                        if(motionVector.lengthVector() > 1)
                            motionVector.normalize();

                        // Arrows need more pull, moving too fast already
                        boolean isArrow = entity instanceof EntityArrow;
                        float speed = ATTRACT_SPEED + (isArrow ? 0.2F : 0F);

                        // Apply speed
                        motionVector.addVector(speed, speed, speed);

                        entity.motionX = motionVector.xCoord;
                        entity.motionY = motionVector.yCoord;
                        entity.motionZ = motionVector.zCoord;
                        entity.velocityChanged = true;
                    }

                    // Drain if possible
                    if(magnetEnergy != null)
                        magnetEnergy.extractEnergy(1, false);
                }
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (playerIn.isSneaking() && playerIn.getHeldItem(handIn).getItem() instanceof ItemMagnet) {
            ItemStack heldStack = playerIn.getHeldItem(handIn);
            if(heldStack.getTagCompound() != null) {
                heldStack.getTagCompound().setBoolean(ACTIVE, !heldStack.getTagCompound().getBoolean(ACTIVE));
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(ACTIVE) && stack.getTagCompound().getBoolean(ACTIVE);
    }

    /**
     * Allows items to add custom lines of information to the mouseover description
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        if(!isBasicMagnet)
            EnergyUtils.addToolTipInfo(stack, tooltip);
    }
}
