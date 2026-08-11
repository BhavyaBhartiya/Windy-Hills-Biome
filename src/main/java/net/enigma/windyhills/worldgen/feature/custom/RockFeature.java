package net.enigma.windyhills.worldgen.feature.custom;

import com.mojang.serialization.Codec;
import net.enigma.windyhills.worldgen.feature.custom.shape.BoulderShape;
import net.enigma.windyhills.worldgen.feature.custom.shape.RockShape;
import net.enigma.windyhills.worldgen.feature.custom.shape.RockShapeFactory;
import net.enigma.windyhills.worldgen.feature.custom.util.RockBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RockFeature extends Feature<NoneFeatureConfiguration> {
    private final RockShapeFactory shapeFactory;

    public RockFeature(Codec<NoneFeatureConfiguration> codec, RockShapeFactory shapeFactory) {
        super(codec);
        this.shapeFactory = shapeFactory;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, context.origin());

        if (!level.getFluidState(origin).isEmpty()) {
            return false;
        }

        if (hasStableGround(level, origin)) {
            return false;
        }

        RockShape shape = shapeFactory.create(random);

        if (shape instanceof BoulderShape) {
            return generateBoulderField(level, origin, random);
        }

        RockBuilder.build(level, origin.below(shape.getEmbedDepth()), shape, random);

        return true;
    }

    private boolean generateBoulderField(WorldGenLevel level, BlockPos center, RandomSource random) {
        int count = 3 + random.nextInt(4);

        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(13) - 6;
            int dz = random.nextInt(13) - 6;
            BlockPos pos = center.offset(dx, 0, dz);
            pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);

            if (!level.getFluidState(pos).isEmpty()) {
                continue;
            }

            if (hasStableGround(level, pos)) {
                continue;
            }

            RockShape rock = shapeFactory.create(random);

            RockBuilder.build(level, pos.below(rock.getEmbedDepth()), rock, random);
        }

        return true;
    }

    private boolean hasStableGround(WorldGenLevel level, BlockPos pos) {
        int center = pos.getY();
        int north = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.north()).getY();
        int south = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.south()).getY();
        int east = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.east()).getY();
        int west = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.west()).getY();

        return Math.abs(center - north) > 3 || Math.abs(center - south) > 3 || Math.abs(center - east) > 3 || Math.abs(center - west) > 3;
    }
}