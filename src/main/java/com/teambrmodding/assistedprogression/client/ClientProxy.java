package com.teambrmodding.assistedprogression.client;

import com.teambr.nucleus.annotation.IRegisterable;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.client.models.ModelPipette;
import com.teambrmodding.assistedprogression.client.renderers.tiles.TileGrinderRenderer;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/12/17
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        // Pipette models
        ModelLoader.setCustomMeshDefinition(ItemManager.itemPipette, stack -> ModelPipette.LOCATION);
        ModelBakery.registerItemVariants(ItemManager.itemPipette, ModelPipette.LOCATION);
        ModelLoaderRegistry.registerLoader(ModelPipette.LoaderDynPipette.INSTANCE);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        // Item Models
        AssistedProgression.registrationData.getBlocks().forEach(block -> ((IRegisterable<?>)block).registerRender());
        AssistedProgression.registrationData.getItems().forEach(item   -> ((IRegisterable<?>)item).registerRender());

        // Grinder
        ClientRegistry.bindTileEntitySpecialRenderer(TileGrinder.class,
                new TileGrinderRenderer<>());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) { }
}
