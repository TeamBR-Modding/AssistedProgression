package com.teambrmodding.assistedprogression.managers;

import com.teambr.nucleus.helper.LogHelper;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import com.teambrmodding.assistedprogression.recipe.RecipeTypeGrinderRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
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
}
