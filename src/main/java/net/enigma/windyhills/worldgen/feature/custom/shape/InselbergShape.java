package net.enigma.windyhills.worldgen.feature.custom.shape;

import net.minecraft.util.RandomSource;

public class InselbergShape implements RockShape {
    private static final double MIN_RADIUS = 1.5;
    private static final double MAX_WOBBLE = 2.0;
    private final int radius;
    private final int height;
    private final double[] layerRadius;
    private final int[] xOffset;
    private final int[] zOffset;
    private final double stretchX;
    private final double stretchZ;

    public InselbergShape(int radius, int height, RandomSource random) {
        this.radius = radius;
        this.height = height;
        layerRadius = new double[height];
        xOffset = new int[height];
        zOffset = new int[height];
        stretchX = 0.85 + random.nextDouble() * 0.35;
        stretchZ = 0.85 + random.nextDouble() * 0.35;
        double profilePower = 0.70 + random.nextDouble() * 0.40;
        double wobbleX = 0.0;
        double wobbleZ = 0.0;

        for (int y = 0; y < height; y++) {
            double t = (double) y / (height - 1);
            double profile = Math.pow(Math.sin(t * Math.PI), profilePower);

            if (t > 0.82) {
                profile = Math.max(profile, 0.68);
            }

            profile += (random.nextDouble() - 0.5) * 0.10;
            layerRadius[y] = Math.max(MIN_RADIUS, profile * radius);
            wobbleX += (random.nextDouble() - 0.5) * 0.45;
            wobbleZ += (random.nextDouble() - 0.5) * 0.45;
            wobbleX = clamp(wobbleX);
            wobbleZ = clamp(wobbleZ);
            xOffset[y] = (int) Math.round(wobbleX);
            zOffset[y] = (int) Math.round(wobbleZ);
        }
    }

    @Override
    public boolean contains(int x, int y, int z) {
        if (y < 0 || y >= height) {
            return false;
        }

        double dx = (x - xOffset[y]) / stretchX;
        double dz = (z - zOffset[y]) / stretchZ;
        double r = layerRadius[y];
        double noise = Math.sin(dx * 0.35) + Math.cos(dz * 0.29);

        return dx * dx + dz * dz <= r * r + noise * 0.08 * r;
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
        return height / 3;
    }

    private static double clamp(double value) {
        return Math.clamp(value, -2.0, InselbergShape.MAX_WOBBLE);
    }
}