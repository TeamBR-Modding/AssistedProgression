package com.teambrmodding.assistedprogression.client.gui;

import com.teambrmodding.assistedprogression.common.container.ContainerCrafter;
import com.teambrmodding.assistedprogression.common.tiles.TileCrafter;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * This file was created for NeoTech
 *
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 18, 2015
 */
public class GuiCrafter extends GuiContainer {

    private ResourceLocation background = new ResourceLocation(Reference.MOD_ID + ":textures/gui/crafter.png");

    public GuiCrafter(InventoryPlayer playerInv, TileCrafter crafter) {
        super(new ContainerCrafter(playerInv, crafter));
        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRenderer.drawString(I18n.format("tile.assistedprogression:block_crafter.registryName"), (xSize / 2) - (fontRenderer
                .getStringWidth(I18n.format("tile.assistedprogression:block_crafter.registryName")) / 2), 5, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        drawDefaultBackground();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
