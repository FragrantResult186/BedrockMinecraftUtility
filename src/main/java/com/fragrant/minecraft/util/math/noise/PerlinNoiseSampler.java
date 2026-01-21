package com.fragrant.minecraft.util.math.noise;

import com.fragrant.minecraft.world.gen.random.AbstractRandom;
import com.fragrant.minecraft.util.math.MathHelper;

public final class PerlinNoiseSampler {
    private static final float field_31701 = 1.0E-7F;
    private final byte[] permutations;
    public final double originX;
    public final double originY;
    public final double originZ;

    public PerlinNoiseSampler(AbstractRandom abstractRandom) {
        this.originX = abstractRandom.nextDouble() * (double)256.0F;
        this.originY = abstractRandom.nextDouble() * (double)256.0F;
        this.originZ = abstractRandom.nextDouble() * (double)256.0F;
        this.permutations = new byte[256];

        for(int i = 0; i < 256; ++i) {
            this.permutations[i] = (byte)i;
        }

        for(int i = 0; i < 256; ++i) {
            int j = abstractRandom.nextInt(256 - i);
            byte b = this.permutations[i];
            this.permutations[i] = this.permutations[i + j];
            this.permutations[i + j] = b;
        }

    }

    public double sample(double d, double e, double f) {
        return this.sample(d, e, f, 0.0F, 0.0F);
    }

    public double sample(double d, double e, double f, double g, double h) {
        double i = d + this.originX;
        double j = e + this.originY;
        double k = f + this.originZ;
        int l = MathHelper.floor(i);
        int m = MathHelper.floor(j);
        int n = MathHelper.floor(k);
        double o = i - (double)l;
        double p = j - (double)m;
        double q = k - (double)n;
        double s;
        if (g != (double)0.0F) {
            double r;
            if (h >= (double)0.0F && h < p) {
                r = h;
            } else {
                r = p;
            }

            s = (double)MathHelper.floor(r / g + (double)field_31701) * g;
        } else {
            s = 0.0F;
        }

        return this.sample(l, m, n, o, p - s, q, p);
    }

    public double sampleDerivative(double d, double e, double f, double[] ds) {
        double g = d + this.originX;
        double h = e + this.originY;
        double i = f + this.originZ;
        int j = MathHelper.floor(g);
        int k = MathHelper.floor(h);
        int l = MathHelper.floor(i);
        double m = g - (double)j;
        double n = h - (double)k;
        double o = i - (double)l;
        return this.sampleDerivative(j, k, l, m, n, o, ds);
    }

    private static double grad(int i, double d, double e, double f) {
        return SimplexNoiseSampler.dot(SimplexNoiseSampler.GRADIENTS[i & 15], d, e, f);
    }

    private int getGradient(int i) {
        return this.permutations[i & 255] & 255;
    }

    private double sample(int i, int j, int k, double d, double e, double f, double g) {
        int l = this.getGradient(i);
        int m = this.getGradient(i + 1);
        int n = this.getGradient(l + j);
        int o = this.getGradient(l + j + 1);
        int p = this.getGradient(m + j);
        int q = this.getGradient(m + j + 1);
        double h = grad(this.getGradient(n + k), d, e, f);
        double r = grad(this.getGradient(p + k), d - (double)1.0F, e, f);
        double s = grad(this.getGradient(o + k), d, e - (double)1.0F, f);
        double t = grad(this.getGradient(q + k), d - (double)1.0F, e - (double)1.0F, f);
        double u = grad(this.getGradient(n + k + 1), d, e, f - (double)1.0F);
        double v = grad(this.getGradient(p + k + 1), d - (double)1.0F, e, f - (double)1.0F);
        double w = grad(this.getGradient(o + k + 1), d, e - (double)1.0F, f - (double)1.0F);
        double x = grad(this.getGradient(q + k + 1), d - (double)1.0F, e - (double)1.0F, f - (double)1.0F);
        double y = MathHelper.perlinFade(d);
        double z = MathHelper.perlinFade(g);
        double aa = MathHelper.perlinFade(f);
        return MathHelper.lerp3(y, z, aa, h, r, s, t, u, v, w, x);
    }

