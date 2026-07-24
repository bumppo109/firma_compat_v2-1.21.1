package com.bumppo109.firma_compat;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class FirmaCompatConfig {

    public static final ModConfigSpec COMMON_CONFIG;
    public static final Common COMMON;

    static {

        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        COMMON = new Common(builder);

        COMMON_CONFIG = builder.build();
    }

    public static class Common {

        public final ModConfigSpec.DoubleValue skeletonVariantChance;
        public final ModConfigSpec.IntValue sheepWoolRegrowthDelayTicks;
        public final ModConfigSpec.IntValue cowMilkingDelayTicks;

        public final ModConfigSpec.DoubleValue tempLerpValue;
        public final ModConfigSpec.BooleanValue doTempLerp;
        public final ModConfigSpec.DoubleValue tempScale;
        public final ModConfigSpec.DoubleValue tempShift;
        public final ModConfigSpec.DoubleValue rainScale;

        public final ModConfigSpec.DoubleValue oceanDepthScale;

        public Common(ModConfigSpec.Builder builder) {

            builder.push("entities");

            skeletonVariantChance = builder
                    .comment(
                            "Chance that a Skeleton will spawn as its variant given TFC climate is appropriate.",
                            "Default is 0.6 (60%)"
                    )
                    .defineInRange(
                            "skeletonVariantChance",
                            0.6,
                            0,
                            1.0
                    );

            sheepWoolRegrowthDelayTicks = builder
                    .comment(
                            "Ticks required after shearing before a sheep can regrow its wool.",
                            "20 ticks = 1 second. Default: 6000 ticks = 5 minutes.",
                            "Set to 0 to disable delay (vanilla behavior)."
                    )
                    .defineInRange(
                            "sheepWoolRegrowthDelayTicks",
                            6000,
                            0,
                            Integer.MAX_VALUE
                    );

            cowMilkingDelayTicks = builder
                    .comment(
                            "Ticks required after milking a cow before it can be milked again.",
                            "20 ticks = 1 second. Default: 1200 ticks = 1 minute.",
                            "Set to 0 to disable (vanilla behavior)."
                    )
                    .defineInRange(
                            "cowMilkingDelayTicks",
                            1200,
                            0,
                            Integer.MAX_VALUE
                    );

            builder.pop();

            builder.push("world");

            tempLerpValue = builder
                    .comment(
                            "Lerp value to align biome temperatures more closely to their JSON temperature value: [0 - 1]",
                            "0 to disable and only return the temperature noise value.",
                            "Default: 0.4"
                    )
                    .defineInRange(
                            "tempLerpValue",
                            0.4,
                            0.0,
                            1.0
                    );

            doTempLerp = builder
                    .comment(
                            "Whether or not to use the Lerp function to adjust climate temperature towards biome temperature value.",
                            "Default: true"
                    )
                    .define(
                            "doTempLerp",
                            true
                    );

            tempScale = builder
                    .comment(
                            "Value to scale temperature noise to climate temperature value",
                            "Default: 27.5"
                    )
                    .defineInRange(
                            "tempScale",
                            27.5,
                            -Double.MAX_VALUE,
                            Double.MAX_VALUE
                    );

            tempShift = builder
                    .comment(
                            "Value to shift temperature scale.",
                            "Default: 7.5"
                    )
                    .defineInRange(
                            "tempShift",
                            7.5,
                            -Double.MAX_VALUE,
                            Double.MAX_VALUE
                    );

            rainScale = builder
                    .comment(
                            "Value to scale vegetation noise to climate rain value",
                            "Default: 250"
                    )
                    .defineInRange(
                            "rainScale",
                            250.0,
                            0.0,
                            Double.MAX_VALUE
                    );

            oceanDepthScale = builder
                    .comment(
                            "Scale for ocean depth, no change by default",
                            "Default: 1"
                    )
                    .defineInRange(
                            "oceanDepthScale",
                            1.0,
                            0.0,
                            4.0
                    );

            builder.pop();
        }
    }

    public static void register(ModContainer container) {

        container.registerConfig(
                ModConfig.Type.COMMON,
                COMMON_CONFIG,
                "firma_compat-common.toml"
        );
    }
}