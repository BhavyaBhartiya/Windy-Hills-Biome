package net.enigma.windyhills.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class Shrub extends Feature<NoneFeatureConfiguration> {
    private static final int MIN_RADIUS = 1;
    private static final int MAX_RADIUS = 2;
    private static final float STEM_CHANCE = 0.70F;
    private static final float LEAF_SKIP_CHANCE = 0.18F;

    public Shrub(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, context.origin());

        if (!level.isEmptyBlock(origin)){
            return false;
        }

        if (!isSuitableGround(level, origin.below())){
            return false;
        }

        if (random.nextFloat() < STEM_CHANCE) {
            level.setBlock(origin, Blocks.OAK_LOG.defaultBlockState(), 2);

            origin = origin.above();
        }

        int radius = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);

        generateCanopy(level, origin, radius, random);
        generateSideLeaves(level, origin, random);

        return true;
    }

    private void generateCanopy(WorldGenLevel level, BlockPos center, int radius, RandomSource random) {
        for (int y = -1; y <= 1; y++) {
            int layerRadius = Math.max(1, radius - Math.abs(y));
            int radiusSq = layerRadius * layerRadius;

            for (int x = -layerRadius; x <= layerRadius; x++) {
                for (int z = -layerRadius; z <= layerRadius; z++) {
                    int distanceSq = x * x + z * z;

                    if (distanceSq > radiusSq + 1){
                        continue;
                    }

                    if (random.nextFloat() < LEAF_SKIP_CHANCE){
                        continue;
                    }

                    BlockPos leaf = center.offset(x, y, z);

                    if (!level.isEmptyBlock(leaf)){
                        continue;
                    }

                    level.setBlock(leaf, Blocks.OAK_LEAVES.defaultBlockState(), 2);
                }
            }
        }
    }

    private void generateSideLeaves(WorldGenLevel level, BlockPos center, RandomSource random) {
        int count = random.nextInt(3);

        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(3) - 1;
            int dz = random.nextInt(3) - 1;

            if (dx == 0 && dz == 0){
                continue;
            }

            BlockPos pos = center.offset(dx, 0, dz);

            if (!level.isEmptyBlock(pos)){
                continue;
            }

            level.setBlock(pos, Blocks.OAK_LEAVES.defaultBlockState(), 2);
        }
    }

    private boolean isSuitableGround(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.STONE) || level.getBlockState(pos).is(Blocks.COBBLESTONE) || level.getBlockState(pos).is(Blocks.MOSSY_COBBLESTONE) || level.getBlockState(pos).is(Blocks.ANDESITE) || level.getBlockState(pos).is(Blocks.GRASS_BLOCK) || level.getBlockState(pos).is(Blocks.COARSE_DIRT);
    }
}