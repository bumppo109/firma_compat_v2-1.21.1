package com.bumppo109.firma_compat.fluid;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public enum Potion {

    // ========================================================================
    // BREWABLE POTIONS
    // ========================================================================

    NIGHT_VISION(
            0x1F1FA1,
            effect(MobEffects.NIGHT_VISION, 3600, 0)
    ),

    NIGHT_VISION_EXT(
            0x1F1FA1,
            effect(MobEffects.NIGHT_VISION, 9600, 0)
    ),

    INVISIBILITY(
            0x7BFF7B,
            effect(MobEffects.INVISIBILITY, 3600, 0)
    ),

    INVISIBILITY_EXT(
            0x7BFF7B,
            effect(MobEffects.INVISIBILITY, 9600, 0)
    ),

    LEAPING(
            0x7CFF3F,
            effect(MobEffects.JUMP, 3600, 0)
    ),

    LEAPING_EXT(
            0x7CFF3F,
            effect(MobEffects.JUMP, 9600, 0)
    ),

    LEAPING_II(
            0x7CFF3F,
            effect(MobEffects.JUMP, 1800, 1)
    ),

    FIRE_RESISTANCE(
            0xE49A3A,
            effect(MobEffects.FIRE_RESISTANCE, 3600, 0)
    ),

    FIRE_RESISTANCE_EXT(
            0xE49A3A,
            effect(MobEffects.FIRE_RESISTANCE, 9600, 0)
    ),

    SWIFTNESS(
            0x7CAFC6,
            effect(MobEffects.MOVEMENT_SPEED, 3600, 0)
    ),

    SWIFTNESS_EXT(
            0x7CAFC6,
            effect(MobEffects.MOVEMENT_SPEED, 9600, 0)
    ),

    SWIFTNESS_II(
            0x7CAFC6,
            effect(MobEffects.MOVEMENT_SPEED, 1800, 1)
    ),

    SLOWNESS(
            0x5A6C81,
            effect(MobEffects.MOVEMENT_SLOWDOWN, 1800, 0)
    ),

    SLOWNESS_EXT(
            0x5A6C81,
            effect(MobEffects.MOVEMENT_SLOWDOWN, 4800, 0)
    ),

    SLOWNESS_II(
            0x5A6C81,
            effect(MobEffects.MOVEMENT_SLOWDOWN, 400, 3)
    ),

    TURTLE_MASTER(
            0x7E6DA8,
            effect(MobEffects.MOVEMENT_SLOWDOWN, 400, 3),
            effect(MobEffects.DAMAGE_RESISTANCE, 400, 2)
    ),

    TURTLE_MASTER_EXT(
            0x7E6DA8,
            effect(MobEffects.MOVEMENT_SLOWDOWN, 800, 3),
            effect(MobEffects.DAMAGE_RESISTANCE, 800, 2)
    ),

    TURTLE_MASTER_II(
            0x7E6DA8,
            effect(MobEffects.MOVEMENT_SLOWDOWN, 400, 5),
            effect(MobEffects.DAMAGE_RESISTANCE, 400, 2)
    ),

    WATER_BREATHING(
            0x2E7AC4,
            effect(MobEffects.WATER_BREATHING, 3600, 0)
    ),

    WATER_BREATHING_EXT(
            0x2E7AC4,
            effect(MobEffects.WATER_BREATHING, 9600, 0)
    ),

    HEALING(
            0xF82423,
            effect(MobEffects.HEAL, 1, 0)
    ),

    HEALING_II(
            0xF82423,
            effect(MobEffects.HEAL, 1, 1)
    ),

    HARMING(
            0x430A09,
            effect(MobEffects.HARM, 1, 0)
    ),

    HARMING_II(
            0x430A09,
            effect(MobEffects.HARM, 1, 1)
    ),

    POISON(
            0x4E9331,
            effect(MobEffects.POISON, 900, 0)
    ),

    POISON_EXT(
            0x4E9331,
            effect(MobEffects.POISON, 1800, 0)
    ),

    POISON_II(
            0x4E9331,
            effect(MobEffects.POISON, 432, 1)
    ),

    REGENERATION(
            0xCD5CAB,
            effect(MobEffects.REGENERATION, 900, 0)
    ),

    REGENERATION_EXT(
            0xCD5CAB,
            effect(MobEffects.REGENERATION, 1800, 0)
    ),

    REGENERATION_II(
            0xCD5CAB,
            effect(MobEffects.REGENERATION, 450, 1)
    ),

    STRENGTH(
            0x932423,
            effect(MobEffects.DAMAGE_BOOST, 3600, 0)
    ),

    STRENGTH_EXT(
            0x932423,
            effect(MobEffects.DAMAGE_BOOST, 9600, 0)
    ),

    STRENGTH_II(
            0x932423,
            effect(MobEffects.DAMAGE_BOOST, 1800, 1)
    ),

    WEAKNESS(
            0x484D48,
            effect(MobEffects.WEAKNESS, 1800, 0)
    ),

    WEAKNESS_EXT(
            0x484D48,
            effect(MobEffects.WEAKNESS, 4800, 0)
    ),

    SLOW_FALLING(
            0xCEB6D8,
            effect(MobEffects.SLOW_FALLING, 1800, 0)
    ),

    SLOW_FALLING_EXT(
            0xCEB6D8,
            effect(MobEffects.SLOW_FALLING, 4800, 0)
    ),

    // ========================================================================
    // SPECIAL
    // ========================================================================

    LUCK(
            0x339900,
            effect(MobEffects.LUCK, 6000, 0)
    ),

    WIND_CHARGED(
            0x9FC3DE,
            effect(MobEffects.WIND_CHARGED, 3600, 0)
    ),

    WEAVING(
            0x7A6C6C,
            effect(MobEffects.WEAVING, 3600, 0)
    ),

    OOZING(
            0x8BFF8B,
            effect(MobEffects.OOZING, 3600, 0)
    ),

    INFESTED(
            0x8C8C8C,
            effect(MobEffects.INFESTED, 3600, 0)
    );

    // ========================================================================
    // DATA
    // ========================================================================

    private final String serializedName;
    private final int color;
    private final EffectData[] effects;

    public record EffectData(
            Holder<MobEffect> effect,
            int duration,
            int level
    ) {
        public MobEffectInstance createInstance() {
            return new MobEffectInstance(
                    effect,
                    duration,
                    level
            );
        }
    }

    private static EffectData effect(
            Holder<MobEffect> effect,
            int duration,
            int level
    ) {
        return new EffectData(effect, duration, level);
    }

    Potion(
            int color,
            EffectData... effects
    ) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.color = color;
        this.effects = effects;
    }

    public String potionNamespace() {
        return "minecraft";
    }

    public String serializedName() {
        return serializedName;
    }

    public int color() {
        return color;
    }

    public EffectData[] effects() {
        return effects;
    }

    public boolean canMakeSplash() {
        return true;
    }

    public boolean canMakeLingering() {
        return true;
    }

    /**
     * Creates fresh MobEffectInstances for this potion.
     *
     * A new instance is returned each time so that applying a potion
     * never mutates the enum's underlying definition.
     */
    public List<MobEffectInstance> effectInstances() {
        List<MobEffectInstance> result = new ArrayList<>(effects.length);

        for (EffectData effect : effects) {
            result.add(new MobEffectInstance(
                    effect.effect(),
                    effect.duration(),
                    effect.level()
            ));
        }

        return result;
    }

    /**
     * Returns the potion produced by vanilla fermented-spider-eye
     * corruption, or null when no corruption exists.
     */
    public Potion corrupted() {
        return switch (this) {
            case NIGHT_VISION -> INVISIBILITY;
            case NIGHT_VISION_EXT, WATER_BREATHING_EXT -> INVISIBILITY_EXT;

            case INVISIBILITY -> NIGHT_VISION;
            case INVISIBILITY_EXT -> NIGHT_VISION_EXT;

            case LEAPING -> SLOWNESS;
            case LEAPING_EXT,
                 FIRE_RESISTANCE_EXT,
                 SWIFTNESS_EXT -> SLOWNESS_EXT;

            case LEAPING_II,
                 SWIFTNESS_II -> SLOWNESS_II;

            case SWIFTNESS -> SLOWNESS;

            case FIRE_RESISTANCE -> SLOWNESS;

            case WATER_BREATHING -> INVISIBILITY;

            case HEALING -> HARMING;
            case HEALING_II,
                 POISON_II -> HARMING_II;

            case POISON,
                 POISON_EXT -> HARMING;

            case STRENGTH -> WEAKNESS;
            case STRENGTH_EXT -> WEAKNESS_EXT;

            default -> null;
        };
    }

    public boolean canBeCorrupted() {
        return corrupted() != null;
    }
}
