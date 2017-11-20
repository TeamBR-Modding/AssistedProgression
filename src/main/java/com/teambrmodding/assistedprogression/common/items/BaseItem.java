package com.teambrmodding.assistedprogression.common.items;

import com.teambr.nucleus.annotation.IRegisterable;
import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.helper.ItemRenderHelper;
import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
public class BaseItem extends Item implements IRegisterable<Item>, IAdvancedToolTipProvider {

    // Item registryName
    private String name;

    /**
     * Base constructor for all items
     * @param itemName The item registryName
     * @param maxStackSize Maximum stack size
     */
    public BaseItem(String itemName, int maxStackSize) {
        super();
        name = itemName;
        setCreativeTab(AssistedProgression.tabAssistedProgression);
        setMaxStackSize(maxStackSize);
        setUnlocalizedName(Reference.MOD_ID + ":" + name);
        setRegistryName(Reference.MOD_ID, itemName);
    }

    /*******************************************************************************************************************
     * Registration                                                                                                    *
     *******************************************************************************************************************/

    /**
     * Registers an object to the ForgeRegistry
     *
     * @param registry The Block Forge Registry
     */
    @Override
    public void registerObject(IForgeRegistry<Item> registry) {
        registry.register(this);
    }

    /**
     * Register the renderers for the block/item
     */
    @Override
    public void registerRender() {
        ItemRenderHelper.registerItem(this);
    }

    /*******************************************************************************************************************
     * BaseItem                                                                                                        *
     *******************************************************************************************************************/

    /**
     * Used to get the registryName of this item
     * @return Given registryName
     */
    public String getName() {
        return name;
    }


    /*******************************************************************************************************************
     * IAdvancedToolTipProvided                                                                                        *
     *******************************************************************************************************************/

    /**
     * Get the tool tip to present when shift is pressed
     *
     * @param stack The itemstack
     * @return The list to display
     */
    @Nullable
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        return Collections.singletonList(ClientUtils.translate(this.name + ".desc"));
    }
}
