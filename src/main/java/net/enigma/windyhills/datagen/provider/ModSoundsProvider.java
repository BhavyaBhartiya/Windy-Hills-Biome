package net.enigma.windyhills.datagen.provider;

import net.enigma.windyhills.WindyHills;
import net.enigma.windyhills.sounds.ModSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;

public class ModSoundsProvider extends FabricSoundsProvider {
    public ModSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter){
        exporter.add(ModSounds.WIND, SoundTypeBuilder.of(ModSounds.WIND).subtitle("sounds.windyhills.wind").sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(WindyHills.MOD_ID, "wind"))));
    }

    @Override
    public String getName(){
        return "Windy Hills";
    }
}
