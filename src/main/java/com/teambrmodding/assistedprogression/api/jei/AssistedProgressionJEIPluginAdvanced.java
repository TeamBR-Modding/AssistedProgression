package com.teambrmodding.assistedprogression.api.jei;

import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
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
public class AssistedProgressionJEIPluginAdvanced implements IRecipeManagerPlugin {
    /**
     * Returns a list of Recipe Categories offered for the focus.
     *
     * @param focus
     */
    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> focus) {
        if(focus != null && focus.getValue() instanceof ItemStack) {
            ItemStack filterStack = (ItemStack) focus.getValue();
            switch (focus.getMode()) {
                case INPUT:
                    for(GrinderRecipe recipe : RecipeHelper.grinderRecipes)
                        if(recipe.isValid(filterStack))
                            return Arrays.asList(new ResourceLocation(Reference.MOD_ID, "grinder"));
                    break;
                case OUTPUT:
                    for(GrinderRecipe recipe : RecipeHelper.grinderRecipes)
                        if(recipe.getRecipeOutput().getItem() == filterStack.getItem())
                            return Arrays.asList(new ResourceLocation(Reference.MOD_ID, "grinder"));
                    break;
                default:
            }
        }
        return new ArrayList<>();
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
        if(recipeCategory == AssistedProgressionJEIPlugin.grinderCategory) {
            if(focus == null)
                return (List<T>) RecipeHelper.grinderRecipes;
            else if(focus.getValue() instanceof ItemStack){
                switch (focus.getMode()) {
                    case INPUT:
                        return (List<T>) Arrays.asList(RecipeHelper.grinderRecipes.stream()
                                .filter(recipe -> recipe.input.test((ItemStack)focus.getValue())).toArray());
                    case OUTPUT:
                        return (List<T>) Arrays.asList(RecipeHelper.grinderRecipes.stream()
                                .filter(recipe -> recipe.getRecipeOutput().getItem() == ((ItemStack)focus.getValue()).getItem()).toArray());
                    default:
                }
            }
        }
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
        if(recipeCategory == AssistedProgressionJEIPlugin.grinderCategory)
            return (List<T>) RecipeHelper.grinderRecipes;
        return new ArrayList<>();
    }
}
