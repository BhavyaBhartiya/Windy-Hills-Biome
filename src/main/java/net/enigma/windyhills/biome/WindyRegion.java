package net.enigma.windyhills.biome;

import com.mojang.datafixers.util.Pair;
import net.enigma.windyhills.WindyHills;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public final class WindyRegion extends Region {
    private static final int REGION_WEIGHT = 5;

    public WindyRegion() {
        super(WindyHills.id("windy_highlands"), RegionType.OVERWORLD, REGION_WEIGHT);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        addModifiedVanillaOverworldBiomes(mapper, builder -> builder.replaceBiome(Biomes.WINDSWEPT_HILLS, WindyBiomes.WINDY_HIGHLANDS));
    }
}