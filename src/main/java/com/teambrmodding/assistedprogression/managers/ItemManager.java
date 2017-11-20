package com.teambrmodding.assistedprogression.managers;

import com.teambr.nucleus.annotation.RegisteringItem;
import com.teambrmodding.assistedprogression.common.items.*;

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
public class ItemManager {

    /*******************************************************************************************************************
     * Item Variables                                                                                                  *
     *******************************************************************************************************************/

    @RegisteringItem
    public static ItemMagnet itemCheapMagnet = new ItemMagnet(true, "item_cheap_magnet");

    @RegisteringItem
    public static ItemMagnet itemElectroMagnet = new ItemMagnet(false,"item_electro_magnet");

    @RegisteringItem
    public static ItemTrashBag itemTrashBag = new ItemTrashBag("item_trash_bag", 1);

    @RegisteringItem
    public static ItemTrashBag itemHeftyBag = new ItemTrashBag("item_hefty_bag", 18);

    @RegisteringItem
    public static ItemSpawnerRelocator itemSpawnerRelocator = new ItemSpawnerRelocator();

    @RegisteringItem
    public static ItemExchanger itemExchanger = new ItemExchanger();

    @RegisteringItem
    public static ItemPipette itemPipette = new ItemPipette();
}
