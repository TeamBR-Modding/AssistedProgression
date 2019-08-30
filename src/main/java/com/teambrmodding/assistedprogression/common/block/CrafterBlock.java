package com.teambrmodding.assistedprogression.common.block;

import com.teambrmodding.assistedprogression.common.tile.CrafterTile;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
 * @since 8/29/2019
 */
public class CrafterBlock extends BaseBlock {

    /**
     * Base Constructor
     */
    public CrafterBlock() {
        super(Properties.create(Material.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(1.0F),
                "crafter", CrafterTile.class);
    }

    /*******************************************************************************************************************
     * Block                                                                                                           *
     *******************************************************************************************************************/

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                    PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return true;
        }
        else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof CrafterTile) {
                CrafterTile grinder = (CrafterTile) tileentity;
                NetworkHooks.openGui((ServerPlayerEntity) player, grinder, pos);
            }
            return true;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
