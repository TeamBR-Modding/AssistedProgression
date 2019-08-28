package com.teambrmodding.assistedprogression.recipe;

import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.item.crafting.IRecipeType;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/27/2019
 */
public class RecipeTypeGrinderRecipe implements IRecipeType<GrinderRecipe> {
    @Override
    public String toString() {
        return Reference.MOD_ID + ":grinder";
    }
}