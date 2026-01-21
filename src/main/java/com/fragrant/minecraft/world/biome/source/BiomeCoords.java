package com.fragrant.minecraft.world.biome.source;

public final class BiomeCoords {
    private BiomeCoords() {

    }

    public static int fromBlock(int i) {
        return i >> 2;
    }

    public static int toBlock(int i) {
        return i << 2;
    }

    public static int fromChunk(int i) {
        return i << 2;
    }

    public static int toChunk(int i) {
        return i >> 2;
    }
}
