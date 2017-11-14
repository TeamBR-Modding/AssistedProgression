package com.teambrmodding.assistedprogression.client;

import com.teambrmodding.assistedprogression.client.models.ModelPipette;
import com.teambrmodding.assistedprogression.client.renderers.tiles.TileFlushableChestRenderer;
import com.teambrmodding.assistedprogression.client.renderers.tiles.TileGrinderRenderer;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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
    public void preInit() {
        ItemRenderManager.registerBlockRenderers();

        // Pipette models
        ModelLoader.setCustomMeshDefinition(ItemManager.itemPipette(), stack -> ModelPipette.LOCATION);
        ModelBakery.registerItemVariants(ItemManager.itemPipette(), ModelPipette.LOCATION);
        ModelLoaderRegistry.registerLoader(ModelPipette.LoaderDynPipette.INSTANCE);
    }

    @Override
    public void init() {
        ItemRenderManager.registerItemRenderers();

        // Flushable Chest
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes()
                .registerBuiltInBlocks(BlockManager.blockFlushableChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFlushableChest.class,
                new TileFlushableChestRenderer<>());

        // Grinder
        ClientRegistry.bindTileEntitySpecialRenderer(TileGrinder.class,
                new TileGrinderRenderer<>());
    }

    @Override
    public void postInit() { }
}
