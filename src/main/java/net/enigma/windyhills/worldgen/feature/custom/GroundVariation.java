package net.enigma.windyhills.worldgen.feature.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GroundVariation extends Feature<NoneFeatureConfiguration> {
    private static final int MIN_RADIUS = 3;
    private static final int MAX_RADIUS = 6;

    public GroundVariation(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int radius = random.nextInt(MAX_RADIUS - MIN_RADIUS + 1) + MIN_RADIUS;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distance = Math.sqrt(x * x + z * z);

                if (distance > radius) {
                    continue;
                }

                double falloff = 1.0 - (distance / radius);

                if (random.nextDouble() > falloff) {
                    continue;
                }

                BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(x, 0, z));
                BlockPos ground = surface.below();
                BlockState current = level.getBlockState(ground);

                if (!isReplaceable(current)) {
                    continue;
                }

                level.setBlock(ground, chooseBlock(random), 2);
            }
        }

        return true;
    }

    private static boolean isReplaceable(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT);
    }

    private static BlockState chooseBlock(RandomSource random) {
        int roll = random.nextInt(100);

        if (roll < 70) {
            return Blocks.STONE.defaultBlockState();
        }

        if (roll < 72) {
            return Blocks.IRON_ORE.defaultBlockState();
        }

        if (roll < 82) {
            return Blocks.ANDESITE.defaultBlockState();
        }

        if (roll < 85) {
            return Blocks.COARSE_DIRT.defaultBlockState();
        }

        if (roll < 92) {
            return Blocks.COBBLESTONE.defaultBlockState();
        }

        return Blocks.GRAVEL.defaultBlockState();
    }
}