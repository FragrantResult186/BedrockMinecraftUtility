package com.fragrant.minecraft.world.chunk;

public class ChunkStatus {
    public static final ChunkStatus EMPTY;
    public static final ChunkStatus NOISE;
    public static final ChunkStatus SURFACE;
    private final int index;

    private static ChunkStatus register(ChunkStatus chunkStatus) {
        return new ChunkStatus(chunkStatus);
    }

    ChunkStatus(ChunkStatus chunkStatus) {
        this.index = chunkStatus == null ? 0 : chunkStatus.getIndex() + 1;
    }

    public int getIndex() {
        return this.index;
    }

    static {
        EMPTY = register(null);
        NOISE = register(EMPTY);
        SURFACE = register(NOISE);
    }
}
