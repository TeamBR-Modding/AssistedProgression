package com.teambrmodding.assistedprogression.api.jei.grinder;

import com.teambr.bookshelf.api.jei.drawables.SlotDrawable;
import com.teambrmodding.assistedprogression.api.jei.AssistedProgressionPlugin;
import com.teambrmodding.assistedprogression.lib.Reference;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

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
public class JEIGrinderRecipeCategory implements IRecipeCategory<IRecipeWrapper> {

    ResourceLocation location = new ResourceLocation(Reference.MOD_ID(), "textures/gui/jei/jei.png");
    SlotDrawable inputSlot = new SlotDrawable(80, 0, false);
    SlotDrawable outputSlot1 = new SlotDrawable(62, 22, false);
    SlotDrawable outputSlot2 = new SlotDrawable(80, 22, false);
    SlotDrawable outputSlot3 = new SlotDrawable(98, 22, false);

    @Override
    public String getUid() {
        return "assistedprogression.grinder";
    }

    @Override
    public String getTitle() {
        return I18n.translateToLocal("tile.assistedprogression:blockGrinder.name");
    }

    @Override
    public IDrawable getBackground() {
        return AssistedProgressionPlugin.jeiHelpers.getGuiHelper().createDrawable(location, 0, 0, 170, 60);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        inputSlot.draw(minecraft);
        outputSlot1.draw(minecraft);
        outputSlot2.draw(minecraft);
        outputSlot3.draw(minecraft);
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {}

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 80, 0);
        stacks.init(1, false, 62, 22);

        recipeLayout.getItemStacks().set(0, ((JEIGrinderRecipe)recipeWrapper).getInputs());
        recipeLayout.getItemStacks().set(1, ((JEIGrinderRecipe)recipeWrapper).getOutputs());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 80, 0);
        stacks.init(1, false, 62, 22);

        recipeLayout.getItemStacks().set(0, ((JEIGrinderRecipe)recipeWrapper).getInputs());
        recipeLayout.getItemStacks().set(1, ((JEIGrinderRecipe)recipeWrapper).getOutputs());
    }
}
