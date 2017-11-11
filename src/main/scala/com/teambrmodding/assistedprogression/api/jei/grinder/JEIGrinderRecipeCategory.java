package com.teambrmodding.assistedprogression.api.jei.grinder;

import com.teambr.bookshelf.util.ClientUtils;
import com.teambrmodding.assistedprogression.api.jei.AssistedProgressionPlugin;
import com.teambrmodding.assistedprogression.lib.Reference;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

/**
 * This file was created for Assisted-Progression
 * <p>
 * Assisted-Progression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 1/18/2017
 */
public class JEIGrinderRecipeCategory implements IRecipeCategory<JEIGrinderRecipeWrapper> {

    /**
     * Returns a unique ID for this recipe category.
     * Referenced from recipes to identify which recipe category they belong to.
     *
     * @see VanillaRecipeCategoryUid for vanilla examples
     */
    @Override
    public String getUid() {
        return AssistedProgressionPlugin.GRINDER_UUID;
    }

    /**
     * Returns the localized name for this recipe type.
     * Drawn at the top of the recipe GUI pages for this category.
     */
    @Override
    public String getTitle() {
        return ClientUtils.translate("tile.assistedprogression:blockGrinder.name");
    }

    /**
     * Return the mod name or id associated with this recipe category.
     * Used for the recipe category tab's tooltip.
     *
     * @since JEI 4.5.0
     */
    @Override
    public String getModName() {
        return "Assisted Progression";
    }

    /**
     * Returns the drawable background for a single recipe in this category.
     * <p>
     * The size of the background determines how recipes are laid out by JEI,
     * make sure it is the right size to contains everything being displayed.
     */
    @Override
    public IDrawable getBackground() {
        return AssistedProgressionPlugin.jeiHelpers.getGuiHelper().createDrawable(new ResourceLocation(Reference.MOD_ID(), "textures/gui/jei/grinder.png"), 0, 0, 170, 80);
    }

    /**
     * Set the {@link IRecipeLayout} properties from the {@link IRecipeWrapper} and {@link IIngredients}.
     *
     * @param recipeLayout  the layout that needs its properties set.
     * @param recipeWrapper the recipeWrapper, for extra information.
     * @param ingredients   the ingredients, already set by the recipeWrapper
     * @since JEI 3.11.0
     */
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, JEIGrinderRecipeWrapper recipeWrapper, IIngredients ingredients) {
        
    }
}
