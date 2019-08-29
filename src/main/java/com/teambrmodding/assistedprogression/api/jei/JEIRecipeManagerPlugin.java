package com.teambrmodding.assistedprogression.api.jei;

import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/28/2019
 */
public class JEIRecipeManagerPlugin implements IRecipeManagerPlugin {
    /**
     * Returns a list of Recipe Categories offered for the focus.
     *
     * @param focus
     */
    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> focus) {
        return Arrays.asList(new ResourceLocation(Reference.MOD_ID, "grinder"));
    }

    /**
     * Returns a list of Recipes in the recipeCategory that have the focus.
     *
     * @param recipeCategory
     * @param focus
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        if(recipeCategory == JEIAssistedProgression.grinderCategory)
            return (List<T>) RecipeHelper.grinderRecipes;
        return new ArrayList<>();
    }

    /**
     * Returns a list of all Recipes in the recipeCategory.
     *
     * @param recipeCategory
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> recipeCategory) {
        if(recipeCategory == JEIAssistedProgression.grinderCategory)
            return (List<T>) RecipeHelper.grinderRecipes;
        return new ArrayList<>();
    }
}