    private double sampleDerivative(int i, int j, int k, double d, double e, double f, double[] ds) {
        int l = this.getGradient(i);
        int m = this.getGradient(i + 1);
        int n = this.getGradient(l + j);
        int o = this.getGradient(l + j + 1);
        int p = this.getGradient(m + j);
        int q = this.getGradient(m + j + 1);
        int r = this.getGradient(n + k);
        int s = this.getGradient(p + k);
        int t = this.getGradient(o + k);
        int u = this.getGradient(q + k);
        int v = this.getGradient(n + k + 1);
        int w = this.getGradient(p + k + 1);
        int x = this.getGradient(o + k + 1);
        int y = this.getGradient(q + k + 1);
        int[] is = SimplexNoiseSampler.GRADIENTS[r & 15];
        int[] js = SimplexNoiseSampler.GRADIENTS[s & 15];
        int[] ks = SimplexNoiseSampler.GRADIENTS[t & 15];
        int[] ls = SimplexNoiseSampler.GRADIENTS[u & 15];
        int[] ms = SimplexNoiseSampler.GRADIENTS[v & 15];
        int[] ns = SimplexNoiseSampler.GRADIENTS[w & 15];
        int[] os = SimplexNoiseSampler.GRADIENTS[x & 15];
        int[] ps = SimplexNoiseSampler.GRADIENTS[y & 15];
        double g = SimplexNoiseSampler.dot(is, d, e, f);
        double h = SimplexNoiseSampler.dot(js, d - (double)1.0F, e, f);
        double z = SimplexNoiseSampler.dot(ks, d, e - (double)1.0F, f);
        double aa = SimplexNoiseSampler.dot(ls, d - (double)1.0F, e - (double)1.0F, f);
        double ab = SimplexNoiseSampler.dot(ms, d, e, f - (double)1.0F);
        double ac = SimplexNoiseSampler.dot(ns, d - (double)1.0F, e, f - (double)1.0F);
        double ad = SimplexNoiseSampler.dot(os, d, e - (double)1.0F, f - (double)1.0F);
        double ae = SimplexNoiseSampler.dot(ps, d - (double)1.0F, e - (double)1.0F, f - (double)1.0F);
        double af = MathHelper.perlinFade(d);
        double ag = MathHelper.perlinFade(e);
        double ah = MathHelper.perlinFade(f);
        double ai = MathHelper.lerp3(af, ag, ah, is[0], js[0], ks[0], ls[0], ms[0], ns[0], os[0], ps[0]);
        double aj = MathHelper.lerp3(af, ag, ah, is[1], js[1], ks[1], ls[1], ms[1], ns[1], os[1], ps[1]);
        double ak = MathHelper.lerp3(af, ag, ah, is[2], js[2], ks[2], ls[2], ms[2], ns[2], os[2], ps[2]);
        double al = MathHelper.lerp2(ag, ah, h - g, aa - z, ac - ab, ae - ad);
        double am = MathHelper.lerp2(ah, af, z - g, ad - ab, aa - h, ae - ac);
        double an = MathHelper.lerp2(af, ag, ab - g, ac - h, ad - z, ae - aa);
        double ao = MathHelper.perlinFadeDerivative(d);
        double ap = MathHelper.perlinFadeDerivative(e);
        double aq = MathHelper.perlinFadeDerivative(f);
        double ar = ai + ao * al;
        double as = aj + ap * am;
        double at = ak + aq * an;
        ds[0] += ar;
        ds[1] += as;
        ds[2] += at;
        return MathHelper.lerp3(af, ag, ah, g, h, z, aa, ab, ac, ad, ae);
    }

}
