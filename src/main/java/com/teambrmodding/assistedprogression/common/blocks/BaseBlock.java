package com.teambrmodding.assistedprogression.common.blocks;

import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
public class BaseBlock extends BlockContainer {

    private Class<? extends TileEntity> tileClass;
    public String name;
    public AxisAlignedBB BB = new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F);

    protected BaseBlock(Material material, String name, Class<? extends TileEntity> tileEntityClass) {
        super(material);
        this.tileClass = tileEntityClass;
        this.name = name;

        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        setUnlocalizedName(Reference.MOD_ID + ":" + name);

        setHardness(getHardness());

        if(getCreativeTab() != null)
            setCreativeTab(getCreativeTab());
    }

    /**
     * Used to tell if this should be in a creative tab, and if so which one
     *
     * @return Null if none, defaults to the main NeoTech Tab
     */
    protected CreativeTabs getCreativeTab() {
        return AssistedProgression.tabAssistedProgression;
    }

    /**
     * Used to change the hardness of a block, but will default to 2.0F if not overwritten
     *
     * @return The hardness value, default 2.0F
     */
    protected float getHardness() {
        return 2.0F;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (tileClass != null)
            try {
                return tileClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * Block container changes this, we still want a normal model
     * @return The model type
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public AxisAlignedBB setBlockBounds(Float x1, Float y1, Float z1, Float x2, Float y2, Float z2) {
        BB = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
        return BB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }
}
