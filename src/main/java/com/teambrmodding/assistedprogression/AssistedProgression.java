package com.teambrmodding.assistedprogression;

import com.teambrmodding.assistedprogression.client.ClientProxy;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.managers.ScreenHelper;
import com.teambrmodding.assistedprogression.network.PacketManager;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
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

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public AssistedProgression() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeHelper::registerRecipeSerializers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        PacketManager.initPackets();
    }

    protected void setup(final FMLCommonSetupEvent event) {
        proxy.init();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ScreenHelper::registerScreens);
        RecipeHelper.definePressurePlateValues();
    }

    protected void serverStarted(FMLServerStartedEvent event) {
        RecipeHelper.generateDynamicGrinderRecipes(event.getServer().getRecipeManager());
    }
}
