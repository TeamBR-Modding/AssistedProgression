package com.teambrmodding.assistedprogression.common.blocks;

import com.teambr.bookshelf.common.IAdvancedToolTipProvider;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 6/2/2017
 */
public class BlockGrinder extends BaseBlock implements IAdvancedToolTipProvider {

    protected BlockGrinder() {
        super(Material.ROCK, "blockGrinder", TileGrinder.class);
    }

    @Override
    public void onLanded(World world, Entity entity) {
        super.onLanded(world, entity);

        if (entity instanceof EntityPlayer && entity.fallDistance > 0.0 && !world.isAirBlock(entity.getPosition())) {
            Block landedBlock = world.getBlockState(entity.getPosition()).getBlock();
            TileGrinder grinder = (TileGrinder) world.getTileEntity(entity.getPosition().down());
            assert grinder != null;
            if (Blocks.WOODEN_PRESSURE_PLATE == landedBlock)
                grinder.activateGrinder((int)entity.fallDistance,1.00);
            else if (Blocks.STONE_PRESSURE_PLATE == landedBlock)
                grinder.activateGrinder((int)entity.fallDistance,1.25);
            else if (BlockManager.blockPlayerPlate() == landedBlock)
                grinder.activateGrinder((int)entity.fallDistance,1.50);
            else if (Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE == landedBlock)
                grinder.activateGrinder((int)entity.fallDistance,1.75);
            else if (Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE == landedBlock)
                grinder.activateGrinder((int)entity.fallDistance,2.00);
        }
    }

    /*@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }*/

    /*@SideOnly(Side.CLIENT)
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return (layer == BlockRenderLayer.CUTOUT) || (layer == BlockRenderLayer.TRANSLUCENT);
    }

    @Nullable
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        return new ArrayList<>(Arrays.asList("Place any vanilla pressure plate on top", "Jump on the plate to grind ores", "Better plates work faster"));
    }
}
