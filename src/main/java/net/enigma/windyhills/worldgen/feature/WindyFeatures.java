package net.enigma.windyhills.worldgen.feature;

import net.enigma.windyhills.WindyHills;
import net.enigma.windyhills.worldgen.feature.custom.GroundVariation;
import net.enigma.windyhills.worldgen.feature.custom.RockEcology;
import net.enigma.windyhills.worldgen.feature.custom.RockFeature;
import net.enigma.windyhills.worldgen.feature.custom.WindyGrass;
import net.enigma.windyhills.worldgen.feature.custom.shape.ArchShape;
import net.enigma.windyhills.worldgen.feature.custom.shape.BoulderShape;
import net.enigma.windyhills.worldgen.feature.custom.shape.InselbergShape;
import net.enigma.windyhills.worldgen.feature.custom.shape.MushroomRockShape;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class WindyFeatures {
    private WindyFeatures() {}

    public static final Feature<NoneFeatureConfiguration> ROCK = register("rock", new RockFeature(NoneFeatureConfiguration.CODEC, random -> new InselbergShape(5 + random.nextInt(10), 8 + random.nextInt(18), random)));
    public static final Feature<NoneFeatureConfiguration> MUSHROOM_ROCK = register("mushroom_rock", new RockFeature(NoneFeatureConfiguration.CODEC, MushroomRockShape::new));
    public static final Feature<NoneFeatureConfiguration> ARCH = register("arch", new RockFeature(NoneFeatureConfiguration.CODEC, ArchShape::new));
    public static final Feature<NoneFeatureConfiguration> BOULDER = register("boulder", new RockFeature(NoneFeatureConfiguration.CODEC, BoulderShape::new));
    public static final Feature<NoneFeatureConfiguration> GROUND_VARIATION = register("ground_variation", new GroundVariation(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> ROCK_ECOLOGY = register("rock_ecology", new RockEcology(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> WINDY_GRASS = register("windy_grass", new WindyGrass(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> SHRUB = register("shrub", new Shrub(NoneFeatureConfiguration.CODEC));

    private static <T extends Feature<?>> T register(String id, T feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(WindyHills.MOD_ID, id), feature);
    }

    public static void init() {}
}