package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.client.screen.CrafterScreen;
import com.teambrmodding.assistedprogression.client.screen.GrinderScreen;
import com.teambrmodding.assistedprogression.client.screen.TrashBagScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    @OnlyIn(Dist.CLIENT)
    public static void registerScreens() {
        ScreenManager.registerFactory(ContainerManager.grinder, GrinderScreen::new);
        ScreenManager.registerFactory(ContainerManager.crafter, CrafterScreen::new);
        ScreenManager.registerFactory(ContainerManager.trash_bag, TrashBagScreen::new);
    }
}
