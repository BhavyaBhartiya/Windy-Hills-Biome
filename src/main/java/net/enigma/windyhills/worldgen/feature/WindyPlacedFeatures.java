package net.enigma.windyhills.worldgen.feature;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.enigma.windyhills.WindyHills;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public final class WindyPlacedFeatures {
    private WindyPlacedFeatures() {}

    public static final ResourceKey<PlacedFeature> ROCK_ECOLOGY = createKey("rock_ecology");
    public static final ResourceKey<PlacedFeature> SHRUB = createKey("shrub");
    public static final ResourceKey<PlacedFeature> ROCK = createKey("rock");
    public static final ResourceKey<PlacedFeature> ARCH = createKey("arch");
    public static final ResourceKey<PlacedFeature> MUSHROOM_ROCK = createKey("mushroom_rock");
    public static final ResourceKey<PlacedFeature> BOULDER = createKey("boulder");
    public static final ResourceKey<PlacedFeature> GROUND_VARIATION = createKey("ground_variation");
    public static final ResourceKey<PlacedFeature> WINDY_GRASS = createKey("windy_grass");

    private static ResourceKey<PlacedFeature> createKey(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, WindyHills.id(id));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, ROCK_ECOLOGY, configured.getOrThrow(WindyConfiguredFeatures.ROCK_ECOLOGY), RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, SHRUB, configured.getOrThrow(WindyConfiguredFeatures.SHRUB), CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, ROCK, configured.getOrThrow(WindyConfiguredFeatures.ROCK), RarityFilter.onAverageOnceEvery(14), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, ARCH, configured.getOrThrow(WindyConfiguredFeatures.ARCH), RarityFilter.onAverageOnceEvery(36), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, MUSHROOM_ROCK, configured.getOrThrow(WindyConfiguredFeatures.MUSHROOM_ROCK), RarityFilter.onAverageOnceEvery(28), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, BOULDER, configured.getOrThrow(WindyConfiguredFeatures.BOULDER), RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, GROUND_VARIATION, configured.getOrThrow(WindyConfiguredFeatures.GROUND_VARIATION), CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        PlacementUtils.register(context, WINDY_GRASS, configured.getOrThrow(WindyConfiguredFeatures.WINDY_GRASS), CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }
}