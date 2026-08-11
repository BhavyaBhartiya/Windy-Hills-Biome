package net.enigma.windyhills.worldgen.feature.custom.shape;

import net.minecraft.util.RandomSource;

public class MushroomRockShape implements RockShape {
    private static final int MIN_STEM_RADIUS = 2;
    private static final int MAX_STEM_RADIUS = 3;
    private static final int MIN_CAP_RADIUS = 6;
    private static final int MAX_CAP_RADIUS = 8;
    private static final int MIN_STEM_HEIGHT = 8;
    private static final int MAX_STEM_HEIGHT = 14;
    private static final int MIN_CAP_HEIGHT = 3;
    private static final int MAX_CAP_HEIGHT = 5;
    private final int stemRadius;
    private final int capRadius;
    private final int stemHeight;
    private final int capHeight;
    private final int embedDepth;
    private final int capOffsetX;
    private final int capOffsetZ;
    private final double squashX;
    private final double squashZ;
    private final double roughness;

    public MushroomRockShape(RandomSource random) {
        stemRadius = MIN_STEM_RADIUS + random.nextInt(MAX_STEM_RADIUS - MIN_STEM_RADIUS + 1);
        capRadius = MIN_CAP_RADIUS + random.nextInt(MAX_CAP_RADIUS - MIN_CAP_RADIUS + 1);
        stemHeight = MIN_STEM_HEIGHT + random.nextInt(MAX_STEM_HEIGHT - MIN_STEM_HEIGHT + 1);
        capHeight = MIN_CAP_HEIGHT + random.nextInt(MAX_CAP_HEIGHT - MIN_CAP_HEIGHT + 1);
        embedDepth = 2;
        capOffsetX = random.nextInt(3) - 1;
        capOffsetZ = random.nextInt(3) - 1;
        squashX = 0.85 + random.nextDouble() * 0.35;
        squashZ = 0.85 + random.nextDouble() * 0.35;
        roughness = 0.05 + random.nextDouble() * 0.04;
    }

    @Override
    public boolean contains(int x, int y, int z) {
        if (y < 0) {
            return false;
        }

        double stemDistance = (double) x * (double) x + (double) z * (double) z;

        if (y < stemHeight) {
            double taper = 1.0 - (double) y / stemHeight * 0.18;
            double radius = stemRadius * taper;

            return stemDistance <= radius * radius;
        }

        int capY = y - stemHeight;

        if (capY >= capHeight){
            return false;
        }

        double dx = (x - capOffsetX) / squashX;
        double dz = (z - capOffsetZ) / squashZ;
        double shrink = 1.0 - (double) capY / capHeight * 0.28;
        double radius = capRadius * shrink;
        double distance = dx * dx + dz * dz;
        double noise = Math.sin(dx * 0.45) + Math.cos(dz * 0.37) + Math.sin((dx + dz) * 0.28);
        boolean inside = distance <= radius * radius + noise * roughness * radius;

        if (!inside) {
            return false;
        }

        double supportRadius = stemRadius + 2.5 + (capHeight - capY) * 0.35;
        double supportDistance = x * x + z * z;

        return supportDistance <= supportRadius * supportRadius;
    }

    @Override
    public int getRadius() {
        return capRadius;
    }

    @Override
    public int getHeight() {
        return stemHeight + capHeight;
    }

    @Override
    public int getEmbedDepth() {
        return embedDepth;
    }
}