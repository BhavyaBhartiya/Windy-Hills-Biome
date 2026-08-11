package net.enigma.windyhills.sounds;

import net.enigma.windyhills.WindyHills;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public  static final SoundEvent WIND = registerSoundEvents("wind");

    private static SoundEvent registerSoundEvents(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(WindyHills.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds(){
        WindyHills.LOGGER.info("Registering sounds for " +  WindyHills.MOD_ID);
    }
}
