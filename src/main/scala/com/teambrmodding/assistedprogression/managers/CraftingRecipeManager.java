package com.teambrmodding.assistedprogression.managers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This file was created for Assisted-Progression
 * <p>
 * Assisted-Progression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 1/16/2017
 */
public class CraftingRecipeManager {

    public static void preInit() {
        // Crafter
        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.blockCrafter()),
                new ItemStack(Blocks.CRAFTING_TABLE), new ItemStack(Blocks.CHEST), new ItemStack(Blocks.CRAFTING_TABLE));

        // Player Plate
        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.playerPlate()),
                new ItemStack(Items.BRICK), new ItemStack(Items.BRICK));

        // Redstone Clock
        GameRegistry.addRecipe(new ItemStack(BlockManager.redstoneClock()),
                "SRS",
                "SBS",
                "SRS", 'S', Blocks.STONE, 'R', Items.REDSTONE, 'B', Blocks.REDSTONE_BLOCK);

        // Flushable Chest
        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.blockFlushableChest()),
                Blocks.CHEST, Items.FLINT_AND_STEEL);
    }
}
