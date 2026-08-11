package net.enigma.windyhills.worldgen.feature.custom.shape;

public interface RockShape {
    boolean contains(int x, int y,int z);

    int getRadius();
    int getHeight();
    int getEmbedDepth();
}
