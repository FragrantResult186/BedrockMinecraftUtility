package com.fragrant.minecraft.world.gen;

public class NoiseSamplingConfig {
    private final double xzScale;
    private final double yScale;
    private final double xzFactor;
    private final double yFactor;

    public NoiseSamplingConfig(double d, double e, double f, double g) {
        this.xzScale = d;
        this.yScale = e;
        this.xzFactor = f;
        this.yFactor = g;
    }

    public double getXZScale() {
        return this.xzScale;
    }

    public double getYScale() {
        return this.yScale;
    }

    public double getXZFactor() {
        return this.xzFactor;
    }

    public double getYFactor() {
        return this.yFactor;
    }
}
