package com.teambrmodding.assistedprogression.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Collections;
import java.util.Map;

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
public class GrinderRecipe implements IRecipe<IInventory> {

    public static final Serializer SERIALIZER = new Serializer();

    private final Ingredient input;
    private final ItemStack output;
    private final ResourceLocation id;

    public GrinderRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    /**
     * Filler for vanilla, use isValid() instead
     */
    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.input.test(inv.getStackInSlot(0));
    }

    /**
     * Test if the given stack is valid
     * @param input Input itemstack
     * @return True if valid
     */
    public boolean isValid(ItemStack input) {
        return this.input.test(input);
    }

    /**
     * Gets output for given input, or empty
     * @param stack Stack
     * @return Output copy if available for given input, else empty
     */
    public ItemStack getOutputOrEmpty(ItemStack stack) {
        if(isValid(stack))
            return this.output.copy();
        else
            return ItemStack.EMPTY;
    }

    /**
     * Filler for vanilla, use getRecipeOutput() instead
     */
    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.output.copy();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> returnList = NonNullList.create();
        returnList.add(0, input);
        return returnList;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeHelper.GRINDER_RECIPE_TYPE;
    }


    /*******************************************************************************************************************
     * Serializer to send/receive recipes and load                                                                     *
     *******************************************************************************************************************/
    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<GrinderRecipe> {

        Serializer() {
            setRegistryName(new ResourceLocation(Reference.MOD_ID, "grinder"));
        }

        @Override
        public GrinderRecipe read(ResourceLocation recipeId, JsonObject json) {
            final JsonElement inputElement = JSONUtils.isJsonArray(json, "input")
                    ? JSONUtils.getJsonArray(json, "input")
                    : JSONUtils.getJsonObject(json, "input");
            final Ingredient input = Ingredient.deserialize(inputElement);

            final ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));

            return new GrinderRecipe(recipeId, input, output);
        }

        @Override
        public GrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            final Ingredient input = Ingredient.read(buffer);
            final ItemStack output = buffer.readItemStack();
            return new GrinderRecipe(recipeId, input, output);
        }

        @Override
        public void write(PacketBuffer buffer, GrinderRecipe recipe) {
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
        }
    }
}
