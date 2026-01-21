package com.fragrant.minecraft.world.gen;

import com.fragrant.minecraft.util.math.MathHelper;
import com.fragrant.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import com.fragrant.minecraft.util.math.noise.PerlinNoiseSampler;
import com.fragrant.minecraft.world.gen.random.AbstractRandom;

import java.util.stream.IntStream;

public class NoiseSampler {
    private final OctavePerlinNoiseSampler lowerInterpolatedNoise;
    private final OctavePerlinNoiseSampler upperInterpolatedNoise;
    private final OctavePerlinNoiseSampler interpolationNoise;
    private final double xzScale;
    private final double yScale;
    private final double xzMainScale;
    private final double yMainScale;
    private final int cellWidth;
    private final int cellHeight;

    private NoiseSampler(OctavePerlinNoiseSampler octavePerlinNoiseSampler, OctavePerlinNoiseSampler octavePerlinNoiseSampler2, OctavePerlinNoiseSampler octavePerlinNoiseSampler3, NoiseSamplingConfig noiseSamplingConfig, int i, int j) {
        this.lowerInterpolatedNoise = octavePerlinNoiseSampler;
        this.upperInterpolatedNoise = octavePerlinNoiseSampler2;
        this.interpolationNoise = octavePerlinNoiseSampler3;
        this.xzScale = 684.412 * noiseSamplingConfig.getXZScale();
        this.yScale = 684.412 * noiseSamplingConfig.getYScale();
        this.xzMainScale = this.xzScale / noiseSamplingConfig.getXZFactor();
        this.yMainScale = this.yScale / noiseSamplingConfig.getYFactor();
        this.cellWidth = i;
        this.cellHeight = j;
    }

    public NoiseSampler(AbstractRandom abstractRandom, NoiseSamplingConfig noiseSamplingConfig, int i, int j) {
        this(OctavePerlinNoiseSampler.createLegacy(abstractRandom, IntStream.rangeClosed(-15, 0)), OctavePerlinNoiseSampler.createLegacy(abstractRandom, IntStream.rangeClosed(-15, 0)), OctavePerlinNoiseSampler.createLegacy(abstractRandom, IntStream.rangeClosed(-7, 0)), noiseSamplingConfig, i, j);
    }

    public double calculateNoise(int i, int j, int k) {
        int l = Math.floorDiv(i, this.cellWidth);
        int m = Math.floorDiv(j, this.cellHeight);
        int n = Math.floorDiv(k, this.cellWidth);
        double d = 0.0F;
        double e = 0.0F;
        double f = 0.0F;
        double g = 1.0F;

        for(int o = 0; o < 8; ++o) {
            PerlinNoiseSampler perlinNoiseSampler = this.interpolationNoise.getOctave(o);
            if (perlinNoiseSampler != null) {
                f += perlinNoiseSampler.sample(OctavePerlinNoiseSampler.maintainPrecision((double)l * this.xzMainScale * g), OctavePerlinNoiseSampler.maintainPrecision((double)m * this.yMainScale * g), OctavePerlinNoiseSampler.maintainPrecision((double)n * this.xzMainScale * g), this.yMainScale * g, (double)m * this.yMainScale * g) / g;
            }

            g /= 2.0F;
        }

        double o = (f / 10.0F + (double)1.0F) / (double)2.0F;
        boolean bl2 = o >= 1.0F;
        boolean bl3 = o <= 0.0F;
        g = 1.0F;

        for(int p = 0; p < 16; ++p) {
            double h = OctavePerlinNoiseSampler.maintainPrecision(l * this.xzScale * g);
            double q = OctavePerlinNoiseSampler.maintainPrecision(m * this.yScale * g);
            double r = OctavePerlinNoiseSampler.maintainPrecision(n * this.xzScale * g);
            double s = this.yScale * g;
            if (!bl2) {
                PerlinNoiseSampler perlinNoiseSampler2 = this.lowerInterpolatedNoise.getOctave(p);
                if (perlinNoiseSampler2 != null) {
                    d += perlinNoiseSampler2.sample(h, q, r, s, m * s) / g;
                }
            }

            if (!bl3) {
                PerlinNoiseSampler perlinNoiseSampler2 = this.upperInterpolatedNoise.getOctave(p);
                if (perlinNoiseSampler2 != null) {
                    e += perlinNoiseSampler2.sample(h, q, r, s, m * s) / g;
                }
            }

            g /= 2.0F;
        }

        return MathHelper.clampedLerp(d / 512.0F, e / 512.0F, o) / 128.0F;
    }
}