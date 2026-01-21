package com.fragrant.minecraft.util.math;

import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ChunkPos {
    public final int x;
    public final int z;

    public ChunkPos(int i, int j) {
        this.x = i;
        this.z = j;
    }

    public ChunkPos(BlockPos blockPos) {
        this.x = ChunkSectionPos.getSectionCoord(blockPos.getX());
        this.z = ChunkSectionPos.getSectionCoord(blockPos.getZ());
    }

    public ChunkPos(long l) {
        this.x = (int)l;
        this.z = (int)(l >> 32);
    }

    public long toLong() {
        return toLong(this.x, this.z);
    }

    public static long toLong(int i, int j) {
        return (long)i & 4294967295L | ((long)j & 4294967295L) << 32;
    }

    public static long toLong(BlockPos blockPos) {
        return toLong(ChunkSectionPos.getSectionCoord(blockPos.getX()), ChunkSectionPos.getSectionCoord(blockPos.getZ()));
    }

    public static int getPackedX(long l) {
        return (int)(l & 4294967295L);
    }

    public static int getPackedZ(long l) {
        return (int)(l >>> 32 & 4294967295L);
    }

    public int hashCode() {
        int i = 1664525 * this.x + 1013904223;
        int j = 1664525 * (this.z ^ -559038737) + 1013904223;
        return i ^ j;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChunkPos)) {
            return false;
        } else {
            ChunkPos chunkPos = (ChunkPos)object;
            return this.x == chunkPos.x && this.z == chunkPos.z;
        }
    }

    public int getCenterX() {
        return this.getOffsetX(8);
    }

    public int getCenterZ() {
        return this.getOffsetZ(8);
    }

    public int getStartX() {
        return ChunkSectionPos.getBlockCoord(this.x);
    }

    public int getStartZ() {
        return ChunkSectionPos.getBlockCoord(this.z);
    }

    public int getEndX() {
        return this.getOffsetX(15);
    }

    public int getEndZ() {
        return this.getOffsetZ(15);
    }

    public int getRegionX() {
        return this.x >> 5;
    }

    public int getRegionZ() {
        return this.z >> 5;
    }

    public int getRegionRelativeX() {
        return this.x & 31;
    }

    public int getRegionRelativeZ() {
        return this.z & 31;
    }

    public BlockPos getBlockPos(int i, int j, int k) {
        return new BlockPos(this.getOffsetX(i), j, this.getOffsetZ(k));
    }

    public int getOffsetX(int i) {
        return ChunkSectionPos.getOffsetPos(this.x, i);
    }

    public int getOffsetZ(int i) {
        return ChunkSectionPos.getOffsetPos(this.z, i);
    }

    public BlockPos getCenterAtY(int i) {
        return new BlockPos(this.getCenterX(), i, this.getCenterZ());
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }

    public BlockPos getStartPos() {
        return new BlockPos(this.getStartX(), 0, this.getStartZ());
    }

    public int getChebyshevDistance(ChunkPos chunkPos) {
        return Math.max(Math.abs(this.x - chunkPos.x), Math.abs(this.z - chunkPos.z));
    }

    public static Stream<ChunkPos> stream(ChunkPos chunkPos, int i) {
        return stream(new ChunkPos(chunkPos.x - i, chunkPos.z - i), new ChunkPos(chunkPos.x + i, chunkPos.z + i));
    }

    public static Stream<ChunkPos> stream(final ChunkPos chunkPos, final ChunkPos chunkPos2) {
        int i = Math.abs(chunkPos.x - chunkPos2.x) + 1;
        int j = Math.abs(chunkPos.z - chunkPos2.z) + 1;
        final int k = chunkPos.x < chunkPos2.x ? 1 : -1;
        final int l = chunkPos.z < chunkPos2.z ? 1 : -1;
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkPos>((long)(i * j), 64) {
            private ChunkPos position;

            public boolean tryAdvance(Consumer<? super ChunkPos> consumer) {
                if (this.position == null) {
                    this.position = chunkPos;
                } else {
                    int i = this.position.x;
                    int j = this.position.z;
                    if (i == chunkPos2.x) {
                        if (j == chunkPos2.z) {
                            return false;
                        }

                        this.position = new ChunkPos(chunkPos.x, j + l);
                    } else {
                        this.position = new ChunkPos(i + k, j);
                    }
                }

                consumer.accept(this.position);
                return true;
            }
        }, false);
    }
}
