package com.teambrmodding.assistedprogression.api.jei.grinder;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.api.jei.AssistedProgressionJEIPlugin;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
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
public class GrinderJEIRecipeCategory implements IRecipeCategory<GrinderRecipe> {

    private ITickTimer timer;
    private ItemStack grinderStack;
    private List<ItemStack> plates;

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
        return AssistedProgressionJEIPlugin.jeiHelpers.getGuiHelper().createDrawable(new ResourceLocation(Reference.MOD_ID,
                "textures/gui/jei/grinder.png"), 0, 0, 170, 80);
    }

    /**
     * Icon for the category tab.
     * @return icon to draw on the category tab, max size is 16x16 pixels.
     */
    @Override
    public IDrawable getIcon() {
        return AssistedProgressionJEIPlugin.jeiHelpers.getGuiHelper()
                .createDrawableIngredient(new ItemStack(BlockManager.grinder));
    }

    /**
     * Draw extras or additional info about the recipe.
     * Use the mouse position for things like button highlights.
     *
     * @param mouseX the X position of the mouse, relative to the recipe.
     * @param mouseY the Y position of the mouse, relative to the recipe.
     * @see IDrawable for a simple class for drawing things.
     * @see IGuiHelper for useful functions.
     */
    @Override
    public void draw(GrinderRecipe recipe, double mouseX, double mouseY) {
        if(timer == null) {
            timer = AssistedProgressionJEIPlugin.jeiHelpers.getGuiHelper()
                    .createTickTimer((RecipeHelper.pressurePlates.keySet().size() - 1) * 20,
                            (RecipeHelper.pressurePlates.keySet().size() * 20) - 1, false);
        }

        if(grinderStack == null)
            grinderStack = new ItemStack(BlockManager.grinder);

        if(plates == null) {
            plates = new ArrayList<>();
            for(Block block : RecipeHelper.pressurePlates.keySet())
                plates.add(new ItemStack(block));
        }

        GlStateManager.pushMatrix();
        GlStateManager.scaled(2.2, 2.2, 2.2);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translated(-0.5, -0.5, 0);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(plates.get(timer.getValue() / 20), 6, -1);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(grinderStack, 6, 21);
        GlStateManager.popMatrix();

        Minecraft.getInstance().currentScreen.drawString(Minecraft.getInstance().fontRenderer,
                ClientUtils.translate("jei_grinder_progress"),
                2, 4, 0xFFFFFF);
        Minecraft.getInstance().currentScreen.drawString(Minecraft.getInstance().fontRenderer,
                "x" + RecipeHelper.pressurePlates.get(RecipeHelper.pressurePlates.keySet().toArray()[timer.getValue() / 20]),
                48, 18, 0xFFFFFF);
    }

    /**
     * Get tooltips for dispay
     * @param recipe The recipe
     * @param mouseX Cursor x
     * @param mouseY Curson y
     * @return A list of strings, empty for nothing
     */
    @Override
    public List<String> getTooltipStrings(GrinderRecipe recipe, double mouseX, double mouseY) {
        if(mouseX > 15 && mouseX < 43) {
            if(mouseY >= 15 && mouseY <= 30) {
                return Collections.singletonList(plates.get(timer.getValue() / 20).getDisplayName().getFormattedText());
            } else if (mouseY >= 45 && mouseY <= 79) {
                return Collections.singletonList(ClientUtils.translate("jei_assistedprogression_grinder_place"));
            }
        }
        return Collections.emptyList();
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

        guiItemStacks.init(0, true,  127, 36);
        guiItemStacks.init(1, false, 109, 55);

        guiItemStacks.set(ingredients);
    }
}
