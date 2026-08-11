package net.enigma.windyhills.worldgen.feature.custom.shape;

import net.minecraft.util.RandomSource;

@FunctionalInterface
public interface RockShapeFactory{
    RockShape create(RandomSource random);
}

