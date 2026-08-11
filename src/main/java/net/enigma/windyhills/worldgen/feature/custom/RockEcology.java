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

public class RockEcology extends Feature<NoneFeatureConfiguration> {
    private static final Block[] FLOWERS = {Blocks.DANDELION, Blocks.AZURE_BLUET, Blocks.OXEYE_DAISY};
    private static final int MIN_CLUSTERS = 2;
    private static final int MAX_CLUSTERS = 5;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_RADIUS = 6;

    public RockEcology(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        int clusters = MIN_CLUSTERS + random.nextInt(MAX_CLUSTERS - MIN_CLUSTERS + 1);

        for (int c = 0; c < clusters; c++) {
            int centerX = random.nextInt(25) - 12;
            int centerZ = random.nextInt(25) - 12;
            int radius = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
            int radiusSq = radius * radius;

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distanceSq = dx * dx + dz * dz;

                    if (distanceSq > radiusSq) {
                        continue;
                    }

                    double distance = Math.sqrt(distanceSq);
                    double density = 1.0 - distance / radius;
                    density *= 0.75 + random.nextDouble() * 0.25;

                    if (random.nextDouble() > density) {
                        continue;
                    }

                    BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(centerX + dx, 0, centerZ + dz));

                    if (!canPlace(level, surface)) {
                        continue;
                    }

                    placeVegetation(level, surface, random, density);
                }
            }
        }

        return true;
    }

    private static boolean canPlace(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.isEmptyBlock(pos);
    }

    private static void placeVegetation(WorldGenLevel level, BlockPos pos, RandomSource random, double density) {
        float roll = random.nextInt();
        if (density > 75 && roll > 95) {
            level.setBlock(pos.below(), Blocks.MOSS_BLOCK.defaultBlockState(), 2);

            return;
        }

        if (roll < 45) {
            level.setBlock(pos, Blocks.TALL_GRASS.defaultBlockState(), 2);

            return;
        }

        if (roll < 75F) {
            level.setBlock(pos, Blocks.FERN.defaultBlockState(), 2);

            return;
        }

        level.setBlock(pos, FLOWERS[random.nextInt(FLOWERS.length)].defaultBlockState(), 2);
    }
}