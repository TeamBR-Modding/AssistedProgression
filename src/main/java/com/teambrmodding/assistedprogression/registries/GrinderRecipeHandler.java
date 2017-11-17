package com.teambrmodding.assistedprogression.registries;

import com.google.gson.reflect.TypeToken;
import com.teambr.nucleus.helper.LogHelper;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.registries.recipes.GrinderRecipe;
import net.minecraft.command.CommandBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

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
public class GrinderRecipeHandler extends AbstractRecipeHandler<GrinderRecipe, ItemStack, ItemStack> {

    /**
     * Used to get the base name of the files
     *
     * @return The base name for the files
     */
    @Override
    public String getBaseName() {
        return "grinder";
    }

    /**
     * This is the current version of the registry, if you update this it will cause the registry to be redone
     *
     * @return Current version number
     */
    @Override
    public int getVersion() {
        return 0;
    }


    /**
     * Used to get the default folder location
     *
     * @return The folder location
     */
    @Override
    public String getBaseFolderLocation() {
        return AssistedProgression.configFolderLocation;
    }

    /**
     * Used to get what type token to read from file (Generics don't handle well)
     *
     * @return A defined type token
     */
    @Override
    public TypeToken<ArrayList<GrinderRecipe>> getTypeToken() {
        return new TypeToken<ArrayList<GrinderRecipe>>() {
        };
    }

    @Override
    public CommandBase getCommand() {
        //TODO: Rethink this
        return null;
    }

    @Override
    protected void generateDefaultRecipes() {
        LogHelper.logger.info("[Assisted Progression] Creating Grinder Recipe List...");

        addRecipe(new GrinderRecipe("oreRedstone", "dustRedstone:12"));
        addRecipe(new GrinderRecipe("oreLapis", "gemLapis:8"));
        addRecipe(new GrinderRecipe(getItemStackString(new ItemStack(Items.BLAZE_ROD)), getItemStackString(new ItemStack(Items.BLAZE_POWDER, 4))));
        addRecipe(new GrinderRecipe("cobblestone", "sand:2"));
        addRecipe(new GrinderRecipe("sand", "gravel:2"));
        addRecipe(new GrinderRecipe("bone", getItemStackString(new ItemStack(Items.DYE, 8, EnumDyeColor.WHITE.getDyeDamage()))));
        addRecipe(new GrinderRecipe("oreQuartz", "gemQuartz:4"));
        addRecipe(new GrinderRecipe(getItemStackString(new ItemStack(Blocks.CLAY)), getItemStackString(new ItemStack(Items.CLAY_BALL, 4))));
        addRecipe(new GrinderRecipe(getItemStackString(new ItemStack(Blocks.HARDENED_CLAY)), getItemStackString(new ItemStack(Items.BRICK, 4))));
        addRecipe(new GrinderRecipe("oreDiamond", "gemDiamond:2"));
        addRecipe(new GrinderRecipe("oreEmerald", "gemEmerald:2"));
        addRecipe(new GrinderRecipe("glowstone", "dustGlowstone:4"));
        addRecipe(new GrinderRecipe("oreCoal", getItemStackString(new ItemStack(Items.COAL, 3))));
        addRecipe(new GrinderRecipe("minecraft:wool:" + OreDictionary.WILDCARD_VALUE + ":1", getItemStackString(new ItemStack(Items.STRING, 4))));
        addRecipe(new GrinderRecipe("blockGlass", "sand:1"));
        addRecipe(new GrinderRecipe("gravel", getItemStackString(new ItemStack(Items.FLINT, 3))));

        // Adjust to Ore Dictionary
        String[] oreDictionary = OreDictionary.getOreNames();

        for (String entry : oreDictionary) {
            if (entry.startsWith("dust") && !doesRecipeExist(entry.replaceFirst("dust", "ore"))) {
                List<ItemStack> oreList = OreDictionary.getOres(entry.replaceFirst("dust", "ore"));
                if (!oreList.isEmpty()) {
                    // We want a single type output
                    List<ItemStack> dustList = OreDictionary.getOres(entry);
                    if (!dustList.isEmpty())
                        addRecipe(new GrinderRecipe(entry.replaceFirst("dust", "ore"),
                                getItemStackString(new ItemStack(dustList.get(0).getItem(), 2, dustList.get(0).getItemDamage()))));
                }
            } else if (entry.startsWith("ingot")) {
                List<ItemStack> dustList = OreDictionary.getOres(entry.replaceFirst("ingot", "dust"));
                if (!dustList.isEmpty() && !doesRecipeExist(entry.replaceFirst("ingot", "dust"))) {
                    addRecipe(new GrinderRecipe(entry,
                            getItemStackString(new ItemStack(dustList.get(0).getItem(), 1, dustList.get(0).getItemDamage()))));
                }
            }
        }

        // TODO: Figure out how this is done now
        /*
        // Add Flower to Dyes
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapelessRecipes) {
                ShapelessRecipes shapelessRecipe = (ShapelessRecipes) recipe;
                if (shapelessRecipe.recipeItems.size() == 1 &&
                        Block.getBlockFromItem(shapelessRecipe.recipeItems.get(0).getItem()) != null &&
                        Block.getBlockFromItem(shapelessRecipe.recipeItems.get(0).getItem()) instanceof BlockFlower) {
                    String inputString = getItemStackString(shapelessRecipe.recipeItems.get(0));
                    if (!doesRecipeExist(inputString))
                        addRecipe(new GrinderRecipe(inputString,
                                "dye:" +
                                        String.valueOf(shapelessRecipe.getRecipeOutput().getCount() * 2) + ":" +
                                        String.valueOf(shapelessRecipe.getRecipeOutput().getItemDamage()),
                                "", 0));
                }
            }
        }
        */

        saveToFile();
    }

    /**
     * Does this already exist in our registry
     */
    private boolean doesRecipeExist(String stack) {
        for (GrinderRecipe recipe : recipes)
            if (stack.equalsIgnoreCase(recipe.inputItemStack))
                return true;
        return false;
    }
}