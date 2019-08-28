package com.teambrmodding.assistedprogression.client;

import com.teambrmodding.assistedprogression.client.render.GrinderTileRenderer;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.common.item.DustItem;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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
public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(GrinderTile.class, new GrinderTileRenderer<>());
        Minecraft.getInstance().getItemColors().register((IItemColor) ItemManager.iron_dust,
                (IItemProvider) () -> ItemManager.iron_dust);
        Minecraft.getInstance().getItemColors().register((IItemColor) ItemManager.gold_dust,
                (IItemProvider) () -> ItemManager.gold_dust);
    }
}
