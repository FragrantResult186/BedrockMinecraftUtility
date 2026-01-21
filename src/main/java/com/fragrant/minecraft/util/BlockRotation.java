package com.fragrant.minecraft.util;

import java.util.Random;

public enum BlockRotation {
    NONE(),
    CLOCKWISE_90(),
    CLOCKWISE_180(),
    COUNTERCLOCKWISE_90();

    BlockRotation() {}

    public static BlockRotation random(Random random) {
        return Util.getRandom(values(), random);
    }
}
