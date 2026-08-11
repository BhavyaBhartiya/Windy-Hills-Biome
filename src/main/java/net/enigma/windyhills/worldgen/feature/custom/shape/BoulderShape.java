package net.enigma.windyhills.worldgen.feature.custom.shape;

import net.minecraft.util.RandomSource;

public class BoulderShape implements RockShape {
    private static final int MIN_RADIUS = 1;
    private static final int MAX_RADIUS = 4;
    private static final double MIN_SQUASH = 0.80;
    private static final double MAX_SQUASH = 1.25;
    private static final double ROUGHNESS = 0.05;
    private final int radius;
    private final int height;
    private final int embedDepth;
    private final double scaleX;
    private final double scaleY;
    private final double scaleZ;
    private final double seedA;
    private final double seedB;
    private final double seedC;

    public BoulderShape(RandomSource random) {
        radius = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
        height = radius + random.nextInt(2);
        embedDepth = Math.max(1, radius / 2);
        scaleX = MIN_SQUASH + random.nextDouble() * (MAX_SQUASH - MIN_SQUASH);
        scaleY = 0.70 + random.nextDouble() * 0.35;
        scaleZ = MIN_SQUASH + random.nextDouble() * (MAX_SQUASH - MIN_SQUASH);
        seedA = random.nextDouble() * Math.PI * 2.0;
        seedB = random.nextDouble() * Math.PI * 2.0;
        seedC = random.nextDouble() * Math.PI * 2.0;
    }

    @Override
    public boolean contains(int x, int y, int z) {
        double dx = x / (radius * scaleX);
        double dy = y / (height * scaleY);
        double dz = z / (radius * scaleZ);
        double distance = dx * dx + dy * dy + dz * dz;
        double surfaceNoise = Math.sin(x * 0.72 + seedA) + Math.cos(z * 0.64 + seedB) + Math.sin((x + z) * 0.41 + seedC) + Math.cos(y * 0.93 + seedA);
        double threshold = 1.0 + surfaceNoise * ROUGHNESS;

        return distance <= threshold;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getEmbedDepth() {
        return embedDepth;
    }
}