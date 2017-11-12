package com.teambrmodding.assistedprogression.registries.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.asm.transformers.ItemStackTransformer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


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
public class GrinderRecipe extends AbstractRecipe<ItemStack, ItemStack> {
    public String inputItemStack, outputItemStack;

    /**
     * Creates the recipe
     */
    public GrinderRecipe(String inputItemStack, String outputItemStack) {
        this.inputItemStack = inputItemStack;
        this.outputItemStack = outputItemStack;
    }

    /***************************************************************************************************************
     * AbstractRecipe                                                                                              *
     ***************************************************************************************************************/

    /**
     * Used to get the output of this recipe
     *
     * @param input The input object
     * @return The output object
     */
    @Nonnull
    @Override
    public ItemStack getOutput(ItemStack input) {
        if(input == null) // Safety Check
            return null;

        if(isValidInput(input))
            return getItemStackFromString(outputItemStack);

        return null;
    }

    /**
     * Is the input valid for an output
     *
     * @param input The input object
     * @return True if there is an output
     */
    @Override
    public boolean isValidInput(ItemStack input) {
        return isItemStackValidForRecipeStack(inputItemStack, input);
    }
}