package com.teambrmodding.assistedprogression.client.screen;

import com.teambr.nucleus.client.gui.GuiBase;
import com.teambr.nucleus.client.gui.component.display.GuiComponentText;
import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.common.container.GrinderContainer;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
 * @since 8/27/2019
 */
public class GrinderScreen extends GuiBase<GrinderContainer> {
    protected GrinderTile grinder;

    public GrinderScreen(GrinderContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title,
                176, 166,
                new ResourceLocation(Reference.MOD_ID, "textures/gui/grinder.png"));
        this.grinder = container.tile;
        addComponents();
    }

    /**
     * This will be called after the GUI has been initialized and should be where you add all components.
     */
    @Override
    protected void addComponents() {
        if(grinder != null) {
            // Input
            components.add(new GuiComponentText(this, 6, 20,
                    ClientUtils.translate("assistedprogression.grinder.input"), new Color(77, 77, 77)));

            // Grinding
            components.add(new GuiComponentText(this, 6, 42,
                    ClientUtils.translate("assistedprogression.grinder.grinding"), new Color(77,77, 77)));

            // Output
            components.add(new GuiComponentText(this, 6, 64,
                    ClientUtils.translate("assistedprogression.grinder.output"), new Color(77, 77, 77)));

            // Percentage
            components.add(new GuiComponentText(this, 105, 48,
                    new DecimalFormat("#.##").format((grinder.progress / (double) GrinderTile.MAX_PROGRESS) * 100) + "%",
                    new Color(77, 77, 77)) {
                @Override
                public void renderOverlay(int guiLeft, int guiTop, int mouseX, int mouseY) {
                    setLabel(new DecimalFormat("#.##").format((grinder.progress / (double) GrinderTile.MAX_PROGRESS) * 100) + "%");
                    super.renderOverlay(guiLeft, guiTop, mouseX, mouseY);
                }
            });
        }
    }
}
