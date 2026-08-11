package net.enigma.windyhills.worldgen.feature.custom.util;

import net.minecraft.core.BlockPos;

public final class FormationBounds {
    private static final int BASE_RADIUS_PADDING = 10;
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;
    private boolean empty = true;

    public void include(BlockPos pos) {
        include(pos.getX(), pos.getZ());
    }

    public void include(int x, int z) {
        if (empty) {
            minX = maxX = x;
            minZ = maxZ = z;
            empty = false;
            return;
        }

        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x);
        minZ = Math.min(minZ, z);
        maxZ = Math.max(maxZ, z);
    }

    public boolean isEmpty() {
        return empty;
    }

    public int centerX() {
        return (minX + maxX) / 2;
    }

    public int centerZ() {
        return (minZ + maxZ) / 2;
    }

    public int baseRadius() {
        int halfX = (maxX - minX) / 2 + 1;
        int halfZ = (maxZ - minZ) / 2 + 1;

        return Math.max(halfX, halfZ) + BASE_RADIUS_PADDING;
    }
}
