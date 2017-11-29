package com.teambrmodding.assistedprogression.common.tiles;

import com.teambr.nucleus.common.tiles.InventoryHandler;
import com.teambrmodding.assistedprogression.managers.RecipeManager;
import com.teambrmodding.assistedprogression.registries.GrinderRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/12/17
 */
public class TileGrinder extends InventoryHandler {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Syncable variables
    public static final int CREATE_EFFECTS_VARID = 0;

    // Defined variables
    public static final int MAX_PROGRESS = 15;

    // Operating variables
    public int progress = 0;

    /*******************************************************************************************************************
     * TileGrinder                                                                                                     *
     *******************************************************************************************************************/

    public void activateGrinder(int progressValue, double multiplier) {
        updateCurrentItem();
        markForUpdate(6);
        if(!getStackInSlot(3).isEmpty() && hasOutputAvailable()) {
            int movement = progressValue;

            sendValueToClient(CREATE_EFFECTS_VARID, 0);

            if(progressValue == 1)
                movement = 2;

            progress += (movement * multiplier);
            if(progress >= MAX_PROGRESS) {
                progress = progress - MAX_PROGRESS;
                grindItem();
                world.playSound(null,
                        pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                        SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS,
                        0.6F, 1.1F);
                world.playSound(null,
                        pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                        SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS,
                        0.1F, 0.4F);
                markForUpdate(6);
                if(progress >= MAX_PROGRESS)
                    activateGrinder(progressValue - MAX_PROGRESS, multiplier);
            }
        } else
            progress = 0;
    }

    /**
     * Update the current item and pull in stackable inputs
     */
    private void updateCurrentItem() {
        if(getStackInSlot(3).isEmpty()) { // Nothing to grind, try to move in
            for(int x = 0; x < 3; x++) { // Loop input slots
                if(!getStackInSlot(x).isEmpty()) { // Found something
                    setStackInSlot(3, getStackInSlot(x).copy()); // Copy to middle
                    setStackInSlot(x, ItemStack.EMPTY); // Clear input
                    return; // No need to proceed
                }
            }
        }
        else if(getStackInSlot(3).getCount() < getStackInSlot(3).getMaxStackSize()) { // Something grinding, but want to fill
            for(int x = 0; x < 3; x++) { // Loop input slots
                if(!getStackInSlot(x).isEmpty() && getStackInSlot(x).getItem() == getStackInSlot(3).getItem()
                        && getStackInSlot(x).getItemDamage() == getStackInSlot(3).getItemDamage()) {
                    int spaceFree = getStackInSlot(3).getMaxStackSize() - getStackInSlot(3).getCount();
                    if(getStackInSlot(x).getCount() <= spaceFree) {
                        getStackInSlot(3).grow(getStackInSlot(x).getCount());
                        setStackInSlot(x, ItemStack.EMPTY);
                    } else {
                        getStackInSlot(x).shrink(spaceFree);
                        getStackInSlot(3).grow(spaceFree);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Used to determine if has space to output
     * @return True if can grind
     */
    private boolean hasOutputAvailable() {
        ItemStack output = ((GrinderRecipeHandler)RecipeManager.getHandler(RecipeManager.RecipeType.GRINDER))
                    .getOutput(getStackInSlot(3));
        for(int x = 4; x < 7; x++) {
            if(!getStackInSlot(x).isEmpty() && getStackInSlot(x).getCount() <= getStackInSlot(x).getMaxStackSize() &&
                    getStackInSlot(x).getItem() == output.getItem() &&
                    getStackInSlot(x).getItemDamage() == output.getItemDamage() &&
                    getStackInSlot(x).getCount() + output.getCount() <= getStackInSlot(x).getMaxStackSize()) {
                return true;
            }
        }
        for(int x = 4; x < 7; x++) {
            if (getStackInSlot(x).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Grinds the current item, decreasing the current slot and adding output
     */
    private void grindItem() {
        ItemStack output = ((GrinderRecipeHandler)RecipeManager.getHandler(RecipeManager.RecipeType.GRINDER))
                .getOutput(getStackInSlot(3));
        if(output == null)
            return;
        for(int x = 4; x < 7; x++) {
            if(!getStackInSlot(x).isEmpty() &&
                    getStackInSlot(x).getCount() <= getStackInSlot(x).getMaxStackSize() &&
                    getStackInSlot(x).getItem() == output.getItem() &&
                    getStackInSlot(x).getItemDamage() == output.getItemDamage() &&
                    getStackInSlot(x).getCount() + output.getCount() <= getStackInSlot(x).getMaxStackSize()) {
                getStackInSlot(3).shrink(1);
                getStackInSlot(x).grow(output.getCount());
                if(getStackInSlot(3).getCount() <= 0)
                    setStackInSlot(3, ItemStack.EMPTY);
                return;
            }
        }
        for(int x = 4; x < 7; x++) {
            if(getStackInSlot(x).isEmpty()) {
                getStackInSlot(3).shrink(1);
                setStackInSlot(x, output.copy());
                if(getStackInSlot(3).getCount() <= 0)
                    setStackInSlot(3, ItemStack.EMPTY);
                return;
            }
        }
    }

    /*******************************************************************************************************************
     * TileEntity                                                                                                      *
     *******************************************************************************************************************/

    /**
     * Used to save the inventory to an NBT tag
     *
     * @param compound The tag to save to
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Progress", progress);
        return compound;
    }

    /**
     * Used to read the inventory from an NBT tag compound
     *
     * @param compound The tag to read from
     */
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        progress = compound.getInteger("Progress");
    }

    /*******************************************************************************************************************
     * InventoryHandler                                                                                                *
     *******************************************************************************************************************/

    /**
     * The initial size of the inventory
     *
     * @return How big to make the inventory on creation
     */
    @Override
    public int getInventorySize() {
        return 7;
    }

    /**
     * Used to define if an item is valid for a slot
     *
     * @param index The slot id
     * @param stack The stack to check
     * @return True if you can put this there
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index < 3 && RecipeManager.getHandler(RecipeManager.RecipeType.GRINDER).isValidInput(stack);
    }

    @Override
    protected void onInventoryChanged(int slot) {
        super.onInventoryChanged(slot);
        markForUpdate(6);
    }

    /*******************************************************************************************************************
     * InventoryHandler                                                                                                *
     *******************************************************************************************************************/

    /**
     * Used to set the value of a field
     *
     * @param id    The field id
     * @param value The value of the field
     */
    @Override
    public void setVariable(int id, double value) {
        switch (id) {
            case CREATE_EFFECTS_VARID :
                for(int x = 0; x < 4; x++)
                    world.spawnParticle(EnumParticleTypes.FALLING_DUST,
                            pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D,
                            0D, 0D, 0D,
                            Block.getStateId(Blocks.GRAVEL.getDefaultState()));
                break;
            default :
        }
    }

    /**
     * Used to get the field on the server, this will fetch the server value and overwrite the current
     *
     * @param id The field id
     * @return The value on the server, now set to ourselves
     */
    @Override
    public Double getVariable(int id) {
        return null;
    }
}
