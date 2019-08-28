package com.teambrmodding.assistedprogression.client;

import com.teambrmodding.assistedprogression.client.render.GrinderTileRenderer;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
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
    }
}
