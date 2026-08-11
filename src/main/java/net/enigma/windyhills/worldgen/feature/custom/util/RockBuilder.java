package net.enigma.windyhills.worldgen.feature.custom.util;

import net.enigma.windyhills.worldgen.feature.custom.shape.MushroomRockShape;
import net.enigma.windyhills.worldgen.feature.custom.shape.RockShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class RockBuilder {
    private static final float WEATHERING_CHANCE = 0.015F;
    private static final float SHELF_CHANCE = 0.025F;
    private static final float SURFACE_VARIATION = 0.08F;
    private static final int MAX_SUPPORT_DEPTH = 24;

    private RockBuilder() {}

    public static void build(WorldGenLevel level, BlockPos origin, RockShape shape, RandomSource random) {

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        FormationBounds bounds = new FormationBounds();
        int radius = shape.getRadius();
        int height = shape.getHeight();
        int embed = shape.getEmbedDepth();

        for (int y = -embed; y <= height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    if (!shape.contains(x, y, z)) {
                        continue;
                    }

                    pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);

                    if (!level.isEmptyBlock(pos)) {
                        continue;
                    }

                    BlockState state = chooseRockState(pos, level, random);
                    level.setBlock(pos, state, Block.UPDATE_CLIENTS);
                    bounds.include(pos);

                    if (y <= embed + 1) {
                        createSupportColumn(level, pos);
                    }
                }
            }
        }

        erode(level, origin, shape, random);
        RockWeatheringZone.apply(level, bounds, origin, shape, random);
    }

    private static BlockState chooseRockState(BlockPos pos, WorldGenLevel level, RandomSource random) {

        BlockState state = RockPalette.pick(pos);

        if (!level.isEmptyBlock(pos.above())) {
            return state;
        }

        if (random.nextFloat() >= SURFACE_VARIATION) {
            return state;
        }

        if (state.is(Blocks.STONE)) {
            return Blocks.COBBLESTONE.defaultBlockState();
        }

        if (state.is(Blocks.ANDESITE)) {
            return Blocks.STONE.defaultBlockState();
        }

        return state;
    }

    private static void createSupportColumn(WorldGenLevel level, BlockPos start) {
        BlockPos.MutableBlockPos down = start.mutable().move(Direction.DOWN);
        int depth = 0;

        while (depth < MAX_SUPPORT_DEPTH && down.getY() > -64 && level.isEmptyBlock(down)) {
            level.setBlock(down, RockPalette.pick(down), Block.UPDATE_CLIENTS);
            down.move(Direction.DOWN);
            depth++;
        }
    }

    private static void erode(WorldGenLevel level, BlockPos origin, RockShape shape, RandomSource random) {

        if (shape instanceof MushroomRockShape) {
            return;
        }

        int radius = shape.getRadius() + 2;
        int height = shape.getHeight();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -shape.getEmbedDepth(); y <= height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = origin.offset(x, y, z);

                    if (level.isEmptyBlock(pos)) {
                        continue;
                    }

                    if (isInterior(level, pos)) {
                        continue;
                    }

                    float chance = random.nextFloat();

                    if (chance < WEATHERING_CHANCE) {
                        level.removeBlock(pos, false);
                    }
                    else if (chance < SHELF_CHANCE) {
                        Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                        BlockPos shelf = pos.relative(dir);

                        if (level.isEmptyBlock(shelf)) {
                            level.setBlock(shelf, RockPalette.pick(shelf), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }
    }

    private static boolean isInterior(WorldGenLevel level, BlockPos pos) {
        return !level.isEmptyBlock(pos.north()) && !level.isEmptyBlock(pos.south()) && !level.isEmptyBlock(pos.east()) && !level.isEmptyBlock(pos.west()) && !level.isEmptyBlock(pos.above());
    }
}