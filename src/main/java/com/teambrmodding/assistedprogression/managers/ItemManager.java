package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.common.item.*;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.sql.Ref;

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
public class ItemManager {

    // Create tabs
    // Main Tab
    public static ItemGroup itemGroupAssistedProgression;

    // Pipette Tag
    public static ItemGroup itemGroupAssistedProgressionPipettes;

    /*******************************************************************************************************************
     * Items                                                                                                           *
     *******************************************************************************************************************/

    @ObjectHolder("pipette")
    public static Item pipette;

    @ObjectHolder("iron_dust")
    public static Item iron_dust;

    @ObjectHolder("gold_dust")
    public static Item gold_dust;

    @ObjectHolder("magnet")
    public static Item magnet;

    @ObjectHolder("spawner_relocator")
    public static Item spawner_relocator;

    @ObjectHolder("hefty_bag")
    public static Item hefty_bag;

    @ObjectHolder("trash_bag")
    public static Item trash_bag;

    /*******************************************************************************************************************
     * BlockItems                                                                                                      *
     *******************************************************************************************************************/

    @ObjectHolder("player_plate")
    public static Item player_plate;

    @ObjectHolder("grinder")
    public static Item grinder;

    @ObjectHolder("crafter")
    public static Item crafter;

    @ObjectHolder("redstone_clock")
    public static Item redstone_clock;

    /*******************************************************************************************************************
     * Register                                                                                                        *
     *******************************************************************************************************************/

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // Setup ItemGroup
        itemGroupAssistedProgression = new ItemGroup(Reference.MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(BlockManager.player_plate);
            }
        };

        itemGroupAssistedProgressionPipettes = new ItemGroup(Reference.MOD_ID + ".pipettes") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ItemManager.pipette);
            }
        };

        // Register Items
        event.getRegistry().register(new PipetteItem());
        event.getRegistry().register(new DustItem("iron_dust", 0xFFd8d8d8));
        event.getRegistry().register(new DustItem("gold_dust", 0xFFdede00));
        event.getRegistry().register(new MagnetItem("magnet"));
        event.getRegistry().register(new SpawnerRelocatorItem("spawner_relocator"));
        event.getRegistry().register(new TrashBagItem("trash_bag", 1));
        event.getRegistry().register(new TrashBagItem("hefty_bag", 18));

        // Register BlockItems
        registerBlockItemForBlock(event.getRegistry(), BlockManager.player_plate);
        registerBlockItemForBlock(event.getRegistry(), BlockManager.grinder);
        registerBlockItemForBlock(event.getRegistry(), BlockManager.crafter);
        registerBlockItemForBlock(event.getRegistry(), BlockManager.redstone_clock);
    }

    @SuppressWarnings("ConstantConditions")
    public static void registerBlockItemForBlock(IForgeRegistry<Item> registry, Block block) {
        Item itemBlock = new BlockItem(block, new Item.Properties().group(itemGroupAssistedProgression));
        itemBlock.setRegistryName(block.getRegistryName());
        registry.register(itemBlock);
    }
}
