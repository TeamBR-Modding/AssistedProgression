package com.teambrmodding.assistedprogression;

import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.events.RenderEvents;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.*;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
@Mod(modid          = Reference.MOD_ID,
        name           = Reference.MOD_NAME,
        version        = Reference.VERSION,
        dependencies   = Reference.DEPENDENCIES,
        updateJSON     = Reference.UPDATE_JSON)
public class AssistedProgression {

    @Mod.Instance
    public static AssistedProgression INSTANCE;

    public static String configFolderLocation;

    @SidedProxy(clientSide = "com.teambrmodding.assistedprogression.client.ClientProxy",
            serverSide = "com.teambrmodding.assistedprogression.common.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabAssistedProgression = new CreativeTabs("tabAssistedProgression") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(BlockManager.blockGrinder));
        }
    };

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        configFolderLocation = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "AssistedProgression";
        ConfigManager.preinit();
        EventManager.registerEvents();
        RecipeManager.preInit();
        proxy.preInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new ItemGuiManager());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        RecipeManager.init();
        if(event.getSide() == Side.CLIENT)
            MinecraftForge.EVENT_BUS.register(new RenderEvents());
        proxy.init();
    }

    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public static void serverLoad(FMLServerAboutToStartEvent event) {
        RecipeManager.initCommands((ServerCommandManager)event.getServer().getCommandManager());
    }
}
