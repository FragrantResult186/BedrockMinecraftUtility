package com.fragrant.minecraft.world.chunk;

import com.google.common.collect.Maps;
import com.fragrant.minecraft.util.math.BlockPos;
import com.fragrant.minecraft.util.math.ChunkPos;
import com.fragrant.minecraft.world.gen.AquiferSampler;
import com.fragrant.minecraft.world.gen.ChunkNoiseSampler;
import com.fragrant.minecraft.world.gen.NoiseColumnSampler;
import com.fragrant.minecraft.block.BlockState;
import com.fragrant.minecraft.block.Blocks;

import java.util.Map;
import java.util.function.Supplier;

public abstract class Chunk {
    protected final ChunkPos pos;
    protected ChunkNoiseSampler chunkNoiseSampler;
    protected final Map<Heightmap.Type, Heightmap> heightmaps = Maps.newEnumMap(Heightmap.Type.class);
    protected final ChunkSection[] sectionArray;

    public Chunk(ChunkPos chunkPos) {
        this.pos = chunkPos;
        this.sectionArray = new ChunkSection[24];
        fillSectionArray(this.sectionArray);
    }

    public BlockState getBlockState(BlockPos blockPos) {
        int i = blockPos.getY();
        if (i < this.getBottomY() || i >= this.getBottomY() + this.getHeight()) {
            return new BlockState(Blocks.VOID_AIR);
        } else {
            ChunkSection chunkSection = this.getSection(this.getSectionIndex(i));
            return chunkSection.isEmpty() ? new BlockState(Blocks.AIR) : chunkSection.getBlockState(blockPos.getX() & 15, i & 15, blockPos.getZ() & 15);
        }
    }

    private static void fillSectionArray(ChunkSection[] chunkSections) {
        for(int i = 0; i < 24; ++i) {
            if (chunkSections[i] == null) {
                chunkSections[i] = new ChunkSection(i-4);
            }
        }
    }

    public ChunkSection getHighestNonEmptySection() {
        ChunkSection[] chunkSections = this.getSectionArray();

        for(int i = chunkSections.length - 1; i >= 0; --i) {
            ChunkSection chunkSection = chunkSections[i];
            if (!chunkSection.isEmpty()) {
                return chunkSection;
            }
        }

        return null;
    }

    public int getHighestNonEmptySectionYOffset() {
        ChunkSection chunkSection = this.getHighestNonEmptySection();
        return chunkSection == null ? this.getBottomY() : chunkSection.getYOffset();
    }

    public ChunkSection[] getSectionArray() {
        return this.sectionArray;
    }

    public ChunkSection getSection(int i) {
        return this.getSectionArray()[i];
    }

    public int getSectionIndex(int worldY) {
        return (worldY + 64) >> 4;
    }

    public Heightmap getHeightmap(Heightmap.Type type) {
        return this.heightmaps.computeIfAbsent(type, Heightmap::new);
    }

    public ChunkPos getPos() {
        return this.pos;
    }

    public abstract ChunkStatus getStatus();

    public int getBottomY() {
        return -64;
    }

    public int getHeight() {
        return 384;
    }

    public ChunkNoiseSampler getOrCreateChunkNoiseSampler(
            NoiseColumnSampler noiseColumnSampler,
            Supplier<ChunkNoiseSampler.ColumnSampler> supplier,
            AquiferSampler.FluidLevelSampler fluidLevelSampler
    ) {
        if (this.chunkNoiseSampler == null) {
            this.chunkNoiseSampler = ChunkNoiseSampler.create(this, noiseColumnSampler, supplier, fluidLevelSampler);
        }
        return this.chunkNoiseSampler;
    }
}