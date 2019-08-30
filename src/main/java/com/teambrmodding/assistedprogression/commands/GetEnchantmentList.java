package com.teambrmodding.assistedprogression.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;

import static net.minecraft.command.Commands.literal;

public class GetEnchantmentList {

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

    /*public GetEnchantmentList() {
        dispatcher.register(
                literal("listEnchants")
                        .requires(cs -> cs.hasPermissionLevel(0))
                        .executes(ctx -> {
                            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                                ResourceLocation enchant = enchantment.getRegistryName();
                                AssistedProgression.LOGGER.warn(enchant.getNamespace() + "-" + enchant.getPath());
                            });

    }*/
}
