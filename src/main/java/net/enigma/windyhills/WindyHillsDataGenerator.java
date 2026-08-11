package net.enigma.windyhills;

import net.enigma.windyhills.biome.WindyBiomes;
import net.enigma.windyhills.datagen.provider.ModSoundsProvider;
import net.enigma.windyhills.datagen.provider.WindyDynamicRegistryProvider;
import net.enigma.windyhills.worldgen.feature.WindyConfiguredFeatures;
import net.enigma.windyhills.worldgen.feature.WindyPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public final class WindyHillsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(WindyDynamicRegistryProvider::new);
		pack.addProvider(ModSoundsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder
				.add(Registries.CONFIGURED_FEATURE, WindyConfiguredFeatures::bootstrap)
				.add(Registries.PLACED_FEATURE, WindyPlacedFeatures::bootstrap)
				.add(Registries.BIOME, WindyBiomes::bootstrap);
	}
}