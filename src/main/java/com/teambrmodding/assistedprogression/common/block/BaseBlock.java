package com.teambrmodding.assistedprogression.common.block;

import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.block.ContainerBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

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
 * @since 8/27/2019
 */
public class BaseBlock extends ContainerBlock implements IAdvancedToolTipProvider {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    private Class<? extends TileEntity> tileClass;
    public String registryName;

    /**
     * Base Constructor
     * @param builder Properties
     * @param name Name for this block
     * @param tileEntityClass TileEntity class
     */
    protected BaseBlock(Properties builder, String name, Class<? extends TileEntity> tileEntityClass) {
        super(builder);
        this.tileClass = tileEntityClass;
        this.registryName = name;

        setRegistryName(name);
    }

    /*******************************************************************************************************************
     * ContainerBlock                                                                                                  *
     *******************************************************************************************************************/

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        if(tileClass != null)
            try {
                return tileClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        return null;
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
        return Collections.singletonList(ClientUtils.translate(this.registryName + ".desc"));
    }
}
