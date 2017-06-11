package com.teambrmodding.assistedprogression;

import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.events.RenderEvents;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.*;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 6/2/2017
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class AssistedProgression {

    //The logger. For logging
    public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME);

    public static String configFolderLocation;

    @SidedProxy(clientSide = "com.teambrmodding.assistedprogression.client.ClientProxy",
            serverSide = "com.teambrmodding.assistedprogression.common.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabAssistedProgression = new CreativeTabs("tabAssistedProgression") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(BlockManager.blockGrinder());
        }
    };

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        configFolderLocation = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "AssistedProgression";
        ConfigManager.preInit();
        BlockManager.preInit();
        ItemManager.preInit();
        RecipeManager.preInit();
        proxy.preInit();
        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new ItemGuiManager);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        RecipeManager.init();
        EventManager.registerEvents();
        if(event.getSide() == Side.CLIENT)
            MinecraftForge.EVENT_BUS.register(new RenderEvents());
        proxy.init();
    }

    @Mod.EventHandler
    public static void posInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public static void serverLoad(FMLServerStartingEvent event) {
        RecipeManager.initCommands((ServerCommandManager) event.getServer().getCommandManager());
    }
}
