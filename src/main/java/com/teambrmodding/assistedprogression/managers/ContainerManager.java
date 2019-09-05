package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.common.container.CrafterContainer;
import com.teambrmodding.assistedprogression.common.container.GrinderContainer;
import com.teambrmodding.assistedprogression.common.container.TrashBagContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/24/2019
 */
@ObjectHolder(Reference.MOD_ID)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerManager {

    @ObjectHolder("grinder")
    public static ContainerType<GrinderContainer> grinder;

    @ObjectHolder("crafter")
    public static ContainerType<CrafterContainer> crafter;

    @ObjectHolder("trash_bag")
    public static ContainerType<TrashBagContainer> trash_bag;

    @SubscribeEvent
    public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create(GrinderContainer::new).setRegistryName("grinder"));
        event.getRegistry().register(IForgeContainerType.create(CrafterContainer::new).setRegistryName("crafter"));
        event.getRegistry().register(IForgeContainerType.create(TrashBagContainer::new).setRegistryName("trash_bag"));
    }
}
