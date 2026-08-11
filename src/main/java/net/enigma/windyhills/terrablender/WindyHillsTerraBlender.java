package net.enigma.windyhills.terrablender;

import net.enigma.windyhills.WindyHills;
import net.enigma.windyhills.biome.WindyRegion;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class WindyHillsTerraBlender implements TerraBlenderApi{
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new WindyRegion());
        WindyHills.LOGGER.info("TerraBlender Initialized");
    }
}
