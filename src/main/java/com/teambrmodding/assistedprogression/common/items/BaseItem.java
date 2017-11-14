package com.teambrmodding.assistedprogression.common.items;

import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.item.Item;

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
public class BaseItem extends Item {

    // Item name
    private String name;

    /**
     * Base constructor for all items
     * @param itemName The item name
     * @param maxStackSize Maximum stack size
     */
    public BaseItem(String itemName, int maxStackSize) {
        super();
        setCreativeTab(AssistedProgression.tabAssistedProgression());
        setMaxStackSize(maxStackSize);
        setUnlocalizedName(Reference.MOD_ID + ":" + name);
    }

    /*******************************************************************************************************************
     * BaseItem                                                                                                        *
     *******************************************************************************************************************/

    /**
     * Used to get the name of this item
     * @return Given name
     */
    public String getName() {
        return name;
    }
}
