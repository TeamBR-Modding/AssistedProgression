package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

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
public class ConfigManager {
    public static final Configuration config = new Configuration(
            new File(AssistedProgression.configFolderLocation + File.separator + "AssistedProgression.cfg"));

    public static int totalRFMagnet = 25000;

    public static void preinit() {
        config.load();

        totalRFMagnet = config.get(Reference.CONFIG_MAGNET, "Total RF Magnet", 25000,
                "The max RF stored in the magnet").getInt();

        File path = new File(AssistedProgression.configFolderLocation + File.separator + "Registries");
        if(!path.exists())
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
    }
}
