package net.enigma.windyhills.worldgen.feature;

import net.enigma.windyhills.WindyHills;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class WindyConfiguredFeatures {

    private WindyConfiguredFeatures() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> ROCK = createKey("rock");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ARCH = createKey("arch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHROOM_ROCK = createKey("mushroom_rock");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BOULDER = createKey("boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GROUND_VARIATION = createKey("ground_variation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROCK_ECOLOGY = createKey("rock_ecology");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WINDY_GRASS = createKey("windy_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHRUB = createKey("shrub");

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, WindyHills.id(id));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(ROCK, new ConfiguredFeature<>(WindyFeatures.ROCK, NoneFeatureConfiguration.INSTANCE));
        context.register(ARCH, new ConfiguredFeature<>(WindyFeatures.ARCH, NoneFeatureConfiguration.INSTANCE));
        context.register(MUSHROOM_ROCK, new ConfiguredFeature<>(WindyFeatures.MUSHROOM_ROCK, NoneFeatureConfiguration.INSTANCE));
        context.register(BOULDER, new ConfiguredFeature<>(WindyFeatures.BOULDER, NoneFeatureConfiguration.INSTANCE));
        context.register(GROUND_VARIATION, new ConfiguredFeature<>(WindyFeatures.GROUND_VARIATION, NoneFeatureConfiguration.INSTANCE));
        context.register(ROCK_ECOLOGY, new ConfiguredFeature<>(WindyFeatures.ROCK_ECOLOGY, NoneFeatureConfiguration.INSTANCE));
        context.register(WINDY_GRASS, new ConfiguredFeature<>(WindyFeatures.WINDY_GRASS, NoneFeatureConfiguration.INSTANCE));
        context.register(SHRUB, new ConfiguredFeature<>(WindyFeatures.SHRUB, NoneFeatureConfiguration.INSTANCE));
    }
}