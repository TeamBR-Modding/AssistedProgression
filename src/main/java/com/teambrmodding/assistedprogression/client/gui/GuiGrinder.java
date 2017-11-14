package com.teambrmodding.assistedprogression.client.gui;

import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.util.ClientUtils;
import com.teambrmodding.assistedprogression.common.container.ContainerGrinder;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.DecimalFormat;

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
public class GuiGrinder extends GuiBase<ContainerGrinder> {
    protected TileGrinder grinder;

    /**
     * Main constructor for Guis
     *
     * @param inventory The container
     * @param width     The width of the gui
     * @param height    The height of the gui
     * @param title     The title of the gui
     * @param texture   The location of the background texture
     */
    public GuiGrinder(EntityPlayer player, TileGrinder tile) {
        super(new ContainerGrinder(player.inventory, tile), 175, 165,
                tile.getBlockType().getLocalizedName(),
                new ResourceLocation(Reference.MOD_ID, "textures/gui/grinder.png"));
        this.grinder = tile;

        addComponents();
    }

    /**
     * This will be called after the GUI has been initialized and should be where you add all components.
     */
    @Override
    protected void addComponents() {
        if(grinder != null) {
            // Input
            components.add(new GuiComponentText(this, 8, 26,
                    ClientUtils.translate("assistedprogression.grinder.input"), new Color(77, 77, 77)));

            // Grinding
            components.add(new GuiComponentText(this, 8, 48,
                    ClientUtils.translate("assistedprogression.grinder.grinding"), new Color(77,77, 77)));

            // Output
            components.add(new GuiComponentText(this, 8, 70,
                    ClientUtils.translate("assistedprogression.grinder.output"), new Color(77, 77, 77)));

            // Percentage
            components.add(new GuiComponentText(this, 100, 48,
                    new DecimalFormat("#.##").format((grinder.progress / (double) TileGrinder.MAX_PROGRESS) * 100) + "%",
                    new Color(77, 77, 77)) {
                @Override
                public void renderOverlay(int guiLeft, int guiTop, int mouseX, int mouseY) {
                    setLabel(new DecimalFormat("#.##").format((grinder.progress / (double) TileGrinder.MAX_PROGRESS) * 100) + "%");
                    super.renderOverlay(guiLeft, guiTop, mouseX, mouseY);
                }
            });
        }
    }
}
