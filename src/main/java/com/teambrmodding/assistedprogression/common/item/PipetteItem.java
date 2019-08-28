package com.teambrmodding.assistedprogression.common.item;

import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
 * @since 8/25/2019
 */
public class PipetteItem extends Item implements IAdvancedToolTipProvider {

    public PipetteItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(ItemManager.itemGroupAssistedProgression));
        setRegistryName("pipette");
    }

    /*******************************************************************************************************************
     * IAdvancedTooltipProvider                                                                                        *
     *******************************************************************************************************/

    /**
     * Get the tooltip
     * @param stack Itemstack to name
     * @return List of tips
     */
    @Nullable
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        return Collections.singletonList("NOT IMPLEMENTED: Forge has not finalized fluids yet for 1.14.4, waiting for updates");
    }
}
