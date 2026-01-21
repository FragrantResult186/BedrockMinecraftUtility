package com.fragrant.minecraft.util.math.noise;

import com.fragrant.minecraft.world.gen.random.AbstractRandom;

public class SimplexNoiseSampler {
    protected static final int[][] GRADIENTS = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}, {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}};
    private final int[] permutations = new int[512];
    public final double originX;
    public final double originY;
    public final double originZ;

    public SimplexNoiseSampler(AbstractRandom abstractRandom) {
        this.originX = abstractRandom.nextDouble() * (double)256.0F;
        this.originY = abstractRandom.nextDouble() * (double)256.0F;
        this.originZ = abstractRandom.nextDouble() * (double)256.0F;

        for(int i = 0; i < 256; ++i) {
            int j = abstractRandom.nextInt(256 - i);
            int k = this.permutations[i];
            this.permutations[i] = this.permutations[j + i];
            this.permutations[j + i] = k;
        }
    }

    protected static double dot(int[] is, double d, double e, double f) {
        return (double)is[0] * d + (double)is[1] * e + (double)is[2] * f;
    }
}
