package com.teambrmodding.assistedprogression.managers;

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

    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {

        // Grinder
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(GRINDER_RECIPE_TYPE.toString()), GRINDER_RECIPE_TYPE);
        event.getRegistry().register(GrinderRecipe.SERIALIZER);
    }

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

    /**
     * Checks if the given stack is valid in a grinder
     * @param stack The stack
     * @param manager The manager, world.getRecipeManager()
     * @return True if valid input
     */
    public static boolean isValidGrinderInput(ItemStack stack, RecipeManager manager) {
        for(IRecipe<?> recipe : getRecipes(GRINDER_RECIPE_TYPE, manager).values()) {
            if(recipe instanceof GrinderRecipe) {
                final GrinderRecipe localRecipe = (GrinderRecipe) recipe;
                if(localRecipe.isValid(stack))
                    return true;
            }
        }
        return false;
    }

    public static ItemStack getGrinderOutput(ItemStack stack, RecipeManager manager) {
        for(IRecipe<?> recipe : getRecipes(GRINDER_RECIPE_TYPE, manager).values()) {
            if(recipe instanceof GrinderRecipe) {
                final GrinderRecipe localRecipe = (GrinderRecipe) recipe;
                if(localRecipe.isValid(stack))
                    return localRecipe.getOutputOrEmpty(stack);
            }
        }
        return ItemStack.EMPTY;
    }
}
