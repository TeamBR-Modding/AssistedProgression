package com.teambrmodding.assistedprogression.api.jei;

import com.teambrmodding.assistedprogression.api.jei.grinder.JEIGrinderRecipeCategory;
import com.teambrmodding.assistedprogression.client.screen.CrafterScreen;
import com.teambrmodding.assistedprogression.client.screen.GrinderScreen;
import com.teambrmodding.assistedprogression.common.container.CrafterContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
public class JEIAssistedProgression implements IModPlugin {

    // JEI Helper Reference
    public static IJeiHelpers jeiHelpers;

    public static IRecipeCategory grinderCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Reference.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelpers = registration.getJeiHelpers();

        grinderCategory = new JEIGrinderRecipeCategory();
        registration.addRecipeCategories(grinderCategory);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockManager.grinder),
                new ResourceLocation(Reference.MOD_ID, "grinder"));
        registration.addRecipeCatalyst(new ItemStack(BlockManager.crafter),
                VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CrafterContainer.class, VanillaRecipeCategoryUid.CRAFTING,
                2, 9, 20, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrinderScreen.class, 47, 36, 27, 20,
                new ResourceLocation(Reference.MOD_ID, "grinder"));

        registration.addRecipeClickArea(CrafterScreen.class, 61,  34, 14, 9, VanillaRecipeCategoryUid.CRAFTING);
        registration.addRecipeClickArea(CrafterScreen.class, 101, 63, 14, 9, VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        registration.addRecipeManagerPlugin(new JEIRecipeManagerPlugin());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        // JEI sometimes fails, not sure root cause but to prevent this from failing to register, check for add here
        if(!jeiRuntime.getRecipeManager().getRecipeCategories().contains(grinderCategory))
            jeiRuntime.getRecipeManager().getRecipeCategories().add(grinderCategory);
    }
}
