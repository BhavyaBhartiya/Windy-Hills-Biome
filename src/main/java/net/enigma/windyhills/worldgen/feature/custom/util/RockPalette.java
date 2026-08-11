package net.enigma.windyhills.worldgen.feature.custom.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class RockPalette {
    private RockPalette() {}

    public static BlockState pick(BlockPos pos) {
        double value = (Math.random()*10+Math.sin(pos.getX()*0.08)+Math.sin(-pos.getY()*0.08)+Math.cos(pos.getZ()*0.08))*10;

        if (value > 129 || (value > 96 && value<100)) {
            return Blocks.COARSE_DIRT.defaultBlockState();
        }
        if (value > 127 || (value > 88 && value<100)) {
            return Blocks.COBBLESTONE.defaultBlockState();
        }
        if (value > 123 || (value > 76 && value<100)) {
            return Blocks.GRAVEL.defaultBlockState();
        }
        if (value > 121 || (value > 72 && value<100)) {
            return Blocks.DEEPSLATE.defaultBlockState();
        }
        if (value > 116 || (value > 56 && value<100)) {
            return Blocks.ANDESITE.defaultBlockState();
        }

        return Blocks.STONE.defaultBlockState();
    }
}