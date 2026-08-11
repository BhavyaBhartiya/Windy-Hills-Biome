package net.enigma.windyhills.worldgen.feature.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WindyGrass extends Feature<NoneFeatureConfiguration> {
    private static final Block[] FLOWERS = {Blocks.DANDELION, Blocks.AZURE_BLUET, Blocks.OXEYE_DAISY};
    private static final int MIN_PATCHES = 3;
    private static final int MAX_PATCHES = 5;
    private static final int MIN_RADIUS = 2;
    private static final int MAX_RADIUS = 4;

    public WindyGrass(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        int patches = MIN_PATCHES + random.nextInt(MAX_PATCHES - MIN_PATCHES + 1);

        for (int p = 0; p < patches; p++) {
            int centerX = random.nextInt(25) - 12;
            int centerZ = random.nextInt(25) - 12;
            int radius = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
            int radiusSq = radius * radius;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distSq = dx * dx + dz * dz;

                    if (distSq > radiusSq) {
                        continue;
                    }

                    double density = 1.0 - Math.sqrt(distSq) / radius;
                    density *= 0.80 + random.nextDouble() * 0.20;

                    if (random.nextDouble() > density) {
                        continue;
                    }

                    BlockPos pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(centerX + dx, 0, centerZ + dz));

                    if (!canPlace(level, pos)) {
                        continue;
                    }

                    placePlant(level, pos, random);
                }
            }
        }

        return true;
    }

    private static boolean canPlace(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.isEmptyBlock(pos);
    }

    private static void placePlant(WorldGenLevel level, BlockPos pos, RandomSource random) {
        float roll = random.nextInt(100);

        if (70<roll && roll< 90) {
            level.setBlock(pos, Blocks.SHORT_GRASS.defaultBlockState(), 2);
        } else if (roll < 70) {
            level.setBlock(pos, Blocks.TALL_GRASS.defaultBlockState(), 2);
        } else {
            level.setBlock(pos, FLOWERS[random.nextInt(FLOWERS.length)].defaultBlockState(), 2);
        }
    }
}