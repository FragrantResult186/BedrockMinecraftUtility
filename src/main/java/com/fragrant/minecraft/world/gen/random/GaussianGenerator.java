package com.fragrant.minecraft.world.gen.random;

import com.fragrant.minecraft.util.math.MathHelper;

public class GaussianGenerator {
    public final AbstractRandom baseRandom;
    private double nextNextGaussian;
    private boolean hasNextGaussian;

    public GaussianGenerator(AbstractRandom abstractRandom) {
        this.baseRandom = abstractRandom;
    }

    public void reset() {
        this.hasNextGaussian = false;
    }

    public double next() {
        if (this.hasNextGaussian) {
            this.hasNextGaussian = false;
            return this.nextNextGaussian;
        } else {
            double d;
            double e;
            double f;
            do {
                d = (double)2.0F * this.baseRandom.nextDouble() - (double)1.0F;
                e = (double)2.0F * this.baseRandom.nextDouble() - (double)1.0F;
                f = MathHelper.square(d) + MathHelper.square(e);
            } while(f >= (double)1.0F || f == (double)0.0F);

            double g = Math.sqrt((double)-2.0F * Math.log(f) / f);
            this.nextNextGaussian = e * g;
            this.hasNextGaussian = true;
            return d * g;
        }
    }
}
