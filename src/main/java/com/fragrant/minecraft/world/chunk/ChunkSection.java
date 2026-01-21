package com.fragrant.minecraft.world.chunk;

import com.fragrant.minecraft.block.BlockState;
import com.fragrant.minecraft.block.Blocks;

public class ChunkSection {
    private final int yOffset;
    private short nonEmptyBlockCount;
    private final BlockState[][][] blocks;

    public ChunkSection(int i) {
        this.yOffset = blockCoordFromChunkCoord(i);
        this.blocks = new BlockState[16][16][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    this.blocks[x][y][z] = new BlockState(Blocks.AIR);
                }
            }
        }
    }

    public static int blockCoordFromChunkCoord(int i) {
        return i << 4;
    }

    public BlockState getBlockState(int i, int j, int k) {
        return this.blocks[i][j][k];
    }

    public BlockState setBlockState(int i, int j, int k, BlockState blockState) {
        BlockState oldState = this.blocks[i][j][k];
        if (!oldState.isEmpty()) {
            --this.nonEmptyBlockCount;
        }
        this.blocks[i][j][k] = blockState;
        if (!blockState.isEmpty()) {
            ++this.nonEmptyBlockCount;
        }
        return oldState;
    }

    public boolean isEmpty() {
        return this.nonEmptyBlockCount == 0;
    }

    public int getYOffset() {
        return this.yOffset;
    }
}