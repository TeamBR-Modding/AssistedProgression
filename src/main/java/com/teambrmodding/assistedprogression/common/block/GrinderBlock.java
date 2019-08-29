package com.teambrmodding.assistedprogression.common.block;

import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
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
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);

        if(entityIn instanceof PlayerEntity && entityIn.fallDistance > 0.0 &&
                !worldIn.getBlockState(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ)).isAir(worldIn, new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ))) {
            Block landedBlock = worldIn.getBlockState(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ)).getBlock();
            if(landedBlock.getTags().contains(new ResourceLocation(Reference.MOD_ID, "pressure_plate")))
                ((GrinderTile)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.0);
            else if(landedBlock == Blocks.STONE_PRESSURE_PLATE)
                ((GrinderTile)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.25);
            else if(landedBlock == BlockManager.player_plate)
                ((GrinderTile)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.5);
            else if(landedBlock == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                ((GrinderTile)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.75);
            else if(landedBlock == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                ((GrinderTile)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 2.0);
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
