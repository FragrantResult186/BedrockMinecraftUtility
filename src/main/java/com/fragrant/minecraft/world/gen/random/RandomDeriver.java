package com.fragrant.minecraft.world.gen.random;

import com.fragrant.minecraft.util.Identifier;
import com.fragrant.minecraft.util.math.BlockPos;

public interface RandomDeriver {
    default AbstractRandom createRandom(BlockPos blockPos) {
        return this.createRandom(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    default AbstractRandom createRandom(Identifier identifier) {
        return this.createRandom(identifier.toString());
    }

    AbstractRandom createRandom(String string);

    AbstractRandom createRandom(int i, int j, int k);
}
