package com.fragrant.minecraft.world.chunk;

import com.fragrant.minecraft.world.gen.NoiseChunkGenerator;
import com.fragrant.minecraft.world.gen.random.RandomDeriver;
import com.fragrant.minecraft.util.math.ChunkPos;
import java.util.EnumSet;

public class ChunkBuilder {
    private final NoiseChunkGenerator chunkGenerator;

    public ChunkBuilder(RandomDeriver randomDeriver) {
        this.chunkGenerator = new NoiseChunkGenerator(randomDeriver);
    }

    public ProtoChunk generateChunk(ChunkPos chunkPos) {
        ProtoChunk chunk = new ProtoChunk(chunkPos);

        if (chunk.getStatus().getIndex() < ChunkStatus.NOISE.getIndex()) {
            step(chunk, ChunkStatus.NOISE, this::generateNoise);
        }
        if (chunk.getStatus().getIndex() < ChunkStatus.SURFACE.getIndex()) {
            step(chunk, ChunkStatus.SURFACE, this::generateSurface);
        }

        return chunk;
    }

    private void generateNoise(ProtoChunk chunk) {
        this.chunkGenerator.populateNoise(chunk);
    }

    private void generateSurface(ProtoChunk chunk) {
        Heightmap.populateHeightmaps(chunk, EnumSet.of(Heightmap.Type.WORLD_SURFACE));
    }

    private void step(ProtoChunk chunk, ChunkStatus status, GenerationStep generationStep) {
        generationStep.apply(chunk);
        chunk.setStatus(status);
    }

    @FunctionalInterface
    private interface GenerationStep {
        void apply(ProtoChunk chunk);
    }
}