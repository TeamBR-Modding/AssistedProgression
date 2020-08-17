package com.teambrmodding.assistedprogression;

import com.teambrmodding.assistedprogression.client.ClientProxy;
import com.teambrmodding.assistedprogression.common.commands.GetEnchantmentList;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.common.item.TrashBagItem;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.ConfigManager;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.managers.ScreenHelper;
import com.teambrmodding.assistedprogression.network.PacketManager;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
@Mod(Reference.MOD_ID)
public class AssistedProgression {

    // Proxy to wrap each side, prevents sided compile issues, use this for sidedness
    private static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    /**
     * Create the mod
     */
    public AssistedProgression() {
        attachEvents();

        PacketManager.initPackets();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigManager.SPEC);
    }

    /**
     * Attach events to the buses before loading
     */
    private void attachEvents() {
        // Loading steps
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);

        // Event attaches
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeHelper::registerRecipeSerializers);
        MinecraftForge.EVENT_BUS.addListener(TrashBagItem::onItemPickup);
    }

    /**
     * Common setup, called after all registering
     * @param event Setup event
     */
    private void setup(final FMLCommonSetupEvent event) {
        proxy.init();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ScreenHelper::registerScreens);
        RecipeHelper.definePressurePlateValues();
    }

    /**
     * Called when the server is starting, do dedicated server registering here
     */
    private void serverStarting(FMLServerStartingEvent event) {
       GetEnchantmentList.register(event.getServer().getCommandManager().getDispatcher());
    }

    /**
     * Called when a server begins loading, after all registering
     */
    private void serverStarted(FMLServerStartedEvent event) {
        RecipeHelper.generateDynamicGrinderRecipes(event.getServer().getRecipeManager());
    }
}
