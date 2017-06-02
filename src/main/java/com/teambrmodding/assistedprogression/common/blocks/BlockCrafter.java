package com.teambrmodding.assistedprogression.common.blocks;

import com.teambrmodding.assistedprogression.common.tiles.TileCrafter;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 6/2/2017
 */
public class BlockCrafter extends BaseBlock {

    protected BlockCrafter() {
        super(Material.WOOD, "blockCrafter", TileCrafter.class);
    }

    @Override
    public float getHardness() {
        return 1.5F;
    }

}
