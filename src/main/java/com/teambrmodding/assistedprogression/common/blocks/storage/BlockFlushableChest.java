package com.teambrmodding.assistedprogression.common.blocks.storage;

import com.teambr.bookshelf.common.IOpensGui;
import com.teambr.bookshelf.util.WorldUtils;
import com.teambrmodding.assistedprogression.client.gui.storage.GuiFlushableChest;
import com.teambrmodding.assistedprogression.common.blocks.BaseBlock;
import com.teambrmodding.assistedprogression.common.container.storage.ContainerFlushableChest;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.teambr.bookshelf.common.blocks.BlockFourWayRotating.FOUR_WAY;

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
public class BlockFlushableChest extends BaseBlock implements IOpensGui {

    /**
     * Constructor
     */
    protected BlockFlushableChest() {
        super(Material.ROCK, "flushableChest", TileFlushableChest.class);
    }

    /*******************************************************************************************************************
     * Block Methods                                                                                                   *
     *******************************************************************************************************************/

    /**
     * Called when the block is placed
     * @param worldIn The world
     * @param pos The block position
     * @param state The block state
     * @param placer Who placed the block
     * @param stack The stack that was the block
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int playerFacingDirection = placer == null ? 0 : MathHelper.floor((placer.rotationYaw / 90.F) + 0.5F) & 3;
        EnumFacing facing = EnumFacing.getHorizontal(playerFacingDirection).getOpposite();
        worldIn.setBlockState(pos, getDefaultState().withProperty(FOUR_WAY, facing));
        WorldUtils.writeStackNBTToBlock(worldIn, pos, stack);
    }

    /**
     * Called when neighbor changes so we can check for Redstone Signal
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!worldIn.isRemote) {
            if(worldIn.isBlockPowered(pos)) {
                ((TileFlushableChest)worldIn.getTileEntity(pos)).clear();
                worldIn.setBlockState(pos, state, 6);
            }
        }
    }

    /**
     * The following enable transparent textures to be rendered on top of the model
     * Is listed deprecated in favor of logic in block state, but our state calls this*
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /*******************************************************************************************************************
     * BlockState Methods                                                                                              *
     *******************************************************************************************************************/

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FOUR_WAY).getIndex();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     *
     * @param meta
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if(meta == EnumFacing.DOWN.getIndex() || meta == EnumFacing.UP.getIndex())
            return getDefaultState();
        return getDefaultState().withProperty(FOUR_WAY, EnumFacing.getFront(meta));
    }

    /**
     * Creates the block state with our properties
     * @return The block state
     */
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FOUR_WAY);
    }

    /*******************************************************************************************************************
     * IOpensGui                                                                                                       *
     *******************************************************************************************************************/

    /**
     * Return the container for this tile
     *
     * @param id     Id, probably not needed but could be used for multiple guis
     * @param player The player that is opening the gui
     * @param world  The world
     * @param x      X Pos
     * @param y      Y Pos
     * @param z      Z Pos
     * @return The container to open
     */
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerFlushableChest(player.inventory, (TileFlushableChest) world.getTileEntity(new BlockPos(x, y, z)));
    }

    /**
     * Return the gui for this tile
     *
     * @param id     Id, probably not needed but could be used for multiple guis
     * @param player The player that is opening the gui
     * @param world  The world
     * @param x      X Pos
     * @param y      Y Pos
     * @param z      Z Pos
     * @return The gui to open
     */
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiFlushableChest(player, (TileFlushableChest)world.getTileEntity(new BlockPos(x, y, z)));
    }
}
