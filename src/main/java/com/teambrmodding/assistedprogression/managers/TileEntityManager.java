package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.common.block.GrinderBlock;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.tileentity.TileEntityType;
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
public class TileEntityManager {

    @ObjectHolder("grinder")
    public static TileEntityType<GrinderTile> grinder;

    @SubscribeEvent
    public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry()
                .register(TileEntityType.Builder.create(GrinderTile::new, BlockManager.grinder)
                        .build(null).setRegistryName("grinder"));
    }
}
