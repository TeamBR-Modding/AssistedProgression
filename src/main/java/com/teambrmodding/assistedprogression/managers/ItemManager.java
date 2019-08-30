package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.common.item.DustItem;
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
import com.teambrmodding.assistedprogression.common.item.PipetteItem;

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

    // Create tab
    public static ItemGroup itemGroupAssistedProgression;

    /*******************************************************************************************************************
     * Items                                                                                                           *
     *******************************************************************************************************************/

    @ObjectHolder("pipette")
    public static Item pipette;

    @ObjectHolder("iron_dust")
    public static Item iron_dust;

    @ObjectHolder("gold_dust")
    public static Item gold_dust;

    /*******************************************************************************************************************
     * BlockItems                                                                                                      *
     *******************************************************************************************************************/

    @ObjectHolder("player_plate")
    public static Item player_plate;

    @ObjectHolder("grinder")
    public static Item grinder;

    @ObjectHolder("crafter")
    public static Item crafter;

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

        // Register Items
        event.getRegistry().register(new PipetteItem());
        event.getRegistry().register(new DustItem("iron_dust", 0xFFd8d8d8));
        event.getRegistry().register(new DustItem("gold_dust", 0xFFdede00));

        // Register BlockItems
        registerBlockItemForBlock(event.getRegistry(), BlockManager.player_plate);
        registerBlockItemForBlock(event.getRegistry(), BlockManager.grinder);
        registerBlockItemForBlock(event.getRegistry(), BlockManager.crafter);
    }

    @SuppressWarnings("ConstantConditions")
    public static void registerBlockItemForBlock(IForgeRegistry<Item> registry, Block block) {
        Item itemBlock = new BlockItem(block, new Item.Properties().group(itemGroupAssistedProgression));
        itemBlock.setRegistryName(block.getRegistryName());
        registry.register(itemBlock);
    }
}
