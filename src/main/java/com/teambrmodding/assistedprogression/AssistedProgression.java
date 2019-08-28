package com.teambrmodding.assistedprogression;

import com.teambrmodding.assistedprogression.client.ClientProxy;
import com.teambrmodding.assistedprogression.common.CommonProxy;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.managers.ScreenHelper;
import com.teambrmodding.assistedprogression.network.PacketManager;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/13/17
 */
@Mod(Reference.MOD_ID)
public class AssistedProgression {

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public AssistedProgression() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeHelper::registerRecipeSerializers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        PacketManager.initPackets();
    }

    protected void setup(final FMLCommonSetupEvent event) {
        proxy.init();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ScreenHelper::registerScreens);
    }

    @SuppressWarnings({"ConstantConditions"})
    protected void serverStarted(FMLServerStartedEvent event) {
        // Add grinder recipes to our list
        RecipeManager recipeManager = event.getServer().getRecipeManager();
        RecipeHelper.grinderRecipes.clear();
        RecipeHelper
                .getRecipes(RecipeHelper.GRINDER_RECIPE_TYPE, recipeManager)
                .values().forEach((recipe) -> {
            if(recipe instanceof GrinderRecipe) {
                GrinderRecipe localRecipe = (GrinderRecipe) recipe;
                RecipeHelper.grinderRecipes.add(localRecipe);
            }
        });

        // Add custom recipes
        ResourceLocation dustLocation   = new ResourceLocation("forge", "dusts");
        ResourceLocation ingotLocation  = new ResourceLocation("forge", "ingots");
        ResourceLocation oreLocation    = new ResourceLocation("forge", "ores");

        /***************************************************************************************************************
         * Dust from Ore                                                                                               *
         ***************************************************************************************************************/
        Tag<Item> oreTag  = ItemTags.getCollection().get(oreLocation);

        // Get all items in the ore forge tag
        for(Item oreItem : oreTag.getAllElements()) {
            // Don't want to add something already made by built in
            if (!RecipeHelper.isValidGrinderInput(new ItemStack(oreItem), null)) {
                // Iterate all tags for item in forge ore tag
                for (ResourceLocation tag : oreItem.getTags()) {
                    // Attempt lookup for dust, structure should be matching in most cases so this should find proper dust
                    ResourceLocation testDustLocation
                            = new ResourceLocation(tag.toString().replace("ores", "dusts"));

                    // We don't want to re-add the whole dust tag
                    if (!testDustLocation.equals(dustLocation)
                            && ItemTags.getCollection().get(testDustLocation) != null
                            && ItemTags.getCollection().get(tag) != null) {
                        ItemStack output = Ingredient.fromTag(ItemTags.getCollection().get(testDustLocation)).getMatchingStacks()[0].copy();
                        output.setCount(2);
                        RecipeHelper.addGrinderRecipe(null,
                                ItemTags.getCollection().get(tag), output);
                    }
                }
            }
        }

        /***************************************************************************************************************
         * Dust from Ingot                                                                                             *
         ***************************************************************************************************************/
        Tag<Item> ingotTag  = ItemTags.getCollection().get(ingotLocation);

        // Get all items in the ore forge tag
        for(Item ingotItem : ingotTag.getAllElements()) {
            // Don't want to add something already made by built in
            if (!RecipeHelper.isValidGrinderInput(new ItemStack(ingotItem), null)) {
                // Iterate all tags for item in forge ingot tag
                for (ResourceLocation tag : ingotItem.getTags()) {
                    // Attempt lookup for dust, structure should be matching in most cases so this should find proper dust
                    ResourceLocation testDustLocation
                            = new ResourceLocation(tag.toString().replace("ingots", "dusts"));

                    // We don't want to re-add the whole dust tag
                    if (!testDustLocation.equals(dustLocation)
                            && ItemTags.getCollection().get(testDustLocation) != null
                            && ItemTags.getCollection().get(tag) != null) {
                        ItemStack output = Ingredient.fromTag(ItemTags.getCollection().get(testDustLocation)).getMatchingStacks()[0].copy();
                        RecipeHelper.addGrinderRecipe(null,
                                ItemTags.getCollection().get(tag), output);
                    }
                }
            }
        }
    }
}
