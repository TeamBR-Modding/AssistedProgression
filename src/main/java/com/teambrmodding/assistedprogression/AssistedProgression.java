package com.teambrmodding.assistedprogression;

import com.teambrmodding.assistedprogression.client.render.GrinderTileRenderer;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.managers.ScreenHelper;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

    public AssistedProgression() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeHelper::registerRecipeSerializers);
    }

    private void setupClient(FMLClientSetupEvent event) {
        ScreenHelper.registerScreens();
        ClientRegistry.bindTileEntitySpecialRenderer(GrinderTile.class, new GrinderTileRenderer<>());
    }
}
