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
    private ItemStack input, outputOne;

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
    public JEIGrinderRecipeWrapper(@Nonnull ItemStack in, @Nonnull ItemStack outOne) {
        input = in;
        outputOne = outOne;
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
        ingredients.setOutput(ItemStack.class, outputOne);
    }
}
