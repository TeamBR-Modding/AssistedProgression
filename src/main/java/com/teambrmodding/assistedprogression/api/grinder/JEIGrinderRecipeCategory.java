package com.teambrmodding.assistedprogression.api.grinder;

import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.api.JEIAssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
public class JEIGrinderRecipeCategory implements IRecipeCategory<GrinderRecipe> {
    /**
     * Returns a unique ID for this recipe category.
     * Referenced from recipes to identify which recipe category they belong to.
     */
    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Reference.MOD_ID, "grinder");
    }

    @Override
    public Class<? extends GrinderRecipe> getRecipeClass() {
        return GrinderRecipe.class;
    }

    /**
     * Returns the localized name for this recipe type.
     * Drawn at the top of the recipe GUI pages for this category.
     */
    @Override
    public String getTitle() {
        return ClientUtils.translate("assistedprogression.grinder");
    }

    /**
     * Returns the drawable background for a single recipe in this category.
     * <p>
     * The size of the background determines how recipes are laid out by JEI,
     * make sure it is the right size to contains everything being displayed.
     */
    @Override
    public IDrawable getBackground() {
        return JEIAssistedProgression.jeiHelpers.getGuiHelper().createDrawable(new ResourceLocation(Reference.MOD_ID,
                "textures/gui/jei/grinder.png"), 0, 0, 169, 60);
    }

    /**
     * Icon for the category tab.
     * @return icon to draw on the category tab, max size is 16x16 pixels.
     */
    @Override
    public IDrawable getIcon() {
        return JEIAssistedProgression.jeiHelpers.getGuiHelper()
                .createDrawableIngredient(new ItemStack(BlockManager.grinder));
    }

    /**
     * Sets all the recipe's ingredients by filling out an instance of {@link IIngredients}.
     * This is used by JEI for lookups, to figure out what ingredients are inputs and outputs for a recipe.
     *
     * @param recipe
     * @param ingredients
     */
    @Override
    public void setIngredients(GrinderRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    /**
     * Set the {@link IRecipeLayout} properties from the recipe.
     *
     * @param recipeLayout the layout that needs its properties set.
     * @param recipe       the recipe, for extra information.
     * @param ingredients  the ingredients, already set earlier by {@link IRecipeCategory#setIngredients}
     */
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GrinderRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 76, 22);
        guiItemStacks.init(1, false, 58, 41);

        guiItemStacks.set(ingredients);
    }
}
