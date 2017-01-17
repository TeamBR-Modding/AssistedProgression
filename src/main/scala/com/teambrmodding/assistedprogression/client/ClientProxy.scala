package com.teambrmodding.assistedprogression.client

import com.teambrmodding.assistedprogression.client.renderers.tiles.TileFlushableChestRenderer
import com.teambrmodding.assistedprogression.common.CommonProxy
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest
import com.teambrmodding.assistedprogression.managers.BlockManager
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.registry.ClientRegistry

/**
  * This file was created for AssistedProgression
  *
  * AssistedProgression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 1/16/2017
  */
class ClientProxy extends CommonProxy {

    override def preInit(): Unit = {
        ItemRenderManager.preInit()
    }

    override def init(): Unit = {
        ItemRenderManager.registerItemRenderers()

        Minecraft.getMinecraft.getRenderItem.getItemModelMesher.getModelManager.getBlockModelShapes.registerBuiltInBlocks(BlockManager.blockFlushableChest)
        ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileFlushableChest], new TileFlushableChestRenderer[TileFlushableChest])
    }

    override def postInit(): Unit = { }
}
