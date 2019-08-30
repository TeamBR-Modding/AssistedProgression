package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.common.tile.CrafterTile;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.common.tile.RedstoneClockTile;
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

    @ObjectHolder("crafter")
    public static TileEntityType<CrafterTile> crafter;

    @ObjectHolder("redstone_clock")
    public static TileEntityType<RedstoneClockTile> redstone_clock;

    @SubscribeEvent
    public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry()
                .register(TileEntityType.Builder.create(GrinderTile::new, BlockManager.grinder)
                        .build(null).setRegistryName("grinder"));
        event.getRegistry()
                .register(TileEntityType.Builder.create(CrafterTile::new, BlockManager.crafter)
                .build(null).setRegistryName("crafter"));
        event.getRegistry()
                .register(TileEntityType.Builder.create(RedstoneClockTile::new, BlockManager.redstone_clock)
                .build(null).setRegistryName("redstone_clock"));
    }
}
