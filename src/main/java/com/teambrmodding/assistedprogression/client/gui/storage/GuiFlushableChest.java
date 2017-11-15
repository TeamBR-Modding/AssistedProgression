package com.teambrmodding.assistedprogression.client.gui.storage;

import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.GuiColor;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSetNumber;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.util.ClientUtils;
import com.teambrmodding.assistedprogression.common.container.storage.ContainerFlushableChest;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

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
public class GuiFlushableChest extends GuiBase<ContainerFlushableChest> {
    private TileFlushableChest chest;

    /**
     * Main constructor for Guis
     *
     * @param inventory The container
     * @param width     The width of the gui
     * @param height    The height of the gui
     * @param title     The title of the gui
     * @param texture   The location of the background texture
     */
    public GuiFlushableChest(EntityPlayer player, TileFlushableChest tile) {
        super(new ContainerFlushableChest(player.inventory, tile), 175, 165,
                tile.getBlockType().getLocalizedName(),
                new ResourceLocation(Reference.MOD_ID, "textures/gui/flushable_chest.png"));
        chest = tile;
    }

    /**
     * This will be called after the GUI has been initialized and should be where you add all components.
     */
    @Override
    protected void addComponents() {
        // No Op
    }

    @Override
    protected void addRightTabs(GuiTabCollection tabs) {
        ArrayList<BaseComponent> infoTabComponents = new ArrayList<>();

        // Interval
        infoTabComponents.add(new GuiComponentSetNumber(this, 72, 21, 177, 0,
                chest.getVariable(TileFlushableChest.FLUSH_INTERVAL_VARID).intValue(), 1, 60) {
            /**
             * Called when the user sets the value or when the value is changed
             *
             * @param value The value set by the user
             */
            @Override
            protected void setValue(int value) {
                chest.setVariable(TileFlushableChest.FLUSH_INTERVAL_VARID, value);
                chest.sendValueToServer(TileFlushableChest.FLUSH_INTERVAL_VARID, value);
            }
        });

        // Auto Flush
        infoTabComponents.add(new GuiComponentCheckBox(this, 10, 20, 178, 22,
                chest.getVariable(TileFlushableChest.AUTO_FLUSH_VARID) == 1 ? true : false, ClientUtils.translate("assistedprogression.text.flushableChest.autoFlush")) {
            @Override
            protected void setValue(boolean value) {
                chest.setVariable(TileFlushableChest.AUTO_FLUSH_VARID, value ? 1 : -1);
                chest.sendValueToServer(TileFlushableChest.AUTO_FLUSH_VARID, value ? 1 : -1);
            }
        });

        // Play Sounds
        infoTabComponents.add(new GuiComponentCheckBox(this, 10, 30, 178, 22,
                chest.getVariable(TileFlushableChest.PLAY_FLUSH_SOUND_VARID) == 1 ? true : false, ClientUtils.translate("assistedprogression.text.flushableChest.playSound")) {
            @Override
            protected void setValue(boolean value) {
                chest.setVariable(TileFlushableChest.PLAY_FLUSH_SOUND_VARID, value ? 1 : -1);
                chest.sendValueToServer(TileFlushableChest.PLAY_FLUSH_SOUND_VARID, value ? 1 : -1);
            }
        });

        // Title
        infoTabComponents.add(new GuiComponentText(this, 26, 6,
                GuiColor.ORANGE + ClientUtils.translate("assistedprogression.text.config"), null));
        tabs.addTab(infoTabComponents, 115, 50, 176, 37, new ItemStack(Item.getItemFromBlock(Blocks.STONE_BUTTON)));
    }
}
