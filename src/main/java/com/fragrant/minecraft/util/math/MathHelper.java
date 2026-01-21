package com.fragrant.minecraft.util.math;

import com.fragrant.minecraft.util.Util;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Random;
import java.util.function.IntPredicate;

public class MathHelper {
    public static final float PI = (float)Math.PI;
    public static final float HALF_PI = ((float)Math.PI / 2F);
    public static final float TAU = ((float)Math.PI * 2F);
    public static final float RADIANS_PER_DEGREE = ((float)Math.PI / 180F);
    public static final float DEGREES_PER_RADIAN = (180F / (float)Math.PI);
    public static final float EPSILON = 1.0E-5F;
    private static final float[] SINE_TABLE = Util.make(new float[65536], (fs) -> {
        for(int i = 0; i < fs.length; ++i) {
            fs[i] = (float)Math.sin((double)i * Math.PI * (double)2.0F / (double)65536.0F);
        }
    });
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
    private static final double[] ARCSINE_TABLE = new double[257];
    private static final double[] COSINE_TABLE = new double[257];

    public static float sin(float f) {
        return SINE_TABLE[(int)(f * 10430.378F) & '\uffff'];
    }

    public static float cos(float f) {
        return SINE_TABLE[(int)(f * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static int floor(float f) {
        int i = (int)f;
        return f < (float)i ? i - 1 : i;
    }

    public static int fastFloor(double d) {
        return (int)(d + (double)1024.0F) - 1024;
    }

    public static int floor(double d) {
        int i = (int)d;
        return d < (double)i ? i - 1 : i;
    }

    public static long lfloor(double d) {
        long l = (long)d;
        return d < (double)l ? l - 1L : l;
    }

    public static int absFloor(double d) {
        return (int)(d >= (double)0.0F ? d : -d + (double)1.0F);
    }

    public static float abs(float f) {
        return Math.abs(f);
    }

    public static int abs(int i) {
        return Math.abs(i);
    }

    public static int ceil(float f) {
        int i = (int)f;
        return f > (float)i ? i + 1 : i;
    }

    public static int ceil(double d) {
        int i = (int)d;
        return d > (double)i ? i + 1 : i;
    }

    public static byte clamp(byte b, byte c, byte d) {
        if (b < c) {
            return c;
        } else {
            return b > d ? d : b;
        }
    }

    public static int clamp(int i, int j, int k) {
        if (i < j) {
            return j;
        } else {
            return Math.min(i, k);
        }
    }

    public static long clamp(long l, long m, long n) {
        if (l < m) {
            return m;
        } else {
            return Math.min(l, n);
        }
    }

    public static float clamp(float f, float g, float h) {
        if (f < g) {
            return g;
        } else {
            return Math.min(f, h);
        }
    }

    public static double clamp(double d, double e, double f) {
        if (d < e) {
            return e;
        } else {
            return Math.min(d, f);
        }
    }

    public static double clampedLerp(double d, double e, double f) {
        if (f < (double)0.0F) {
            return d;
        } else {
            return f > (double)1.0F ? e : lerp(f, d, e);
        }
    }

    public static float clampedLerp(float f, float g, float h) {
        if (h < 0.0F) {
            return f;
        } else {
            return h > 1.0F ? g : lerp(h, f, g);
        }
    }

    public static double absMax(double d, double e) {
        if (d < (double)0.0F) {
            d = -d;
        }

        if (e < (double)0.0F) {
            e = -e;
        }

        return Math.max(d, e);
    }

    public static int floorDiv(int i, int j) {
        return Math.floorDiv(i, j);
    }

    public static int nextInt(Random random, int i, int j) {
        return i >= j ? i : random.nextInt(j - i + 1) + i;
    }

    public static float nextFloat(Random random, float f, float g) {
        return f >= g ? f : random.nextFloat() * (g - f) + f;
    }

    public static double nextDouble(Random random, double d, double e) {
        return d >= e ? d : random.nextDouble() * (e - d) + d;
    }

    public static double average(long[] ls) {
        long l = 0L;

        for(long m : ls) {
            l += m;
        }

        return (double)l / (double)ls.length;
    }

    public static boolean approximatelyEquals(float f, float g) {
        return Math.abs(g - f) < 1.0E-5F;
    }

    public static boolean approximatelyEquals(double d, double e) {
        return Math.abs(e - d) < (double)1.0E-5F;
    }

    public static int floorMod(int i, int j) {
        return Math.floorMod(i, j);
    }

    public static float floorMod(float f, float g) {
        return (f % g + g) % g;
    }

    public static double floorMod(double d, double e) {
        return (d % e + e) % e;
    }

    public static float wrapDegrees(float f) {
        float g = f % 360.0F;
        if (g >= 180.0F) {
            g -= 360.0F;
        }

        if (g < -180.0F) {
            g += 360.0F;
        }

        return g;
    }

    public static float subtractAngles(float f, float g) {
        return wrapDegrees(g - f);
    }

    public static float angleBetween(float f, float g) {
        return abs(subtractAngles(f, g));
    }

    public static float clampAngle(float f, float g, float h) {
        float i = subtractAngles(f, g);
        float j = clamp(i, -h, h);
        return g - j;
    }

    public static float stepTowards(float f, float g, float h) {
        h = abs(h);
        return f < g ? clamp(f + h, f, g) : clamp(f - h, g, f);
    }

    public static float stepUnwrappedAngleTowards(float f, float g, float h) {
        float i = subtractAngles(f, g);
        return stepTowards(f, f + i, h);
    }

    public static int parseInt(String string, int i) {
        return NumberUtils.toInt(string, i);
    }

    public static int parseInt(String string, int i, int j) {
        return Math.max(j, parseInt(string, i));
    }

    public static double parseDouble(String string, double d) {
        try {
            return Double.parseDouble(string);
        } catch (Throwable var4) {
            return d;
        }
    }

    public static double parseDouble(String string, double d, double e) {
        return Math.max(e, parseDouble(string, d));
    }

    public static int smallestEncompassingPowerOfTwo(int i) {
        int j = i - 1;
        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    public static boolean isPowerOfTwo(int i) {
        return i != 0 && (i & i - 1) == 0;
    }

    public static int ceilLog2(int i) {
        i = isPowerOfTwo(i) ? i : smallestEncompassingPowerOfTwo(i);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)i * 125613361L >> 27) & 31];
    }

    public static int floorLog2(int i) {
        return ceilLog2(i) - (isPowerOfTwo(i) ? 0 : 1);
    }

    public static int packRgb(float f, float g, float h) {
        return packRgb(floor(f * 255.0F), floor(g * 255.0F), floor(h * 255.0F));
    }

    public static int packRgb(int i, int j, int k) {
        int l = (i << 8) + j;
        l = (l << 8) + k;
        return l;
    }

    public static int multiplyColors(int i, int j) {
        int k = (i & 16711680) >> 16;
        int l = (j & 16711680) >> 16;
        int m = (i & '\uff00') >> 8;
        int n = (j & '\uff00') >> 8;
        int o = (i & 255);
        int p = (j & 255);
        int q = (int)((float)k * (float)l / 255.0F);
        int r = (int)((float)m * (float)n / 255.0F);
        int s = (int)((float)o * (float)p / 255.0F);
        return i & -16777216 | q << 16 | r << 8 | s;
    }

    public static int multiplyColors(int i, float f, float g, float h) {
        int j = (i & 16711680) >> 16;
        int k = (i & '\uff00') >> 8;
        int l = (i & 255);
        int m = (int)((float)j * f);
        int n = (int)((float)k * g);
        int o = (int)((float)l * h);
        return i & -16777216 | m << 16 | n << 8 | o;
    }

    public static float fractionalPart(float f) {
        return f - (float)floor(f);
    }

    public static double fractionalPart(double d) {
        return d - (double)lfloor(d);
    }

    public static long hashCode(Vec3i vec3i) {
        return hashCode(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static long hashCode(int i, int j, int k) {
        long l = (i * 3129871L) ^ (long)k * 116129781L ^ (long)j;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    public static double getLerpProgress(double d, double e, double f) {
        return (d - e) / (f - e);
    }

    public static float getLerpProgress(float f, float g, float h) {
        return (f - g) / (h - g);
    }

    public static int binarySearch(int i, int j, IntPredicate intPredicate) {
        int k = j - i;

        while(k > 0) {
            int l = k / 2;
            int m = i + l;
            if (intPredicate.test(m)) {
                k = l;
            } else {
                i = m + 1;
                k -= l + 1;
            }
        }

        return i;
    }

    public static float lerp(float f, float g, float h) {
        return g + f * (h - g);
    }

    public static double lerp(double d, double e, double f) {
        return e + d * (f - e);
    }

    public static double lerp2(double d, double e, double f, double g, double h, double i) {
        return lerp(e, lerp(d, f, g), lerp(d, h, i));
    }

    public static double lerp3(double d, double e, double f, double g, double h, double i, double j, double k, double l, double m, double n) {
        return lerp(f, lerp2(d, e, g, h, i, j), lerp2(d, e, k, l, m, n));
    }

    public static double perlinFade(double d) {
        return d * d * d * (d * (d * (double)6.0F - (double)15.0F) + (double)10.0F);
    }

    public static double perlinFadeDerivative(double d) {
        return (double)30.0F * d * d * (d - (double)1.0F) * (d - (double)1.0F);
    }

    public static float wrap(float f, float g) {
        return (Math.abs(f % g - g * 0.5F) - g * 0.25F) / (g * 0.25F);
    }

    public static float square(float f) {
        return f * f;
    }

    public static double square(double d) {
        return d * d;
    }

    public static int square(int i) {
        return i * i;
    }

    public static long square(long l) {
        return l * l;
    }

    public static double clampedLerpFromProgress(double d, double e, double f, double g, double h) {
        return clampedLerp(g, h, getLerpProgress(d, e, f));
    }

    public static float clampedLerpFromProgress(float f, float g, float h, float i, float j) {
        return clampedLerp(i, j, getLerpProgress(f, g, h));
    }

    public static double lerpFromProgress(double d, double e, double f, double g, double h) {
        return lerp(getLerpProgress(d, e, f), g, h);
    }

    public static float lerpFromProgress(float f, float g, float h, float i, float j) {
        return lerp(getLerpProgress(f, g, h), i, j);
    }

    public static int ceilDiv(int i, int j) {
        return -Math.floorDiv(-i, j);
    }

    public static int nextBetween(Random random, int i, int j) {
        return random.nextInt(j - i + 1) + i;
    }

    public static float nextBetween(Random random, float f, float g) {
        return random.nextFloat() * (g - f) + f;
    }

    public static float nextGaussian(Random random, float f, float g) {
        return f + (float)random.nextGaussian() * g;
    }

    public static double hypot(double d, double e) {
        return Math.sqrt(d * d + e * e);
    }

    public static double magnitude(double d, double e, double f) {
        return Math.sqrt(d * d + e * e + f * f);
    }

    public static int roundDownToMultiple(double d, int i) {
        return floor(d / (double)i) * i;
    }
}
