package com.teambrmodding.assistedprogression.api.jei.grinder;

import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.api.jei.AssistedProgressionPlugin;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeManager;
import com.teambrmodding.assistedprogression.registries.GrinderRecipeHandler;
import com.teambrmodding.assistedprogression.registries.recipes.AbstractRecipe;
import com.teambrmodding.assistedprogression.registries.recipes.GrinderRecipe;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

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

    /*******************************************************************************************************************
     * IRecipeCategory                                                                                                 *
     *******************************************************************************************************************/

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
        return ClientUtils.translate("tile.assistedprogression:block_grinder.name");
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
        return AssistedProgressionPlugin.jeiHelpers.getGuiHelper().createDrawable(new ResourceLocation(Reference.MOD_ID,
                "textures/gui/jei/grinder.png"), 0, 0, 169, 60);
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
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();

        itemStackGroup.init(0, true,  76, 22);
        itemStackGroup.init(1, false, 58, 41);

        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));

        // If contains optional second output
        if(ingredients.getOutputs(ItemStack.class).size() > 1)
            recipeLayout.getItemStacks().set(2, ingredients.getOutputs(ItemStack.class).get(1));
    }

    /*******************************************************************************************************************
     * Class Methods                                                                                                   *
     *******************************************************************************************************************/

    public static List<JEIGrinderRecipeWrapper> buildRecipeList() {
        ArrayList<JEIGrinderRecipeWrapper> recipes = new ArrayList<>();
        GrinderRecipeHandler grinderRecipeHandler = RecipeManager.getHandler(RecipeManager.RecipeType.GRINDER);
        for(GrinderRecipe recipe : grinderRecipeHandler.recipes) {
            ItemStack input = AbstractRecipe.getItemStackFromStringForDisplay(recipe.inputItemStack);
            ItemStack output = AbstractRecipe.getItemStackFromString(recipe.outputItemStack);
            recipes.add(new JEIGrinderRecipeWrapper(input, output));
        }
        return recipes;
    }
}
