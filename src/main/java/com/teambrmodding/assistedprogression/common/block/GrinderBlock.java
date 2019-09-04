package com.teambrmodding.assistedprogression.common.block;

import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

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
public class GrinderBlock extends BaseBlock {

    private static final VoxelShape NORTH_LOWER_RIM = Block.makeCuboidShape(2.0, 0.0, 0.0, 14.0, 2.0, 2.0);
    private static final VoxelShape EAST_LOWER_RIM  = Block.makeCuboidShape(14.0, 0.0, 2.0, 16.0, 2.0, 14.0);
    private static final VoxelShape SOUTH_LOWER_RIM = Block.makeCuboidShape(2.0, 0.0, 14.0, 14.0, 2.0, 16.0);
    private static final VoxelShape WEST_LOWER_RIM  = Block.makeCuboidShape(0.0, 0.0, 2.0, 2.0, 2.0, 14.0);
    private static final VoxelShape NEUP            = Block.makeCuboidShape(14.0, 0.0, 0.0, 16.0, 14.0, 2.0);
    private static final VoxelShape SEUP            = Block.makeCuboidShape(14.0, 0.0, 14.0, 16.0, 14.0, 16.0);
    private static final VoxelShape SWUP            = Block.makeCuboidShape(0.0, 0.0, 14.0, 2.0, 14.0, 16.0);
    private static final VoxelShape NWUP            = Block.makeCuboidShape(0.0, 0.0, 0.0, 2.0, 14.0, 2.0);
    private static final VoxelShape LOWER_PLANE     = Block.makeCuboidShape(2.0, 0.001, 2.0, 14.0, 0.05, 14.0);
    private static final VoxelShape TOP_SLAB        = Block.makeCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape CHUTE_TOP       = Block.makeCuboidShape(2.0, 10.0, 2.0, 14.0, 14.0, 14.0);
    private static final VoxelShape CHUTE_MIDDLE    = Block.makeCuboidShape(3.0, 8.0, 3.0, 13.0, 10.0, 13.0);
    private static final VoxelShape CHUTE_BOTTOM_N  = Block.makeCuboidShape(6.0, 5.0, 6.0, 10.0, 8.0, 7.0);
    private static final VoxelShape CHUTE_BOTTOM_E  = Block.makeCuboidShape(9.0, 5.0, 7.0, 10.0, 8.0, 9.0);
    private static final VoxelShape CHUTE_BOTTOM_W  = Block.makeCuboidShape(6.0, 5.0, 7.0, 7.0, 8.0, 9.0);
    private static final VoxelShape CHUTE_BOTTOM_S  = Block.makeCuboidShape(6.0, 5.0, 9.0, 10.0, 8.0, 10.0);
    private static final VoxelShape ALL_COMBINED    = VoxelShapes.or(NORTH_LOWER_RIM, EAST_LOWER_RIM, SOUTH_LOWER_RIM,
            WEST_LOWER_RIM, NEUP, SEUP, SWUP, NWUP, LOWER_PLANE, TOP_SLAB, CHUTE_TOP, CHUTE_MIDDLE, CHUTE_BOTTOM_N,
            CHUTE_BOTTOM_E, CHUTE_BOTTOM_S, CHUTE_BOTTOM_W);

    /**
     * Base Constructor
     */
    public GrinderBlock() {
        super(Properties.create(
                new Material(MaterialColor.STONE, false, false,
                        true, false,
                        false, false,
                        false, PushReaction.NORMAL))
                        .harvestTool(ToolType.PICKAXE).hardnessAndResistance(2.0F),
                "grinder", GrinderTile.class);
    }

    /*******************************************************************************************************************
     * Block                                                                                                           *
     *******************************************************************************************************************/

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return ALL_COMBINED;
    }

    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);

        if(entityIn instanceof PlayerEntity && entityIn.fallDistance > 0.0 &&
                !worldIn.getBlockState(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ)).isAir(worldIn, new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ))) {

            Block landedBlock = worldIn.getBlockState(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ)).getBlock();

            if(RecipeHelper.pressurePlates.containsKey(landedBlock))
                ((GrinderTile)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, RecipeHelper.pressurePlates.get(landedBlock));
        }
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                    PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return true;
        }
        else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof GrinderTile) {
                GrinderTile grinder = (GrinderTile) tileentity;
                NetworkHooks.openGui((ServerPlayerEntity) player, grinder, pos);
            }
            return true;
        }
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
