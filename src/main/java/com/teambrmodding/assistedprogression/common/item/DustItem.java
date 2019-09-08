package com.teambrmodding.assistedprogression.common.item;

import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.item.Item;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/28/2019
 */
public class DustItem extends Item {
    private int color;
    public DustItem(String name, int itemColor) {
        super(new Properties()
                .group(ItemManager.itemGroupAssistedProgression));
        setRegistryName(name);
        this.color = itemColor;
    }

    public int getColor() {
        return color;
    }
}
