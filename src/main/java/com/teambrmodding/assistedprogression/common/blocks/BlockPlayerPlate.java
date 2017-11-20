package com.teambrmodding.assistedprogression.common.blocks;

import com.teambr.nucleus.annotation.IRegisterable;
import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.helper.ItemRenderHelper;
import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/13/17
 */
public class BlockPlayerPlate extends BlockBasePressurePlate implements IRegisterable<Block>, IAdvancedToolTipProvider {

    /**
     * Constructor, since we are not extending BaseBlock we need to do some manual stuff
     */
    public BlockPlayerPlate() {
        super(Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(BlockPressurePlate.POWERED, false));
        setUnlocalizedName(Reference.MOD_ID + ":" + "block_player_plate");
        setRegistryName(new ResourceLocation(Reference.MOD_ID, "block_player_plate"));
        setCreativeTab(AssistedProgression.tabAssistedProgression);
        setHardness(2.0F);
    }

    /*******************************************************************************************************************
     * Registration                                                                                                    *
     *******************************************************************************************************************/

    /**
     * Registers an object to the ForgeRegistry
     *
     * @param registry The Block Forge Registry
     */
    @Override
    public void registerObject(IForgeRegistry<Block> registry) {
        registry.register(this);
    }

    /**
     * Register the renderers for the block/item
     */
    @Override
    public void registerRender() {
        ItemRenderHelper.registerBlockModel(this, "powered=false");
    }

    /*******************************************************************************************************************
     * BlockBasePressurePlate                                                                                          *
     *******************************************************************************************************************/

    @Override
    protected void playClickOnSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON,
                SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playClickOffSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF,
                SoundCategory.BLOCKS, 0.3F, 0.5F);
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(EntityPlayer.class,
                PRESSURE_AABB.offset(pos)).size() >= 1 ? 15 : 0;
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 15 : 0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(BlockPressurePlate.POWERED, strength > 0);
    }

    /*******************************************************************************************************************
     * BlockState Methods                                                                                              *
     *******************************************************************************************************************/

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockPressurePlate.POWERED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockPressurePlate.POWERED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 1 : 0;
    }


    /*******************************************************************************************************************
     * IAdvancedToolTipProvided                                                                                        *
     *******************************************************************************************************************/

    /**
     * Get the tool tip to present when shift is pressed
     *
     * @param stack The itemstack
     * @return The list to display
     */
    @Nullable
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        return Collections.singletonList(ClientUtils.translate("block_player_plate.desc"));
    }
}
