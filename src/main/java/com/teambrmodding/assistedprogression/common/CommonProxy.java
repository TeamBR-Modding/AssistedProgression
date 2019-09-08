package com.teambrmodding.assistedprogression.common;

import com.teambrmodding.assistedprogression.managers.EventManager;
import net.minecraftforge.common.MinecraftForge;

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
public class CommonProxy {
    public void init() {
        MinecraftForge.EVENT_BUS.addListener(EventManager::onPlayerLogin);
    }
}
