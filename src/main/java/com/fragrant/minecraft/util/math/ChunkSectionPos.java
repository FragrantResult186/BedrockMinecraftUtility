package com.fragrant.minecraft.util.math;

import it.unimi.dsi.fastutil.longs.LongConsumer;

import java.util.stream.Stream;

public class ChunkSectionPos extends Vec3i {
    ChunkSectionPos(int i, int j, int k) {
        super(i, j, k);
    }

    public static ChunkSectionPos from(int i, int j, int k) {
        return new ChunkSectionPos(i, j, k);
    }

    public static ChunkSectionPos from(BlockPos blockPos) {
        return new ChunkSectionPos(getSectionCoord(blockPos.getX()), getSectionCoord(blockPos.getY()), getSectionCoord(blockPos.getZ()));
    }

    public static ChunkSectionPos from(ChunkPos chunkPos, int i) {
        return new ChunkSectionPos(chunkPos.x, i, chunkPos.z);
    }

    public static ChunkSectionPos from(long l) {
        return new ChunkSectionPos(unpackX(l), unpackY(l), unpackZ(l));
    }

    public static long offset(long l, Direction direction) {
        return offset(l, direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    public static long offset(long l, int i, int j, int k) {
        return asLong(unpackX(l) + i, unpackY(l) + j, unpackZ(l) + k);
    }

    public static int getSectionCoord(double d) {
        return getSectionCoord(MathHelper.floor(d));
    }

    public static int getSectionCoord(int i) {
        return i >> 4;
    }

    public static int getLocalCoord(int i) {
        return i & 15;
    }

    public static short packLocal(BlockPos blockPos) {
        int i = getLocalCoord(blockPos.getX());
        int j = getLocalCoord(blockPos.getY());
        int k = getLocalCoord(blockPos.getZ());
        return (short)(i << 8 | k << 4 | j << 0);
    }

    public static int unpackLocalX(short s) {
        return s >>> 8 & 15;
    }

    public static int unpackLocalY(short s) {
        return s >>> 0 & 15;
    }

    public static int unpackLocalZ(short s) {
        return s >>> 4 & 15;
    }

    public int unpackBlockX(short s) {
        return this.getMinX() + unpackLocalX(s);
    }

    public int unpackBlockY(short s) {
        return this.getMinY() + unpackLocalY(s);
    }

    public int unpackBlockZ(short s) {
        return this.getMinZ() + unpackLocalZ(s);
    }

    public BlockPos unpackBlockPos(short s) {
        return new BlockPos(this.unpackBlockX(s), this.unpackBlockY(s), this.unpackBlockZ(s));
    }

    public static int getBlockCoord(int i) {
        return i << 4;
    }

    public static int getOffsetPos(int i, int j) {
        return getBlockCoord(i) + j;
    }

    public static int unpackX(long l) {
        return (int)(l << 0 >> 42);
    }

    public static int unpackY(long l) {
        return (int)(l << 44 >> 44);
    }

    public static int unpackZ(long l) {
        return (int)(l << 22 >> 42);
    }

    public int getSectionX() {
        return this.getX();
    }

    public int getSectionY() {
        return this.getY();
    }

    public int getSectionZ() {
        return this.getZ();
    }

    public int getMinX() {
        return getBlockCoord(this.getSectionX());
    }

    public int getMinY() {
        return getBlockCoord(this.getSectionY());
    }

    public int getMinZ() {
        return getBlockCoord(this.getSectionZ());
    }

    public int getMaxX() {
        return getOffsetPos(this.getSectionX(), 15);
    }

    public int getMaxY() {
        return getOffsetPos(this.getSectionY(), 15);
    }

    public int getMaxZ() {
        return getOffsetPos(this.getSectionZ(), 15);
    }

    public static long fromBlockPos(long l) {
        return asLong(getSectionCoord(BlockPos.unpackLongX(l)), getSectionCoord(BlockPos.unpackLongY(l)), getSectionCoord(BlockPos.unpackLongZ(l)));
    }

    public static long withZeroY(long l) {
        return l & -1048576L;
    }

    public BlockPos getMinPos() {
        return new BlockPos(getBlockCoord(this.getSectionX()), getBlockCoord(this.getSectionY()), getBlockCoord(this.getSectionZ()));
    }

    public BlockPos getCenterPos() {
        int i = 8;
        return this.getMinPos().add(8, 8, 8);
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(this.getSectionX(), this.getSectionZ());
    }

    public static long toLong(BlockPos blockPos) {
        return asLong(getSectionCoord(blockPos.getX()), getSectionCoord(blockPos.getY()), getSectionCoord(blockPos.getZ()));
    }

    public static long asLong(int i, int j, int k) {
        long l = 0L;
        l |= ((long)i & 4194303L) << 42;
        l |= ((long)j & 1048575L) << 0;
        l |= ((long)k & 4194303L) << 20;
        return l;
    }

    public long asLong() {
        return asLong(this.getSectionX(), this.getSectionY(), this.getSectionZ());
    }

    public ChunkSectionPos add(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new ChunkSectionPos(this.getSectionX() + i, this.getSectionY() + j, this.getSectionZ() + k);
    }

    public Stream<BlockPos> streamBlocks() {
        return BlockPos.stream(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    public static void forEachChunkSectionAround(BlockPos blockPos, LongConsumer longConsumer) {
        forEachChunkSectionAround(blockPos.getX(), blockPos.getY(), blockPos.getZ(), longConsumer);
    }

    public static void forEachChunkSectionAround(long l, LongConsumer longConsumer) {
        forEachChunkSectionAround(BlockPos.unpackLongX(l), BlockPos.unpackLongY(l), BlockPos.unpackLongZ(l), longConsumer);
    }

    public static void forEachChunkSectionAround(int i, int j, int k, LongConsumer longConsumer) {
        int l = getSectionCoord(i - 1);
        int m = getSectionCoord(i + 1);
        int n = getSectionCoord(j - 1);
        int o = getSectionCoord(j + 1);
        int p = getSectionCoord(k - 1);
        int q = getSectionCoord(k + 1);
        if (l == m && n == o && p == q) {
            longConsumer.accept(asLong(l, n, p));
        } else {
            for(int r = l; r <= m; ++r) {
                for(int s = n; s <= o; ++s) {
                    for(int t = p; t <= q; ++t) {
                        longConsumer.accept(asLong(r, s, t));
                    }
                }
            }
        }
    }
}
