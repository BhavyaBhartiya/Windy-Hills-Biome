package net.enigma.windyhills.biome;

import net.enigma.windyhills.worldgen.feature.WindyPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class WindyHighlandsBiome {
    private WindyHighlandsBiome() {}

    public static Biome create(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();

        BiomeDefaultFeatures.farmAnimals(mobs);
        BiomeDefaultFeatures.commonSpawns(mobs);

        mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityTypes.LLAMA, 4, 6));
        mobs.addSpawn(MobCategory.CREATURE, 6, new MobSpawnSettings.SpawnerData(EntityTypes.GOAT, 2, 4));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        OverworldBiomes.globalOverworldGeneration(generation);

        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        BiomeDefaultFeatures.addExtraEmeralds(generation);
        BiomeDefaultFeatures.addInfestedStone(generation);
        BiomeDefaultFeatures.addDefaultGrass(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatures.getOrThrow(WindyPlacedFeatures.ROCK));
        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatures.getOrThrow(WindyPlacedFeatures.ARCH));
        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatures.getOrThrow(WindyPlacedFeatures.MUSHROOM_ROCK));
        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatures.getOrThrow(WindyPlacedFeatures.BOULDER));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getOrThrow(WindyPlacedFeatures.GROUND_VARIATION));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getOrThrow(WindyPlacedFeatures.WINDY_GRASS));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getOrThrow(WindyPlacedFeatures.ROCK_ECOLOGY));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getOrThrow(WindyPlacedFeatures.SHRUB));

        return OverworldBiomes
                .baseBiome(0.25F, 0.35F)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_STONY_PEAKS))
                .mobSpawnSettings(mobs.build())
                .generationSettings(generation.build())
                .build();
    }
}
