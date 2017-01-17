package com.teambrmodding.assistedprogression.client

import com.teambrmodding.assistedprogression.common.CommonProxy

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
    }

    override def postInit(): Unit = { }
}
