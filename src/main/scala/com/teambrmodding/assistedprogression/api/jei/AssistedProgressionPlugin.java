package com.teambrmodding.assistedprogression.api.jei;

import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambrmodding.assistedprogression.api.jei.grinder.JEIGrinderRecipeCategory;
import com.teambrmodding.assistedprogression.api.jei.grinder.JEIGrinderRecipeHandler;
import com.teambrmodding.assistedprogression.api.jei.grinder.JEIGrinderRecipeMaker;
import com.teambrmodding.assistedprogression.common.container.ContainerCrafter;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import mezz.jei.api.*;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
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

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();



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

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {}

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
}
