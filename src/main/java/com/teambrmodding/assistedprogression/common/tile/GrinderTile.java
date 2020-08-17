package com.teambrmodding.assistedprogression.common.tile;

import com.teambr.nucleus.common.tiles.InventorySided;
import com.teambrmodding.assistedprogression.common.container.GrinderContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.managers.TileEntityManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/27/2019
 */
public class GrinderTile extends InventorySided implements INamedContainerProvider {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Syncable variables
    public static final int CREATE_EFFECTS_VARID = 0;

    // Defined variables
    public static final int MAX_PROGRESS = 15;

    // Operating variables
    public int progress = 0;

    public GrinderTile() {
        super(TileEntityManager.grinder);
    }

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
                if(!getStackInSlot(x).isEmpty() && getStackInSlot(x).getItem() == getStackInSlot(3).getItem()) {
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
        ItemStack output = RecipeHelper.getGrinderOutput(getStackInSlot(3), world.getRecipeManager());
        for(int x = 4; x < 7; x++) {
            if(!getStackInSlot(x).isEmpty() && getStackInSlot(x).getCount() <= getStackInSlot(x).getMaxStackSize() &&
                    getStackInSlot(x).getItem() == output.getItem() &&
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
        ItemStack output = RecipeHelper.getGrinderOutput(getStackInSlot(3), world.getRecipeManager());
        if(output.isEmpty())
            return;
        for(int x = 4; x < 7; x++) {
            if(!getStackInSlot(x).isEmpty() &&
                    getStackInSlot(x).getCount() <= getStackInSlot(x).getMaxStackSize() &&
                    getStackInSlot(x).getItem() == output.getItem() &&
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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("Progress", progress);
        return compound;
    }

    /**
     * Used to read the inventory from an NBT tag compound
     *
     * @param compound The tag to read from
     */
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        progress = compound.getInt("Progress");
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
        return index < 3 && (world != null && RecipeHelper.isValidGrinderInput(stack, world.getRecipeManager()));
    }

    @Override
    protected void onInventoryChanged(int slot) {
        super.onInventoryChanged(slot);
        markForUpdate(6);
    }

    /**
     * Get the slots for the given face
     *
     * @param face The face
     * @return What slots can be accessed
     */
    @Override
    public int[] getSlotsForFace(Direction face) {
        if(face == Direction.DOWN)
            return new int[] {4, 5, 6};
        return new int[] {1, 2, 3};
    }

    /**
     * Can insert the item into the inventory
     *
     * @param slot        The slot
     * @param itemStackIn The stack to insert
     * @param dir         The dir
     * @return True if can insert
     */
    @Override
    public boolean canInsertItem(int slot, ItemStack itemStackIn, Direction dir) {
        return dir != Direction.DOWN && isItemValidForSlot(slot, itemStackIn);
    }

    /**
     * Can this extract the item
     *
     * @param slot  The slot
     * @param stack The stack
     * @param dir   The dir
     * @return True if can extract
     */
    @Override
    public boolean canExtractItem(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot > 3;
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
        if(world != null)
            switch (id) {
                case CREATE_EFFECTS_VARID :
                    for(int x = 0; x < 4; x++)
                        world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, Blocks.GRAVEL.getDefaultState()),
                                pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D,
                                0D, 0D, 0D);
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

    /*******************************************************************************************************************
     * INamedContainerProvider                                                                                         *
     *******************************************************************************************************************/

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Reference.MOD_ID + ".grinder");
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new GrinderContainer(windowID, playerInventory, this);
    }
}
