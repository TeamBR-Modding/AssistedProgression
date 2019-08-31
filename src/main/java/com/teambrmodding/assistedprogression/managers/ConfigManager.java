package com.teambrmodding.assistedprogression.managers;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * This file was created for AssistedProgression
 *
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author James Rogers - Dyonovan
 * @since 08/31/19
 */

public class ConfigManager {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();


    public static class General {

        public final ForgeConfigSpec.ConfigValue<Boolean> showEnchantTooltip;

        public General(ForgeConfigSpec.Builder builder) {
            builder.push("Client");
            showEnchantTooltip = builder
                    .comment("If true, Enchantment Descriptions will be shown in item tooltips")
                    .define("enableEnchantmentTooltips", true);
            builder.pop();
        }
    }
}
