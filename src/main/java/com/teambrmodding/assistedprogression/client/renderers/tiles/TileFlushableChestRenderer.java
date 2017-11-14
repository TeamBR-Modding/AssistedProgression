package com.teambrmodding.assistedprogression.client.renderers.tiles;

import com.teambr.bookshelf.common.blocks.BlockFourWayRotating;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

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
public class TileFlushableChestRenderer<T extends TileFlushableChest> extends TileEntitySpecialRenderer<T> {
    private ResourceLocation location = new ResourceLocation(Reference.MOD_ID(), "textures/blocks/blockFlushablechest.png");
    private ModelChest modelChest     = new ModelChest();

    @Override
    public void renderTileEntityFast(T tile, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        if(tile == null)
            return;

        int facing = 3;

        if(tile.hasWorld() && tile.getWorld().getBlockState(tile.getPos()).getBlock() == BlockManager.blockFlushableChest())
            facing = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockFourWayRotating.FOUR_WAY).getIndex();

        if(destroyStage >= 0) {
            bindTexture(TileEntityShulkerBoxRenderer.DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else
            bindTexture(location);

        GlStateManager.pushMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate(x, y + 1.0F, z + 1.0F);
        GlStateManager.scale(1.0F, -1F, -1F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int k = 0;
        switch (facing) {
            case 2 :
                k = 180;
                break;
            case 3 :
                k = 0;
                break;
            case 4 :
                k = 90;
                break;
            case 5 :
                k = -90;
                break;
            default :
        }
        GlStateManager.rotate(k, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float lidAngle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partial;
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        modelChest.chestLid.rotateAngleX = -(lidAngle * (float) Math.PI / 2.0F);

        modelChest.renderAll();

        if(destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        GlStateManager.popMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}
