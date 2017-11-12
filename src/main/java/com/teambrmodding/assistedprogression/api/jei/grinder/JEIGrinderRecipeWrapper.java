package com.teambrmodding.assistedprogression.api.jei.grinder;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/10/17
 */
public class JEIGrinderRecipeWrapper implements IRecipeWrapper {

    // Variables
    private ItemStack input, outputOne, outputTwo;
    private int secondOutputChance;

    /*******************************************************************************************************************
     * Constructor                                                                                                     *
     *******************************************************************************************************************/

    /**
     * Holds the information of a recipe
     * @param in The input stack
     * @param outOne Output one, should probably always have this
     * @param outTwo Optional output two
     * @param outThree Optional output three
     */
    public JEIGrinderRecipeWrapper(@Nonnull ItemStack in, @Nonnull ItemStack outOne,
                                   @Nonnull ItemStack outTwo, int chance) {
        input = in;
        outputOne = outOne;
        outputTwo = outTwo;
        secondOutputChance = chance;
    }

    /*******************************************************************************************************************
     * Helper Methods                                                                                                  *
     *******************************************************************************************************************/

    /**
     * Used to validate required info has been passed
     * @return True if we have an input and required output
     */
    public boolean isValid() {
        return !input.isEmpty() && !outputOne.isEmpty();
    }

    /*******************************************************************************************************************
     * IRecipeWrapper                                                                                                  *
     *******************************************************************************************************************/

    /**
     * Gets all the recipe's ingredients by filling out an instance of {@link IIngredients}.
     *
     * @param ingredients
     * @since JEI 3.11.0
     */
    @Override
    public void getIngredients(IIngredients ingredients) {
        // Set inputs
        ingredients.setInput(ItemStack.class, input);

        // Set outputs
        ingredients.setOutputs(ItemStack.class, Arrays.asList(outputOne, outputTwo));

        // Set chance
        ingredients.setOutput(Integer.class, secondOutputChance);
    }

    /**
     * Draw additional info about the recipe.
     * Use the mouse position for things like button highlights.
     * Tooltips are handled by {@link IRecipeWrapper#getTooltipStrings(int, int)}
     *
     * @param mouseX the X position of the mouse, relative to the recipe.
     * @param mouseY the Y position of the mouse, relative to the recipe.
     * @see IDrawable for a simple class for drawing things.
     * @see IGuiHelper for useful functions.
     * @since JEI 2.19.0
     */
    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRendererObj.drawString(String.valueOf(secondOutputChance) + "%", 114, 41, 0xffffff);
    }
}
