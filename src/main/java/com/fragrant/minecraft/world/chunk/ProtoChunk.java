package com.fragrant.minecraft.world.chunk;

import com.fragrant.minecraft.util.math.ChunkPos;

public class ProtoChunk extends Chunk {
    private volatile ChunkStatus status;

    public ProtoChunk(ChunkPos chunkPos) {
        super(chunkPos);
        this.status = ChunkStatus.EMPTY;
    }

    public ChunkStatus getStatus() {
        return this.status;
    }

    public void setStatus(ChunkStatus chunkStatus) {
        this.status = chunkStatus;
    }
}