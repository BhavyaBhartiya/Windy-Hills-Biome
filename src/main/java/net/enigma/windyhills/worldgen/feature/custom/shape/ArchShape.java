package net.enigma.windyhills.worldgen.feature.custom.shape;

import net.minecraft.util.RandomSource;

public class ArchShape implements RockShape {
    private static final int MIN_LENGTH = 14;
    private static final int MAX_LENGTH = 25;
    private static final int MIN_HEIGHT = 8;
    private static final int MAX_HEIGHT = 17;
    private final int length;
    private final int height;
    private final int thickness;
    private final int embedDepth;
    private final double cos;
    private final double sin;
    private final double leftScale;
    private final double rightScale;
    private final double archPower;
    private final double bridgeThickness;

    public ArchShape(RandomSource random) {
        length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
        height = MIN_HEIGHT + random.nextInt(MAX_HEIGHT - MIN_HEIGHT + 1);
        thickness = 2 + random.nextInt(2);
        embedDepth = 5;
        double angle = random.nextDouble() * Math.PI * 2.0;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        leftScale = 0.85 + random.nextDouble() * 0.45;
        rightScale = 0.85 + random.nextDouble() * 0.45;
        archPower = 0.65 + random.nextDouble() * 0.35;
        bridgeThickness = 0.80 + random.nextDouble() * 0.35;
    }

    @Override
    public boolean contains(int x, int y, int z) {
        double rx = x * cos + z * sin;
        double rz = -x * sin + z * cos;
        int samples = length * 5;

        for (int i = 0; i <= samples; i++) {
            double t = i / (double) samples;
            double cx = (t - 0.5) * length;
            double curve = Math.pow(Math.sin(t * Math.PI), archPower);
            double cy = curve * height;
            double end = Math.pow(Math.abs(t - 0.5) * 2.0, 1.8);
            double scale = (t < 0.5) ? leftScale : rightScale;
            double localRadius = (thickness + end * 3.0 + bridgeThickness * (1.0 - end)) * scale;
            double dx = rx - cx;
            double dy = y - cy;
            double distance = dx * dx + dy * dy + rz * rz;

            if (distance > localRadius * localRadius)
                continue;

            if (end > 0.70 && y <= cy)
                return true;

            if (end < 0.35 && dy < 0 && Math.sin(rx * 0.42) + Math.cos(rz * 0.39) > 1.45)
                continue;

            return true;
        }

        return false;
    }

    @Override
    public int getRadius() {
        return length / 2 + 6;
    }

    @Override
    public int getHeight() {
        return height + 4;
    }

    @Override
    public int getEmbedDepth() {
        return embedDepth;
    }
}