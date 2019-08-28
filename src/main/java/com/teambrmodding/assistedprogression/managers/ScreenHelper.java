package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.client.screen.GrinderScreen;
import net.minecraft.client.gui.ScreenManager;

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
public class ScreenHelper {
    public static void registerScreens() {
        ScreenManager.registerFactory(ContainerManager.grinder, GrinderScreen::new);
    }
}
