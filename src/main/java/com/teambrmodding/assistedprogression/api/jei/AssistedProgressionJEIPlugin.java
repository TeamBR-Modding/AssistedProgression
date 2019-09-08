package com.teambrmodding.assistedprogression.api.jei;

import com.teambrmodding.assistedprogression.api.jei.grinder.GrinderJEIRecipeCategory;
import com.teambrmodding.assistedprogression.api.jei.trashbag.TrashBagGhostIngredientHandler;
import com.teambrmodding.assistedprogression.client.screen.CrafterScreen;
import com.teambrmodding.assistedprogression.client.screen.GrinderScreen;
import com.teambrmodding.assistedprogression.client.screen.TrashBagScreen;
import com.teambrmodding.assistedprogression.common.container.CrafterContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
 * @since 8/27/2019
 */
@JeiPlugin
public class AssistedProgressionJEIPlugin implements IModPlugin {

    // JEI Helper Reference
    public static IJeiHelpers jeiHelpers;

    // Category for the Grinder Recipes
    public static IRecipeCategory grinderCategory;

    /**
     * Get the unique name for this plugin
     * @return A new ResourceLocation with our path
     */
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Reference.MOD_ID, "jei");
    }

    /**
     * The grinder can change dynamically, so we need to wrap them on the fly
     * @param registration Registration bus
     */
    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        registration.addRecipeManagerPlugin(new AssistedProgressionJEIPluginAdvanced());
    }

    /**
     * Create the categories we handle
     */
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelpers = registration.getJeiHelpers();

        grinderCategory = new GrinderJEIRecipeCategory();
        registration.addRecipeCategories(grinderCategory);
    }

    /**
     * We want to let the crafter accept shift click from crafting recipe, identify here
     */
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CrafterContainer.class, VanillaRecipeCategoryUid.CRAFTING,
                2, 9, 20, 36);
    }

    /**
     * Adds the clickable areas into the GUI
     */
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Add grinder click area
        registration.addRecipeClickArea(GrinderScreen.class, 47, 36, 27, 20,
                new ResourceLocation(Reference.MOD_ID, "grinder"));

        // Add crafting click area
        registration.addRecipeClickArea(CrafterScreen.class, 61,  34, 14, 9, VanillaRecipeCategoryUid.CRAFTING);
        registration.addRecipeClickArea(CrafterScreen.class, 101, 63, 14, 9, VanillaRecipeCategoryUid.CRAFTING);

        // Create the ghost item handler for trash bags
        registration.addGhostIngredientHandler(TrashBagScreen.class, new TrashBagGhostIngredientHandler<>());
    }

    /**
     * Register the items that launch a category when pressing on usage, also will stack on left of usable objects
     */
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockManager.grinder),
                new ResourceLocation(Reference.MOD_ID, "grinder"));
        registration.addRecipeCatalyst(new ItemStack(BlockManager.crafter),
                VanillaRecipeCategoryUid.CRAFTING);
    }

    /**
     * Called when done loading, we use this to help with the async issues on servers
     */
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        // JEI sometimes fails, not sure root cause but to prevent this from failing to register, check for add here
        if(!jeiRuntime.getRecipeManager().getRecipeCategories().contains(grinderCategory))
            jeiRuntime.getRecipeManager().getRecipeCategories().add(grinderCategory);
    }
}
