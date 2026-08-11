package net.enigma.windyhills.biome;

import net.enigma.windyhills.WindyHills;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class WindyBiomes {
    private WindyBiomes() {}

    public static final ResourceKey<Biome> WINDY_HIGHLANDS = ResourceKey.create(Registries.BIOME, WindyHills.id("windy_highlands"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(WINDY_HIGHLANDS, WindyHighlandsBiome.create(placedFeatures, carvers));
    }

    public static void initialize() {}
}