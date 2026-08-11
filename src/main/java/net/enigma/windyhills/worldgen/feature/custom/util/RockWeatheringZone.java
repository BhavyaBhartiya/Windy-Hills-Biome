package net.enigma.windyhills.worldgen.feature.custom.util;

import net.enigma.windyhills.worldgen.feature.custom.shape.RockShape;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class RockWeatheringZone {
    private static final float PERTURBATION = 2.0F;
    private static final float CENTER_STONE = 0.22F;
    private static final float CENTER_ANDESITE = 0.08F;
    private static final float CENTER_COBBLE = 0.04F;
    private static final float CENTER_GRAVEL = 0.02F;
    private static final float EDGE_STONE = 0.06F;
    private static final float EDGE_ANDESITE = 0.03F;
    private static final float EDGE_COBBLE = 0.01F;
    private static final float EDGE_GRAVEL = 0.01F;
    private static final float CENTER_EROSION_COVERAGE = 0.45F;
    private static final float EDGE_EROSION_COVERAGE = 0.12F;
    private static final int MIN_BLOBS = 2;
    private static final int MAX_BLOBS = 4;
    private static final int MIN_BLOB_SIZE = 2;
    private static final int MAX_BLOB_SIZE = 3;
    private static final int BLOB_MIN_SEPARATION = 6;
    private static final int BLOB_PLACEMENT_ATTEMPTS = 40;
    private static final float CENTER_VEG_REMOVAL = 0.72F;
    private static final float EDGE_VEG_REMOVAL = 0.28F;

    private RockWeatheringZone() {}

    public static void apply(WorldGenLevel level, FormationBounds bounds, BlockPos origin, RockShape shape, RandomSource random) {

        if (bounds.isEmpty()) {
            return;
        }

        int centerX = bounds.centerX();
        int centerZ = bounds.centerZ();
        int baseRadius = bounds.baseRadius();
        int scanTop = origin.getY() + shape.getHeight() + 8;
        int scanBottom = origin.getY() - 4;

        erodeSurfaces(level, centerX, centerZ, baseRadius, scanTop, scanBottom, random);
        reduceVegetation(level, centerX, centerZ, baseRadius, scanTop, scanBottom, random);
        scatterRockBlobs(level, centerX, centerZ, baseRadius, scanTop, scanBottom, random);
    }

    private static void erodeSurfaces(WorldGenLevel level, int centerX, int centerZ, int baseRadius, int scanTop, int scanBottom, RandomSource random) {

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = centerX - baseRadius - 3; x <= centerX + baseRadius + 3; x++) {
            for (int z = centerZ - baseRadius - 3; z <= centerZ + baseRadius + 3; z++) {
                double dx = x - centerX;
                double dz = z - centerZ;
                double distance = Math.sqrt(dx * dx + dz * dz);

                if (distance > effectiveRadius(centerX, centerZ, x, z, baseRadius)) {
                    continue;
                }

                BlockPos surface = findSurface(level, x, z, scanTop, scanBottom, mutable);

                if (surface == null) {
                    continue;
                }

                BlockState surfaceState = level.getBlockState(surface);

                if (!surfaceState.is(Blocks.GRASS_BLOCK) && !surfaceState.is(Blocks.DIRT)) {
                    continue;
                }

                float t = (float) Math.min(1.0, distance / baseRadius);
                float coverage = lerp(CENTER_EROSION_COVERAGE, EDGE_EROSION_COVERAGE, t);

                if (random.nextFloat() >= coverage) {
                    continue;
                }

                applyErosion(level, surface, t, random);
            }
        }
    }

    private static void reduceVegetation(
            WorldGenLevel level,
            int centerX,
            int centerZ,
            int baseRadius,
            int scanTop,
            int scanBottom,
            RandomSource random) {

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = centerX - baseRadius - 3; x <= centerX + baseRadius + 3; x++) {
            for (int z = centerZ - baseRadius - 3; z <= centerZ + baseRadius + 3; z++) {

                double dx = x - centerX;
                double dz = z - centerZ;
                double distance = Math.sqrt(dx * dx + dz * dz);

                if (distance > effectiveRadius(centerX, centerZ, x, z, baseRadius))
                    continue;

                BlockPos surface = findSurface(level, x, z, scanTop, scanBottom, mutable);

                if (surface == null)
                    continue;

                BlockPos above = surface.above();

                if (!level.isEmptyBlock(above))
                    removeVegetationIfPresent(level, above, distance, baseRadius, random);
            }
        }
    }

    private static void scatterRockBlobs(
            WorldGenLevel level,
            int centerX,
            int centerZ,
            int baseRadius,
            int scanTop,
            int scanBottom,
            RandomSource random) {

        int blobCount = MIN_BLOBS + random.nextInt(MAX_BLOBS - MIN_BLOBS + 1);
        List<BlockPos> placedCenters = new ArrayList<>(blobCount);

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int attempt = 0;
                attempt < BLOB_PLACEMENT_ATTEMPTS && placedCenters.size() < blobCount;
                attempt++) {

            int x = centerX + random.nextInt(baseRadius * 2 + 1) - baseRadius;
            int z = centerZ + random.nextInt(baseRadius * 2 + 1) - baseRadius;

            double dx = x - centerX;
            double dz = z - centerZ;
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > effectiveRadius(centerX, centerZ, x, z, baseRadius) * 0.65)
                continue;

            BlockPos surface = findSurface(level, x, z, scanTop, scanBottom, mutable);

            if (surface == null)
                continue;

            BlockState surfaceState = level.getBlockState(surface);

            if (!surfaceState.is(Blocks.GRASS_BLOCK)
                    && !surfaceState.is(Blocks.DIRT)
                    && !surfaceState.is(Blocks.STONE)
                    && !surfaceState.is(Blocks.COBBLESTONE)
                    && !surfaceState.is(Blocks.ANDESITE)
                    && !surfaceState.is(Blocks.GRAVEL))
                continue;

            BlockPos blobCenter = surface.above();

            if (!isSeparated(blobCenter, placedCenters))
                continue;

            placeRockBlob(level, blobCenter, random);
            placedCenters.add(blobCenter.immutable());
        }
    }

    private static double effectiveRadius(
            int centerX,
            int centerZ,
            int x,
            int z,
            int baseRadius) {

        double dx = x - centerX;
        double dz = z - centerZ;
        double angle = Math.atan2(dz, dx);

        double noise = Math.sin(angle * 4.7 + centerX * 0.017)
                + Math.cos(angle * 3.1 + centerZ * 0.013);

        return baseRadius + noise * PERTURBATION;
    }

    private static BlockPos findSurface(
            WorldGenLevel level,
            int x,
            int z,
            int scanTop,
            int scanBottom,
            BlockPos.MutableBlockPos mutable) {

        for (int y = scanTop; y >= scanBottom; y--) {
            mutable.set(x, y, z);
            BlockState state = level.getBlockState(mutable);

            if (shouldIgnore(state))
                continue;

            return mutable.immutable();
        }

        return null;
    }

    private static boolean shouldIgnore(BlockState state) {
        return state.isAir()
                || !state.getFluidState().isEmpty()
                || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.LOGS)
                || isVegetation(state);
    }

    private static void applyErosion(
            WorldGenLevel level,
            BlockPos surface,
            float t,
            RandomSource random) {

        float stone = lerp(CENTER_STONE, EDGE_STONE, t);
        float andesite = lerp(CENTER_ANDESITE, EDGE_ANDESITE, t);
        float cobble = lerp(CENTER_COBBLE, EDGE_COBBLE, t);
        float gravel = lerp(CENTER_GRAVEL, EDGE_GRAVEL, t);

        float roll = random.nextFloat();

        if (roll < stone) {
            level.setBlock(surface, Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        } else if (roll < stone + andesite) {
            level.setBlock(surface, Blocks.ANDESITE.defaultBlockState(), Block.UPDATE_CLIENTS);
        } else if (roll < stone + andesite + cobble) {
            level.setBlock(surface, Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        } else if (roll < stone + andesite + cobble + gravel) {
            level.setBlock(surface, Blocks.GRAVEL.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static void removeVegetationIfPresent(
            WorldGenLevel level,
            BlockPos pos,
            double distance,
            int baseRadius,
            RandomSource random) {

        BlockState state = level.getBlockState(pos);

        if (!isVegetation(state))
            return;

        float t = (float) Math.min(1.0, distance / baseRadius);
        float removalChance = lerp(CENTER_VEG_REMOVAL, EDGE_VEG_REMOVAL, t);

        if (random.nextFloat() < removalChance)
            level.removeBlock(pos, false);
    }

    private static boolean isVegetation(BlockState state) {
        return state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.FERN)
                || state.is(BlockTags.FLOWERS);
    }

    private static void placeRockBlob(
            WorldGenLevel level,
            BlockPos center,
            RandomSource random) {

        int radius = MIN_BLOB_SIZE
                + random.nextInt(MAX_BLOB_SIZE - MIN_BLOB_SIZE + 1);

        int radiusSq = radius * radius;

        BlockState material = pickBlobMaterial(random);

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {

                // Blob outline
                if (dx * dx + dz * dz > radiusSq)
                    continue;

                // Find ground at this column
                int y = center.getY();

                while (y > center.getY() - 6 &&
                        level.getBlockState(mutable.set(center.getX() + dx, y, center.getZ() + dz)).isAir()) {
                    y--;
                }

                // Build slightly INTO the ground
                for (int dy = -1; dy <= 1; dy++) {

                    mutable.set(
                            center.getX() + dx,
                            y + dy,
                            center.getZ() + dz);

                    BlockState state = level.getBlockState(mutable);

                    // Replace only natural surface materials
                    if (!(state.isAir()
                            || state.is(Blocks.GRASS_BLOCK)
                            || state.is(Blocks.DIRT)
                            || state.is(Blocks.SHORT_GRASS)
                            || state.is(Blocks.TALL_GRASS)
                            || state.is(Blocks.FERN)))
                        continue;

                    // Make the top irregular
                    if (dy == 1 && random.nextFloat() < 0.45F)
                        continue;

                    level.setBlock(
                            mutable,
                            material,
                            Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    private static BlockState pickBlobMaterial(RandomSource random) {
        return switch (random.nextInt(3)) {
            case 0 -> Blocks.STONE.defaultBlockState();
            case 1 -> Blocks.ANDESITE.defaultBlockState();
            default -> Blocks.COBBLESTONE.defaultBlockState();
        };
    }

    private static boolean isSeparated(
            BlockPos candidate,
            List<BlockPos> placedCenters) {

        int minSeparationSq = BLOB_MIN_SEPARATION * BLOB_MIN_SEPARATION;

        for (BlockPos placed : placedCenters) {
            int dx = candidate.getX() - placed.getX();
            int dz = candidate.getZ() - placed.getZ();

            if (dx * dx + dz * dz < minSeparationSq)
                return false;
        }

        return true;
    }

    private static float lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }
}
