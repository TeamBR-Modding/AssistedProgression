package com.teambrmodding.assistedprogression.managers;

import com.teambr.nucleus.helper.RegistrationHelper;
import com.teambrmodding.assistedprogression.AssistedProgression;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/19/17
 */
public class RegistrationManager {

    /**
     * Adds the items to the registry, including block ItemBlocks and ore dict tag
     * @param event Registation event
     */
    @SubscribeEvent
    public void addItems(RegistryEvent.Register<Item> event) {
        RegistrationHelper.addItems(event, AssistedProgression.registrationData);
    }

    /**
     * Registers blocks, their tile, and ore dict tag if possible
     * @param event Registration event
     */
    @SubscribeEvent
    public void addBlocks(RegistryEvent.Register<Block> event) {
        RegistrationHelper.addBlocks(event, AssistedProgression.registrationData);
    }
}
