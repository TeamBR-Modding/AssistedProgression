package com.teambrmodding.assistedprogression.common.tiles.storage;

import com.teambr.bookshelf.common.tiles.InventoryHandler;
import com.teambrmodding.assistedprogression.common.container.storage.ContainerFlushableChest;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

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
public class TileFlushableChest extends InventoryHandler {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    public static final int PLAY_FLUSH_SOUND_VARID = 0;
    public static final int AUTO_FLUSH_VARID       = 1;
    public static final int FLUSH_INTERVAL_VARID   = 2;
    public static final int CREATE_EFFECT_VARID    = 100;

    // Lid angle variables
    public float prevLidAngle  = 0;
    public float lidAngle      = 0;
    public int numUsingPlayers = 0;
    public int ticksSinceSync  = -1;

    // Function variables
    private boolean playFlushSound = true;
    private boolean autoFlush      = false;
    private int     flushInterval  = 10;
    private int     flushTimer     = 0;

    /*******************************************************************************************************************
     * TileFlushableChest                                                                                              *
     *******************************************************************************************************************/

    /**
     * Clears the inventory and sends ping to client to play effect
     */
    public void clear() {
        // Clear inventory
        for(ItemStack stack : inventoryContents)
            stack = ItemStack.EMPTY;

        // Safety check, should only be called from server
        if(world != null && !world.isRemote) {
            if(playFlushSound) {
                world.playSound(null,
                        (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D,
                        SoundEvents.BLOCK_LAVA_EXTINGUISH,
                        SoundCategory.BLOCKS,
                        0.3F, 0.5F);
            }
            sendValueToClient(CREATE_EFFECT_VARID, 0);
        }
    }

    /**
     * Used to save the inventory to an NBT tag
     *
     * @param compound The tag to save to
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("PlayFlushSound", playFlushSound);
        compound.setBoolean("AutoFlush", autoFlush);
        compound.setInteger("FlushInterval", flushInterval);
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
        playFlushSound = compound.getBoolean("PlayFlushSound");
        autoFlush      = compound.getBoolean("AutoFlush");
        flushInterval  = compound.getInteger("FlushInterval");
    }

    /*******************************************************************************************************************
     * TileEntity                                                                                                      *
     *******************************************************************************************************************/

    @Override
    public void update() {
        // Auto Flush timer
        if(autoFlush && !world.isRemote) {
            if(flushTimer / 20 >= flushInterval) {
                this.clear();
                flushTimer = 0;
            } else {
                flushTimer++;
            }
        }

        if(world != null && !world.isRemote && numUsingPlayers != 0
                && (ticksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0) {
            numUsingPlayers = 0;
            float usableDistance = 5.0F;
            List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class,
                    new AxisAlignedBB(pos.getX() - usableDistance, pos.getY() - usableDistance, pos.getZ() - usableDistance,
                            pos.getX() + 1 + usableDistance, pos.getY() + 1 + usableDistance, pos.getZ() + 1 + usableDistance));

            for (EntityPlayer player : playerList) {
                if (player.openContainer instanceof ContainerFlushableChest)
                    numUsingPlayers += 1;
            }
        }

        if(world != null && ticksSinceSync < 0 && !world.isRemote)
            world.addBlockEvent(pos, BlockManager.blockFlushableChest(), 0, numUsingPlayers);

        ticksSinceSync += 1;
        prevLidAngle = lidAngle;
        float f = 0.1F;
        if(numUsingPlayers > 0 && lidAngle == 0.0F)
            world.playSound(null, pos, SoundEvents.BLOCK_CHEST_OPEN,
                    SoundCategory.BLOCKS, 0.3F, 0.5F);
        if((numUsingPlayers == 0 && lidAngle > 0.0F) || (numUsingPlayers > 0 && lidAngle < 1.0F)) {
            float f1 = lidAngle;
            if(numUsingPlayers > 0)
                lidAngle += f;
            else
                lidAngle -= f;

            if(lidAngle > 1.0F)
                lidAngle = 1.0F;

            float f2 = 0.5F;
            if(lidAngle < f2 && f1 > f2)
                world.playSound(null, pos, SoundEvents.BLOCK_CHEST_CLOSE,
                        SoundCategory.BLOCKS, 0.3F, 0.5F);
            if(lidAngle < 0.0F)
                lidAngle = 0.0F;
        }
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
    protected int getInventorySize() {
        return 27;
    }

    /**
     * Used to define if an item is valid for a slot
     *
     * @param index The slot id
     * @param stack The stack to check
     * @return True if you can put this there
     */
    @Override
    protected boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    /*******************************************************************************************************************
     * Syncable                                                                                                        *
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
            case PLAY_FLUSH_SOUND_VARID :
                playFlushSound = value == -1 ? false : true;
                break;
            case AUTO_FLUSH_VARID :
                autoFlush = value == -1 ? false : true;
                break;
            case FLUSH_INTERVAL_VARID :
                flushInterval = (int) value;
                break;
            case CREATE_EFFECT_VARID :
                if(world != null && world.isRemote) {
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 0.3, 0, 0, 0);
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.7, pos.getY() + 1, pos.getZ() + 0.7, 0, 0, 0);
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 0.7, 0, 0, 0);
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.7, pos.getY() + 1, pos.getZ() + 0.3, 0, 0, 0);
                }
                break;
            default:
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
        switch (id) {
            case PLAY_FLUSH_SOUND_VARID :
                return playFlushSound ? 1.0 : -1.0;
            case AUTO_FLUSH_VARID :
                return autoFlush ? 1.0 : -1.0;
            case FLUSH_INTERVAL_VARID :
                return (double) flushInterval;
            default :
            return 0.0;
        }
    }
}
