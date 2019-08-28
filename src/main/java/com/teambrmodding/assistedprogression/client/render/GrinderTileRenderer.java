package com.teambrmodding.assistedprogression.client.render;

import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

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
public class GrinderTileRenderer<T extends GrinderTile> extends TileEntityRenderer<T> {
    private ItemRenderer itemRenderer;
    private static ItemEntity customItem;

    @Override
    public void render(T grinder, double x, double y, double z, float partialTicks, int destroyStage) {
        // Make sure not null
        if (customItem == null) {
            customItem = new ItemEntity(EntityType.ITEM, this.getWorld());
        }

        // Custom item renderer, don't want to use minecraft one since you can't change hover start on entity anymore
        if (this.itemRenderer == null) {
            this.itemRenderer = new ItemRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()) {
                @Override
                public int getModelCount(ItemStack stack) {
                    return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
                }

                @Override
                public boolean shouldBob() {
                    return false;
                }

                @Override
                public boolean shouldSpreadItems() {
                    return true;
                }
            };
        }

        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5D, y, z + 0.5D);
        for(int i = 4; i < 7; i++) {
            ItemStack stack = grinder.getStackInSlot(i);
            if(!stack.isEmpty()) {
                customItem.setItem(stack);
                GlStateManager.pushMatrix();
                GlStateManager.scaled(0.45D, 0.45D, 0.45D);
                int rotation = (i - 3) * 120;
                double xRot = 0.25 * Math.cos(Math.toRadians(rotation));
                double zRot = 0.25 * Math.sin(Math.toRadians(rotation));
                this.itemRenderer.doRender(customItem, xRot / 0.45, -0.1, zRot / 0.45,
                        0.0F, 0);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }
}
