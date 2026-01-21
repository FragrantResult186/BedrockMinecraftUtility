package com.fragrant.minecraft.block;

import com.fragrant.minecraft.world.gen.ChunkNoiseSampler;

public interface BlockSource {
    BlockState apply(ChunkNoiseSampler chunkNoiseSampler, int i, int j, int k);
}