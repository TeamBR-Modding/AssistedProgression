package com.teambrmodding.assistedprogression.managers;

import net.minecraftforge.common.MinecraftForge;

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
public class EventManager {

    /**
     * Registers the events for this mod
     */
    public static void registerEvents() {
        registerEvent(new BlockManager());
        registerEvent(new ItemManager());
        registerEvent(ItemManager.itemTrashBag);
    }

    public static void registerEvent(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }
}
