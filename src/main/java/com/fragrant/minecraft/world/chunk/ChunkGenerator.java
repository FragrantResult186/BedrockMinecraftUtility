package com.fragrant.minecraft.world.chunk;

import com.fragrant.minecraft.block.BlockState;
import com.fragrant.minecraft.world.gen.random.ChunkRandom;
import com.fragrant.minecraft.util.math.BlockPos;
import com.fragrant.minecraft.util.math.ChunkPos;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkGenerator {
    private final Map<ChunkPos, ProtoChunk> chunkCache = new ConcurrentHashMap<>();
    private volatile ChunkBuilder chunkBuilder;
    private long worldSeed;

    public ChunkGenerator() {
        this(0L);
    }

    public ChunkGenerator(long worldSeed) {
        setSeed(worldSeed);
    }

    public synchronized void setSeed(long worldSeed) {
        this.worldSeed = worldSeed;
        ChunkBuilder builder = new ChunkBuilder(
                ChunkRandom.RandomProvider.XOROSHIRO.create(worldSeed).createRandomDeriver()
        );
        clearCache();
        this.chunkBuilder = builder;
    }

    private int getHeightAtColumn(ProtoChunk chunk, int localX, int localZ,
                                  Heightmap.Type heightmapType) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int topY = chunk.getHighestNonEmptySectionYOffset() + 16;
        var predicate = heightmapType.getBlockPredicate();

        for (int y = topY - 1; y >= -64; --y) {
            mutable.set(localX, y, localZ);
            BlockState blockState = chunk.getBlockState(mutable);
            if (predicate.test(blockState)) {
                return y + 1;
            }
        }
        return -64;
    }

    public int getHeight(int worldX, int worldZ, Heightmap.Type heightmapType) {
        int localX = worldX & 15;
        int localZ = worldZ & 15;
        ProtoChunk chunk = getChunkFromWorldPos(worldX, worldZ);

        if (chunk.heightmaps.containsKey(heightmapType)) {
            return chunk.getHeightmap(heightmapType).get(localX, localZ);
        }

        return getHeightAtColumn(chunk, localX, localZ, heightmapType);
    }

    public int getWorldSurfaceHeight(int worldX, int worldZ) {
        return getHeight(worldX, worldZ, Heightmap.Type.WORLD_SURFACE);
    }

    public int getTopSolidBlockHeight(int worldX, int worldZ) {
        return getWorldSurfaceHeight(worldX, worldZ) - 1;
    }

    public BlockState getBlockState(int worldX, int worldY, int worldZ) {
        BlockPos blockPos = new BlockPos(worldX, worldY, worldZ);
        return getChunkFromWorldPos(worldX, worldZ).getBlockState(blockPos);
    }

    public BlockState getBlockState(BlockPos blockPos) {
        return getBlockState(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public Heightmap getWorldSurfaceHeightmap(int chunkX, int chunkZ) {
        return getChunk(chunkX, chunkZ).getHeightmap(Heightmap.Type.WORLD_SURFACE);
    }

    public boolean isAir(int worldX, int worldY, int worldZ) {
        return getBlockState(worldX, worldY, worldZ).isEmpty();
    }

    public boolean isSolid(int worldX, int worldY, int worldZ) {
        return !isAir(worldX, worldY, worldZ) &&
                !Objects.equals(getBlockState(worldX, worldY, worldZ).getBlock().getName(), "minecraft:lava") &&
                !Objects.equals(getBlockState(worldX, worldY, worldZ).getBlock().getName(), "minecraft:water");
    }

    public void clearCache() {
        this.chunkCache.clear();
    }

    private ProtoChunk getChunkFromWorldPos(int worldX, int worldZ) {
        int chunkX = worldX >> 4;
        int chunkZ = worldZ >> 4;
        return getChunk(chunkX, chunkZ);
    }

    private ProtoChunk getChunk(int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        if (this.chunkBuilder == null) {
            throw new IllegalStateException("ChunkBuilder not initialized. Call setRandom() first");
        }
        return this.chunkCache.computeIfAbsent(chunkPos, this.chunkBuilder::generateChunk);
    }

    public long getSeed() {
        return worldSeed;
    }
}