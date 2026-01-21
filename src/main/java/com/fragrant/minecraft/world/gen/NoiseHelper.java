package com.fragrant.minecraft.world.gen;

import com.fragrant.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import com.fragrant.minecraft.util.math.MathHelper;

public class NoiseHelper {
    public static double lerpFromProgress(DoublePerlinNoiseSampler doublePerlinNoiseSampler, double d, double e, double f, double g, double h) {
        double i = doublePerlinNoiseSampler.sample(d, e, f);
        return MathHelper.lerpFromProgress(i, -1.0F, 1.0F, g, h);
    }
}
