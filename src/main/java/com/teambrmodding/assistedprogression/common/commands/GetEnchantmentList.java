package com.teambrmodding.assistedprogression.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;

import static net.minecraft.command.Commands.literal;

/**
 * This file was created for AssistedProgression
 *
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author James Rogers - Dyonovan
 * @since 08/30/19
 */

public class GetEnchantmentList {

    /**
     * List all enchants that are registered. Outputs to console
     *
     * @param dispatcher {@link CommandDispatcher}
     */
    public GetEnchantmentList(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal("listEnchants")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(ctx -> {
                            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                                ResourceLocation enchant = enchantment.getRegistryName();
                                LogManager.getLogger(Reference.MOD_ID).warn(enchant.getNamespace() + "-" + enchant.getPath());
                            }
                            return 0;
                })
        );
    }
}
