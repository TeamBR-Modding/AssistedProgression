package com.teambrmodding.assistedprogression.common.block;

import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
 * @since 8/24/2019
 */
public class PlayerPlateBlock extends AbstractPressurePlateBlock implements IAdvancedToolTipProvider {

    public PlayerPlateBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(2.0F)
                .sound(SoundType.METAL));
        setRegistryName("player_plate");
        setDefaultState(this.getStateContainer().getBaseState().with(PressurePlateBlock.POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PressurePlateBlock.POWERED);
    }

    /*******************************************************************************************************************
     * BlockBasePressurePlate                                                                                          *
     *******************************************************************************************************************/

    @Override
    protected void playClickOnSound(IWorld world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON,
                SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playClickOffSound(IWorld world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF,
                SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(PlayerEntity.class,
                PRESSURE_AABB.offset(pos)).size() >= 1 ? 15 : 0;
    }

    @Override
    protected int getRedstoneStrength(BlockState state) {
        return state.get(PressurePlateBlock.POWERED) ? 15 : 0;
    }

    @Override
    protected BlockState setRedstoneStrength(BlockState state, int strength) {
        return state.with(PressurePlateBlock.POWERED, strength > 0);
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
