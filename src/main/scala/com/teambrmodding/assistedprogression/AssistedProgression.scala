package com.teambrmodding.assistedprogression

import java.io.File

import com.teambrmodding.assistedprogression.common.CommonProxy
import com.teambrmodding.assistedprogression.common.items.ItemTrashBag
import com.teambrmodding.assistedprogression.lib.Reference
import com.teambrmodding.assistedprogression.managers._
import net.minecraft.command.ServerCommandManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import org.apache.logging.log4j.LogManager

/**
  * This file was created for AssistedProgression
  *
  * AssistedProgression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 1/16/2017
  */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, modLanguage = "scala")
object AssistedProgression {

    //The logger. For logging
    final val logger = LogManager.getLogger(Reference.MOD_NAME)

    var configFolderLocation : String = ""

    @SidedProxy(clientSide = "com.teambrmodding.assistedprogression.client.ClientProxy",
        serverSide = "com.teambrmodding.assistedprogression.common.CommonProxy")
    var proxy : CommonProxy = null

    val tabAssistedProgression: CreativeTabs = new CreativeTabs("tabAssistedProgression") {
        override def getTabIconItem: Item = Items.BOOK
    }

    @EventHandler def preInit(event : FMLPreInitializationEvent) = {
        configFolderLocation = event.getModConfigurationDirectory.getAbsolutePath + File.separator + "AssistedProgression"
        ConfigManager.preInit()
        BlockManager.preInit()
        ItemManager.preInit()
        RecipeManager.preInit()
        proxy.preInit()
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ItemGuiManager)
    }

    @EventHandler def init(event : FMLInitializationEvent) =  {
        RecipeManager.init()
        proxy.init()
        MinecraftForge.EVENT_BUS.register(ItemTrashBag)
    }

    @EventHandler def postInit(event : FMLPostInitializationEvent) = {
        proxy.postInit()
    }

    @EventHandler def serverLoad(event : FMLServerStartingEvent): Unit = {
        RecipeManager.initCommands(event.getServer.getCommandManager.asInstanceOf[ServerCommandManager])
    }
}
