package com.teambrmodding.assistedprogression.client;

import com.teambrmodding.assistedprogression.client.model.ModelPipette;
import com.teambrmodding.assistedprogression.client.render.GrinderTileRenderer;
import com.teambrmodding.assistedprogression.client.tooltip.EnchantmentToolTip;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.common.item.DustItem;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/27/2019
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(GrinderTile.class, new GrinderTileRenderer<>());
        Minecraft.getInstance().getItemColors().register((color, ref) -> ((DustItem)ItemManager.iron_dust).getColor(),
                (IItemProvider) () -> ItemManager.iron_dust);
        Minecraft.getInstance().getItemColors().register((color, ref) -> ((DustItem)ItemManager.gold_dust).getColor(),
                (IItemProvider) () -> ItemManager.gold_dust);

        MinecraftForge.EVENT_BUS.register(new EnchantmentToolTip());
    }

    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre event) {
        event.addSprite(ModelPipette.maskLocation);
    }

    @SubscribeEvent
    public static void modelBake(ModelBakeEvent event) {
        IBakedModel baseModel = event.getModelRegistry().get(ModelPipette.LOCATION);
        event.getModelRegistry().put(ModelPipette.LOCATION,
                new ModelPipette.PipetteDynamicModel(event.getModelLoader(), baseModel));
    }
}
