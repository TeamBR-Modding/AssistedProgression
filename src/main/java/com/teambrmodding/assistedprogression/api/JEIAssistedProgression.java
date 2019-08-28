package com.teambrmodding.assistedprogression.api;

import com.teambrmodding.assistedprogression.lib.Reference;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.util.ResourceLocation;

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
@JeiPlugin
public class JEIAssistedProgression implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Reference.MOD_ID, "jei");
    }
}
