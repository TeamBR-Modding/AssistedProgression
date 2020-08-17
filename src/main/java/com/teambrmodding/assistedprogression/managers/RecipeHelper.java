package com.teambrmodding.assistedprogression.managers;

import com.teambr.nucleus.helper.LogHelper;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import com.teambrmodding.assistedprogression.recipe.RecipeTypeGrinderRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/27/2019
 */
public class RecipeHelper {

    public static final IRecipeType<GrinderRecipe> GRINDER_RECIPE_TYPE = new RecipeTypeGrinderRecipe();

    /**
     * Register built-in loaders for recipes, this will add in default recipes
     * @param event Register event
     */
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        // Grinder
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(GRINDER_RECIPE_TYPE.toString()), GRINDER_RECIPE_TYPE);
        event.getRegistry().register(GrinderRecipe.SERIALIZER);
    }

    /*******************************************************************************************************************
     * Helpers                                                                                                         *
     *******************************************************************************************************************/

    /**
     * This method lets you get all of the recipe data for a given recipe type. The existing
     * methods for this require an IInventory, and this allows you to skip that overhead. This
     * method uses reflection to get the recipes map, but an access transformer would also
     * work.
     *
     * Credit to Darkhax
     * @author https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes/blob/master/src/main/java/net/darkhax/customrecipeexample/CustomRecipesMod.java
     *
     * @param recipeType The type of recipe to grab.
     * @param manager The recipe manager. This is generally taken from a World.
     * @return A map containing all recipes for the passed recipe type. This map is immutable
     *         and can not be modified.
     */
    public static Map<ResourceLocation, IRecipe<?>> getRecipes(IRecipeType<?> recipeType, RecipeManager manager) {
        final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap
                = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, manager, "field_199522_d");
        return recipesMap.get(recipeType);
    }

    /*******************************************************************************************************************
     * Grinder                                                                                                         *
     *******************************************************************************************************************/

    /**
     * Minecraft uses an ImmutableMap for recipes, so pretty much we can load, but not add. To extend function to dynamic
     * recipes, we are going to wrap around it and add our own. Use this list for all references to recipes
     */
    public static List<GrinderRecipe> grinderRecipes = new ArrayList<>();

    /**
     * List of valid pressure plates that can be used to progress grinder, returns their multiplier
     *
     * Wood   - 1.0
     * Stone  - 1.25
     * Player - 1.5
     * Iron   - 1.75
     * Gold   - 2.0
     *
     */
    public static Map<Block, Double> pressurePlates = new HashMap<>();

    /**
     * Add dynamic recipes to the grinder registry, used for ores
     * @param recipeManager The recipe manager
     */
    @SuppressWarnings({"ConstantConditions"})
    public static void generateDynamicGrinderRecipes(RecipeManager recipeManager) {
        // Add grinder recipes to our list
        grinderRecipes.clear();
        getRecipes(GRINDER_RECIPE_TYPE, recipeManager)
                .values().forEach((recipe) -> {
            if(recipe instanceof GrinderRecipe) {
                GrinderRecipe localRecipe = (GrinderRecipe) recipe;
                grinderRecipes.add(localRecipe);
            }
        });

        // Add custom recipes
        ResourceLocation dustLocation   = new ResourceLocation("forge", "dusts");
        ResourceLocation ingotLocation  = new ResourceLocation("forge", "ingots");
        ResourceLocation oreLocation    = new ResourceLocation("forge", "ores");

        /***************************************************************************************************************
         * Dust from Ore                                                                                               *
         ***************************************************************************************************************/
        ITag<Item> oreTag  = ItemTags.getCollection().get(oreLocation);

        // Get all items in the ore forge tag
        for(Item oreItem : oreTag.getAllElements()) {
            // Don't want to add something already made by built in
            if (!isValidGrinderInput(new ItemStack(oreItem), null)) {
                // Iterate all tags for item in forge ore tag
                for (ResourceLocation tag : oreItem.getTags()) {
                    if (tag.toString().contains("ores")) {
                        // Attempt lookup for dust, structure should be matching in most cases so this should find proper dust
                        ResourceLocation testDustLocation
                                = new ResourceLocation(tag.toString().replace("ores", "dusts"));

                        // We don't want to re-add the whole dust tag
                        if (!testDustLocation.equals(dustLocation)
                                && ItemTags.getCollection().get(testDustLocation) != null
                                && ItemTags.getCollection().get(tag) != null) {
                            ItemStack output = Ingredient.fromTag(ItemTags.getCollection().get(testDustLocation)).getMatchingStacks()[0].copy();
                            output.setCount(2);
                            addGrinderRecipe(null,
                                    ItemTags.getCollection().get(tag), output);
                        }
                    }
                }
            }
        }

        /***************************************************************************************************************
         * Dust from Ingot                                                                                             *
         ***************************************************************************************************************/
        ITag<Item> ingotTag  = ItemTags.getCollection().get(ingotLocation);

        // Get all items in the ore forge tag
        for(Item ingotItem : ingotTag.getAllElements()) {
            // Don't want to add something already made by built in
            if (!isValidGrinderInput(new ItemStack(ingotItem), null)) {
                // Iterate all tags for item in forge ingot tag
                for (ResourceLocation tag : ingotItem.getTags()) {
                    if (tag.toString().contains("ingots")) {
                        // Attempt lookup for dust, structure should be matching in most cases so this should find proper dust
                        ResourceLocation testDustLocation
                                = new ResourceLocation(tag.toString().replace("ingots", "dusts"));

                        // We don't want to re-add the whole dust tag
                        if (!testDustLocation.equals(dustLocation)
                                && ItemTags.getCollection().get(testDustLocation) != null
                                && ItemTags.getCollection().get(tag) != null) {
                            ItemStack output = Ingredient.fromTag(ItemTags.getCollection().get(testDustLocation)).getMatchingStacks()[0].copy();
                            addGrinderRecipe(null,
                                    ItemTags.getCollection().get(tag), output);
                        }
                    }
                }
            }
        }
    }

    /**
     * Define the values for pressure plate progress in grinder
     */
    public static void definePressurePlateValues() {
        pressurePlates.clear();

        // Wooden
        pressurePlates.put(Blocks.ACACIA_PRESSURE_PLATE, 1.0);
        pressurePlates.put(Blocks.BIRCH_PRESSURE_PLATE, 1.0);
        pressurePlates.put(Blocks.DARK_OAK_PRESSURE_PLATE, 1.0);
        pressurePlates.put(Blocks.OAK_PRESSURE_PLATE, 1.0);
        pressurePlates.put(Blocks.SPRUCE_PRESSURE_PLATE, 1.0);

        // Stone
        pressurePlates.put(Blocks.STONE_PRESSURE_PLATE, 1.25);

        // Player
        pressurePlates.put(BlockManager.player_plate, 1.5);

        // Heavy
        pressurePlates.put(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 1.75);

        // Light
        pressurePlates.put(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 2.0);
    }

    /**
     * Checks if the given stack is valid in a grinder
     * @param stack The stack
     * @param manager The manager, world.getRecipeManager()
     * @return True if valid input
     */
    public static boolean isValidGrinderInput(ItemStack stack, @Nullable RecipeManager manager) {
        if(!grinderRecipes.isEmpty()) {
            for(GrinderRecipe recipe : grinderRecipes) {
                if(recipe.isValid(stack))
                    return true;
            }
        } else {
            // If we are empty, we need to use built in recipes
            LogHelper.logger.error("Extended recipes not found, using loaded recipes. If you see this message, please report issue");
            for (IRecipe<?> recipe : getRecipes(GRINDER_RECIPE_TYPE, manager).values()) {
                if (recipe instanceof GrinderRecipe) {
                    final GrinderRecipe localRecipe = (GrinderRecipe) recipe;
                    if (localRecipe.isValid(stack))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the output for the given input
     * @param stack Input stack
     * @param manager Recipe manager
     * @return ItemStack result
     */
    public static ItemStack getGrinderOutput(ItemStack stack, @Nullable RecipeManager manager) {
        if(!grinderRecipes.isEmpty()) {
            for(GrinderRecipe recipe : grinderRecipes) {
                if(recipe.isValid(stack))
                    return recipe.getOutputOrEmpty(stack);
            }
        } else {
            // If we are empty, we need to use built in recipes
            LogHelper.logger.error("Extended recipes not found, using loaded recipes. If you see this message, please report issue");
            for (IRecipe<?> recipe : getRecipes(GRINDER_RECIPE_TYPE, manager).values()) {
                if (recipe instanceof GrinderRecipe) {
                    final GrinderRecipe localRecipe = (GrinderRecipe) recipe;
                    if (localRecipe.isValid(stack))
                        return localRecipe.getOutputOrEmpty(stack);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Adds a grinder recipe with a stack input and output
     * @param label The label for this recipe, null to generate label
     * @param input Input ItemStack
     * @param output Output ItemStack
     */
    public static void addGrinderRecipe(@Nullable String label, ItemStack input, ItemStack output) {
        grinderRecipes.add(new GrinderRecipe(
                new ResourceLocation(Reference.MOD_ID,
                        label == null ? "grinder_recipe_auto_" + grinderRecipes.size() : label),
                Ingredient.fromStacks(input), output));
    }

    /**
     * Adds a grinder recipe with a tag and stack output
     * @param label The label for this recipe, null to generate label
     * @param input Input Tag
     * @param output Output ItemStack
     */
    public static void addGrinderRecipe(@Nullable String label, ITag<Item> input, ItemStack output) {
        grinderRecipes.add(new GrinderRecipe(
                new ResourceLocation(Reference.MOD_ID,
                        label == null ? "grinder_recipe_auto_" + grinderRecipes.size() : label),
                Ingredient.fromTag(input), output));
    }
}
