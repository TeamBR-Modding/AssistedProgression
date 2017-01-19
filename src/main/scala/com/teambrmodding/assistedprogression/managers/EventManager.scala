package com.teambrmodding.assistedprogression.managers

import com.teambrmodding.assistedprogression.common.items.ItemTrashBag
import com.teambrmodding.assistedprogression.events.RenderEvents
import net.minecraftforge.common.MinecraftForge

/**
  * This file was created for Assisted-Progression
  *
  * Assisted-Progression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis - pauljoda
  * @since 1/18/2017
  */
object EventManager {

    /**
      * Adds all events we use to the event handler system
      */
    def registerEvents(): Unit = {
        MinecraftForge.EVENT_BUS.register(new RenderEvents)
        MinecraftForge.EVENT_BUS.register(ItemTrashBag)
    }
}
