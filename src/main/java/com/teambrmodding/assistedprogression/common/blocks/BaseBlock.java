package com.teambrmodding.assistedprogression.common.blocks;

import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.common.IOpensGui;
import com.teambr.bookshelf.util.WorldUtils;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * This file was created for NeoTech
 *
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 2/15/2017
 */
public class BaseBlock extends BlockContainer {
    private Class<? extends TileEntity> tileClass;
    public String name;

    public BaseBlock(Material materialIn, String name, Class<? extends TileEntity> tileEntityClass) {
        super(materialIn);
        this.tileClass = tileEntityClass;
        this.name = name;

        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        setUnlocalizedName(Reference.MOD_ID + ":" + name);

        setHardness(getHardness());

        if(getCreativeTab() != null)
            setCreativeTab(getCreativeTab());
    }


    /*******************************************************************************************************************
     * Block Methods                                                                                                   *
     *******************************************************************************************************************/

    /**
     * Called when the block is broken, allows us to drop items from inventory
     * @param worldIn The world
     * @param pos The pos
     * @param state The state
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote) {
            if(worldIn.getTileEntity(pos) != null
                    && worldIn.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                // Drop the inventory
                WorldUtils.dropStacksInInventory(worldIn.getTileEntity(pos)
                        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), worldIn, pos);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Called when the block is clicked on
     * @return True to prevent future logic
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Make sure our machine is reachable
        if (worldIn.getTileEntity(pos) != null && worldIn.getBlockState(pos).getBlock() instanceof IOpensGui) {
            // Open a GUI
            if(!playerIn.isSneaking()) {
                playerIn.openGui(Bookshelf.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
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
        return 1.5F;
    }

    /*******************************************************************************************************************
     * BlockContainer                                                                                                  *
     *******************************************************************************************************************/

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if(tileClass != null)
            try {
                return tileClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        return null;
    }

    /*******************************************************************************************************************
     * Block                                                                                                           *
     *******************************************************************************************************************/

    /**
     * Block container changes this, we still want a normal model
     * @return The model type
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}