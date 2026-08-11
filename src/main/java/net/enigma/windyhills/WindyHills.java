package net.enigma.windyhills;

import net.enigma.windyhills.biome.WindyBiomes;
import net.enigma.windyhills.sounds.ModSounds;
import net.enigma.windyhills.world.wind.WindEffects;
import net.enigma.windyhills.worldgen.feature.WindyFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WindyHills implements ModInitializer {
	public static final String MOD_ID = "windyhills";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		WindyBiomes.initialize();
		WindyFeatures.init();
		ModSounds.registerSounds();

		ServerTickEvents.END_LEVEL_TICK.register(WindEffects::tick);

		LOGGER.info("{} loaded successfully.", MOD_ID);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}