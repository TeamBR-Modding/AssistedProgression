package com.teambrmodding.assistedprogression.client.gui;

import com.teambr.nucleus.client.gui.GuiBase;
import com.teambr.nucleus.client.gui.component.display.GuiComponentTextureAnimated;
import com.teambr.nucleus.util.EnergyUtils;
import com.teambrmodding.assistedprogression.common.container.ContainerKineticGenerator;
import com.teambrmodding.assistedprogression.common.tiles.TileKineticGenerator;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/18/17
 */
public class GuiKineticGenerator extends GuiBase<ContainerKineticGenerator> {
    protected TileKineticGenerator energyStorage;

    /**
     * Main constructor for Guis
     */
    public GuiKineticGenerator(EntityPlayer player, TileKineticGenerator energyStorage) {
        super(new ContainerKineticGenerator(player.inventory, energyStorage), 175, 165,
                energyStorage.getBlockType().getLocalizedName(), new ResourceLocation(Reference.MOD_ID, "textures/gui/kinetic_generator.png"));
        this.energyStorage = energyStorage;

        addComponents();
    }

    /**
     * This will be called after the GUI has been initialized and should be where you add all components.
     */
    @Override
    protected void addComponents() {
        if(energyStorage != null) {
            // Power Bar
            components.add(new GuiComponentTextureAnimated(this, 16, 12, 176, 0,
                    16, 62, GuiComponentTextureAnimated.ANIMATION_DIRECTION.UP) {
                @Override
                protected int getCurrentProgress(int scale) {
                    return energyStorage.getEnergyStored() * scale / energyStorage.getMaxEnergyStored();
                }

                /**
                 * Used to determine if a dynamic tooltip is needed at runtime
                 *
                 * @param mouseX Mouse X Pos
                 * @param mouseY Mouse Y Pos
                 * @return A list of string to display
                 */
                @Nullable
                @Override
                public List<String> getDynamicToolTip(int mouseX, int mouseY) {
                    List<String> toolTip = new ArrayList<>();
                    EnergyUtils.addToolTipInfo(energyStorage.getCapability(CapabilityEnergy.ENERGY, null),
                            toolTip, energyStorage.energyStorage.getMaxInsert(), energyStorage.energyStorage.getMaxExtract());
                    return toolTip;
                }
            });
        }
    }
}