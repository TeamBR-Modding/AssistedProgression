package com.teambrmodding.assistedprogression;

import com.teambr.nucleus.data.RegistrationData;
import com.teambr.nucleus.helper.RegistrationHelper;
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
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
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

    public static RegistrationData registrationData = new RegistrationData(Reference.MOD_ID);

    @Mod.Instance
    public static AssistedProgression INSTANCE;

    public static String configFolderLocation;

    @SidedProxy(clientSide = "com.teambrmodding.assistedprogression.client.ClientProxy",
            serverSide = "com.teambrmodding.assistedprogression.common.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabAssistedProgression = new CreativeTabs("assistedprogression:main") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(BlockManager.blockGrinder));
        }
    };

    public static CreativeTabs tabPipette = new CreativeTabs("assistedprogression:pipette") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemManager.itemPipette);
        }
    };

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        configFolderLocation = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "AssistedProgression";
        RegistrationHelper.fillRegistrationData(event, registrationData);
        ConfigManager.preinit();
        EventManager.registerEvents();
        RecipeManager.preInit();
        proxy.preInit(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new ItemGuiManager());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        RecipeManager.init();
        if(event.getSide() == Side.CLIENT)
            MinecraftForge.EVENT_BUS.register(new RenderEvents());
        proxy.init(event);
    }

    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public static void serverLoad(FMLServerAboutToStartEvent event) {
        RecipeManager.initCommands((ServerCommandManager)event.getServer().getCommandManager());
    }
}
