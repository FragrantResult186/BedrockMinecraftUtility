package com.fragrant.minecraft.world.gen;

import com.fragrant.minecraft.util.math.MathHelper;

public class SlideConfig {
    private final double target;
    private final int size;
    private final int offset;

    public SlideConfig(double d, int i, int j) {
        this.target = d;
        this.size = i;
        this.offset = j;
    }

    public double method_38414(double d, int i) {
        if (this.size <= 0) {
            return d;
        } else {
            double e = (double)(i - this.offset) / (double)this.size;
            return MathHelper.clampedLerp(this.target, d, e);
        }
    }
}
