package com.teambrmodding.assistedprogression.client.renderers.tiles;

import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

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
public class TileGrinderRenderer<T extends TileGrinder> extends TileEntitySpecialRenderer<T> {

    @Override
    public void render(T grinder, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y, z + 0.5D);
        for(int i = 4; i < 7; i++) {
            ItemStack stack = grinder.getStackInSlot(i);
            if(!stack.isEmpty()) {
                EntityItem entityItem = new EntityItem(getWorld(), 0.0, 0.0, 0.0, stack);
                entityItem.motionX     = 0;
                entityItem.motionY     = 0;
                entityItem.motionZ     = 0;
                entityItem.hoverStart  = 0;
                entityItem.rotationYaw = 0;

                GlStateManager.pushMatrix();

                RenderHelper.enableGUIStandardItemLighting();
                RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

                renderManager.setRenderShadow(false);
                GlStateManager.pushAttrib();
                GlStateManager.scale(0.45D, 0.45D, 0.45D);

                int rotation = (i - 3) * 120;
                double xRot = 0.25 * Math.cos(Math.toRadians(rotation));
                double zRot = 0.25 * Math.sin(Math.toRadians(rotation));
                renderManager.renderEntity(entityItem, xRot / 0.45, -0.1, zRot / 0.45,
                        0.0F, 0, true);

                GlStateManager.popAttrib();
                GlStateManager.enableLighting();
                renderManager.setRenderShadow(true);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }
}
