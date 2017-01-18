package com.teambrmodding.assistedprogression.managers

import java.io.File

import com.teambrmodding.assistedprogression.AssistedProgression
import com.teambrmodding.assistedprogression.lib.Reference
import net.minecraftforge.common.config.Configuration

/**
  * This file was created for Assisted-Progression
  *
  * Assisted-Progression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis - pauljoda
  * @since 1/16/2017
  */
object ConfigManager {

    // Config Object
    val config = new Configuration(new File(AssistedProgression.configFolderLocation + File.separator + "AssistedProgression.cfg"))

    var totalRFMagnet = 25000

    def preInit(): Unit = {
        config.load()

        totalRFMagnet = config.get(Reference.CONFIG_MAGNET, "Total RF Magnet", 25000, "The max RF stored in the magnet").getInt()

        config.save()

        //Check to make sure Registry Directory is made
        val path: File = new File(AssistedProgression.configFolderLocation + File.separator + "Registries")
        if (!path.exists) path.mkdirs
    }
}
