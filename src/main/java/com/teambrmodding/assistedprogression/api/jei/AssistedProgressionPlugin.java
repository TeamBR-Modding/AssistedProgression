package com.teambrmodding.assistedprogression.api.jei;

import com.teambr.nucleus.client.gui.GuiBase;
import com.teambrmodding.assistedprogression.api.jei.grinder.JEIGrinderRecipeCategory;
import com.teambrmodding.assistedprogression.client.gui.GuiCrafter;
import com.teambrmodding.assistedprogression.client.gui.GuiGrinder;
import com.teambrmodding.assistedprogression.common.container.ContainerCrafter;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

/**
 * This file was created for Assisted-Progression
 * <p>
 * Assisted-Progression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 1/18/2017
 */
@JEIPlugin
public class AssistedProgressionPlugin implements IModPlugin {

    // JEI Helper Reference
    public static IJeiHelpers jeiHelpers;

    // UUIDS
    public static final String GRINDER_UUID = "assistedprogression.grinder";

    /**
     * Register this mod plugin with the mod registry.
     */
    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();

        // Fill in recipes
        registry.addRecipes(JEIGrinderRecipeCategory.buildRecipeList(), GRINDER_UUID);

        /*
         * Add data for JEI
         */

        // Grinder
        registry.addRecipeCatalyst(new ItemStack(BlockManager.blockGrinder), GRINDER_UUID);
        registry.addRecipeClickArea(GuiGrinder.class, 47, 36, 27, 20, GRINDER_UUID);

        // Crafter
        registry.addRecipeCatalyst(new ItemStack(BlockManager.blockCrafter), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeClickArea(GuiCrafter.class, 61,  34, 14, 9, VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeClickArea(GuiCrafter.class, 101, 63, 14, 9, VanillaRecipeCategoryUid.CRAFTING);


        // Adds shift click to crafter
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCrafter.class, VanillaRecipeCategoryUid.CRAFTING,
                2, 9, 20, 36);

        // Move JEI item pane around tabs
        registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<GuiBase>() {
            @Nonnull
            @Override
            public Class<GuiBase> getGuiContainerClass() {
                return GuiBase.class;
            }

            @Nullable
            @Override
            public List<Rectangle> getGuiExtraAreas(@Nonnull GuiBase guiContainer) {
                return guiContainer.getCoveredAreas();
            }

            @Nullable
            @Override
            public Object getIngredientUnderMouse(@Nonnull GuiBase guiContainer, int mouseX, int mouseY) {
                return null;
            }
        });
    }

    /**
     * Register the categories handled by this plugin.
     * These are registered before recipes so they can be checked for validity.
     *
     * @since JEI 4.5.0
     */
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new JEIGrinderRecipeCategory()
        );
    }
}
