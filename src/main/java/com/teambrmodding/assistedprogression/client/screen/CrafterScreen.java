package com.teambrmodding.assistedprogression.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teambr.nucleus.util.ClientUtils;
import com.teambr.nucleus.util.RenderUtils;
import com.teambrmodding.assistedprogression.common.container.CrafterContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/29/2019
 */
public class CrafterScreen extends ContainerScreen<CrafterContainer> {

    // Display Image
    private ResourceLocation background = new ResourceLocation(Reference.MOD_ID + ":textures/gui/crafter.png");

    /**
     * Creates a gui for the crafter
     * @param container THe container for the object
     * @param playerInventory The player's inventory of the one openning
     * @param title Title for display
     */
    public CrafterScreen(CrafterContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        xSize = 176;
        ySize = 166;
    }

    /*******************************************************************************************************************
     * Screen                                                                                                          *
     *******************************************************************************************************************/

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, ClientUtils.translate("block.assistedprogression.crafter"),
                (xSize / 2) - (font.getStringWidth(ClientUtils.translate("block.assistedprogression.crafter")) / 2),
                5, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float f, int x, int y) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtils.bindTexture(background);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
