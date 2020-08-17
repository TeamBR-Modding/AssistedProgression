package com.teambrmodding.assistedprogression.client.render;

import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
@OnlyIn(Dist.CLIENT)
public class GrinderTileRenderer<T extends GrinderTile> extends TileEntityRenderer<T> {

    // We use our own renderer, minecraft has limited access to some things we need
    private ItemRenderer itemRenderer;

    // Custom entity, to pass around so we don't have to keep making so many instances
    private static ItemEntity customItem;

    public GrinderTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T grinder, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        // Make sure not null
        if (customItem == null) {
            customItem = new ItemEntity(EntityType.ITEM, renderDispatcher.world);
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
                    return false;
                }
            };
        }

        // Render items
        matrixStackIn.push();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.translate(0.5D, 0, 0.5D);
        for(int i = 4; i < 7; i++) {
            ItemStack stack = grinder.getStackInSlot(i);
            if(!stack.isEmpty()) {
                customItem.setItem(stack);
                matrixStackIn.push();
                matrixStackIn.scale(0.45F, 0.45F, 0.45F);

                // Each slot rotates 120 degrees, dropping 3 to offset slot to normal range for calculation
                int rotation = (i - 3) * 120;
                double xRot = 0.25 * Math.cos(Math.toRadians(rotation));
                double zRot = 0.25 * Math.sin(Math.toRadians(rotation));
                this.itemRenderer.render(customItem, (float) (xRot / 0.45), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
                matrixStackIn.pop();
            }
        }
        matrixStackIn.pop();
    }
}
