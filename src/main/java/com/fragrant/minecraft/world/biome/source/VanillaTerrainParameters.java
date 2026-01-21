package com.fragrant.minecraft.world.biome.source;

import com.google.common.annotations.VisibleForTesting;

import com.fragrant.minecraft.util.math.MathHelper;
import com.fragrant.minecraft.util.function.ToFloatFunction;
import com.fragrant.minecraft.util.math.Spline;

public final class VanillaTerrainParameters {
    private static final ToFloatFunction<Float> field_35673;
    private final Spline<NoisePoint> offsetSpline;
    private final Spline<NoisePoint> factorSpline;
    private final Spline<NoisePoint> peakSpline;

    public VanillaTerrainParameters(Spline<NoisePoint> spline, Spline<NoisePoint> spline2, Spline<NoisePoint> spline3) {
        this.offsetSpline = spline;
        this.factorSpline = spline2;
        this.peakSpline = spline3;
    }

    public static VanillaTerrainParameters createSurfaceParameters() {
        ToFloatFunction<Float> toFloatFunction = field_35673;
        ToFloatFunction<Float> toFloatFunction2 = field_35673;
        ToFloatFunction<Float> toFloatFunction3 = field_35673;
        Spline<NoisePoint> spline = createLandSpline(-0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, toFloatFunction);
        Spline<NoisePoint> spline2 = createLandSpline(-0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, toFloatFunction);
        Spline<NoisePoint> spline3 = createLandSpline(-0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, toFloatFunction);
        Spline<NoisePoint> spline4 = createLandSpline(-0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, toFloatFunction);
        Spline<NoisePoint> spline5 = Spline.builder(LocationFunction.CONTINENTS, toFloatFunction).add(-1.1F, 0.044F, 0.0F).add(-1.02F, -0.2222F, 0.0F).add(-0.51F, -0.2222F, 0.0F).add(-0.44F, -0.12F, 0.0F).add(-0.18F, -0.12F, 0.0F).add(-0.16F, spline, 0.0F).add(-0.15F, spline, 0.0F).add(-0.1F, spline2, 0.0F).add(0.25F, spline3, 0.0F).add(1.0F, spline4, 0.0F).build();
        Spline<NoisePoint> spline6 = Spline.builder(LocationFunction.CONTINENTS, field_35673).add(-0.19F, 3.95F, 0.0F).add(-0.15F, buildErosionFactorSpline(6.25F, true, field_35673), 0.0F).add(-0.1F, buildErosionFactorSpline(5.47F, true, toFloatFunction2), 0.0F).add(0.03F, buildErosionFactorSpline(5.08F, true, toFloatFunction2), 0.0F).add(0.06F, buildErosionFactorSpline(4.69F, false, toFloatFunction2), 0.0F).build();
        Spline<NoisePoint> spline7 = Spline.builder(LocationFunction.CONTINENTS, toFloatFunction3).add(-0.11F, 0.0F, 0.0F).add(0.03F, method_38856(1.0F, 0.5F, 0.0F, 0.0F, toFloatFunction3), 0.0F).add(0.65F, method_38856(1.0F, 1.0F, 1.0F, 0.0F, toFloatFunction3), 0.0F).build();
        return new VanillaTerrainParameters(spline5, spline6, spline7);
    }

    private static Spline<NoisePoint> method_38856(float f, float g, float h, float i, ToFloatFunction<Float> toFloatFunction) {
        Spline<NoisePoint> spline1 = method_38855(f, h, toFloatFunction);
        Spline<NoisePoint> spline2 = method_38855(g, i, toFloatFunction);
        return Spline.builder(LocationFunction.EROSION, toFloatFunction).add(-1.0F, spline1, 0.0F).add(-0.78F, spline2, 0.0F).add(-0.5775F, spline2, 0.0F).add(-0.375F, 0.0F, 0.0F).build();
    }

    private static Spline<NoisePoint> method_38855(float f, float g, ToFloatFunction<Float> toFloatFunction) {
        float h = getNormalizedWeirdness(0.4F);
        float i = getNormalizedWeirdness(0.56666666F);
        float j = (h + i) / 2.0F;
        Spline.Builder<NoisePoint> builder = Spline.builder(LocationFunction.RIDGES, toFloatFunction);
        builder.add(h, 0.0F, 0.0F);
        if (g > 0.0F) {
            builder.add(j, method_38857(g, toFloatFunction), 0.0F);
        } else {
            builder.add(j, 0.0F, 0.0F);
        }

        if (f > 0.0F) {
            builder.add(1.0F, method_38857(f, toFloatFunction), 0.0F);
        } else {
            builder.add(1.0F, 0.0F, 0.0F);
        }

        return builder.build();
    }

    private static Spline<NoisePoint> method_38857(float f, ToFloatFunction<Float> toFloatFunction) {
        float g = 0.63F * f;
        float h = 0.3F * f;
        return Spline.builder(LocationFunction.WEIRDNESS, toFloatFunction).add(-0.01F, g, 0.0F).add(0.01F, h, 0.0F).build();
    }

    private static Spline<NoisePoint> buildErosionFactorSpline(float f, boolean bl, ToFloatFunction<Float> toFloatFunction) {
        Spline<NoisePoint> spline = Spline.builder(LocationFunction.WEIRDNESS, toFloatFunction).add(-0.2F, 6.3F, 0.0F).add(0.2F, f, 0.0F).build();
        Spline.Builder<NoisePoint> builder = Spline.builder(LocationFunction.EROSION, toFloatFunction).add(-0.6F, spline, 0.0F).add(-0.5F, Spline.builder(LocationFunction.WEIRDNESS, toFloatFunction).add(-0.05F, 6.3F, 0.0F).add(0.05F, 2.67F, 0.0F).build(), 0.0F).add(-0.35F, spline, 0.0F).add(-0.25F, spline, 0.0F).add(-0.1F, Spline.builder(LocationFunction.WEIRDNESS, toFloatFunction).add(-0.05F, 2.67F, 0.0F).add(0.05F, 6.3F, 0.0F).build(), 0.0F).add(0.03F, spline, 0.0F);
        if (bl) {
            Spline<NoisePoint> spline2 = Spline.builder(LocationFunction.WEIRDNESS, toFloatFunction).add(0.0F, f, 0.0F).add(0.1F, 0.625F, 0.0F).build();
            Spline<NoisePoint> spline3 = Spline.builder(LocationFunction.RIDGES, toFloatFunction).add(-0.9F, f, 0.0F).add(-0.69F, spline2, 0.0F).build();
            builder.add(0.35F, f, 0.0F).add(0.45F, spline3, 0.0F).add(0.55F, spline3, 0.0F).add(0.62F, f, 0.0F);
        } else {
            Spline<NoisePoint> spline2 = Spline.builder(LocationFunction.RIDGES, toFloatFunction).add(-0.7F, spline, 0.0F).add(-0.15F, 1.37F, 0.0F).build();
            Spline<NoisePoint> spline3 = Spline.builder(LocationFunction.RIDGES, toFloatFunction).add(0.45F, spline, 0.0F).add(0.7F, 1.56F, 0.0F).build();
            builder.add(0.05F, spline3, 0.0F).add(0.4F, spline3, 0.0F).add(0.45F, spline2, 0.0F).add(0.55F, spline2, 0.0F).add(0.58F, f, 0.0F);
        }

        return builder.build();
    }

    private static float method_38210(float f, float g, float h, float i) {
        return (g - f) / (i - h);
    }

    private static Spline<NoisePoint> method_38219(float f, boolean bl, ToFloatFunction<Float> toFloatFunction) {
        Spline.Builder<NoisePoint> builder = Spline.builder(LocationFunction.RIDGES, toFloatFunction);
        float i = getOffsetValue(-1.0F, f, -0.7F);
        float k = getOffsetValue(1.0F, f, -0.7F);
        float l = method_38217(f);
        if (-0.65F < l && l < 1.0F) {
            float n = getOffsetValue(-0.65F, f, -0.7F);
            float p = getOffsetValue(-0.75F, f, -0.7F);
            float q = method_38210(i, p, -1.0F, -0.75F);
            builder.add(-1.0F, i, q);
            builder.add(-0.75F, p, 0.0F);
            builder.add(-0.65F, n, 0.0F);
            float r = getOffsetValue(l, f, -0.7F);
            float s = method_38210(r, k, l, 1.0F);
            builder.add(l - 0.01F, r, 0.0F);
            builder.add(l, r, s);
            builder.add(1.0F, k, s);
        } else {
            float n = method_38210(i, k, -1.0F, 1.0F);
            if (bl) {
                builder.add(-1.0F, Math.max(0.2F, i), 0.0F);
                builder.add(0.0F, MathHelper.lerp(0.5F, i, k), n);
            } else {
                builder.add(-1.0F, i, n);
            }

            builder.add(1.0F, k, n);
        }

        return builder.build();
    }

    private static float getOffsetValue(float f, float g, float h) {
        float k = 1.0F - (1.0F - g) * 0.5F;
        float l = 0.5F * (1.0F - g);
        float m = (f + 1.17F) * 0.46082947F;
        float n = m * k - l;
        return f < h ? Math.max(n, -0.2222F) : Math.max(n, 0.0F);
    }

    private static float method_38217(float f) {
        float i = 1.0F - (1.0F - f) * 0.5F;
        float j = 0.5F * (1.0F - f);
        return j / (0.46082947F * i) - 1.17F;
    }

    private static Spline<NoisePoint> createLandSpline(float f, float g, float h, float i, float j, float k, boolean bl, boolean bl2, ToFloatFunction<Float> toFloatFunction) {
        Spline<NoisePoint> spline1 = method_38219(MathHelper.lerp(i, 0.6F, 1.5F), bl2, toFloatFunction);
        Spline<NoisePoint> spline2 = method_38219(MathHelper.lerp(i, 0.6F, 1.0F), bl2, toFloatFunction);
        Spline<NoisePoint> spline3 = method_38219(i, bl2, toFloatFunction);
        Spline<NoisePoint> spline4 = createFlatOffsetSpline(f - 0.15F, 0.5F * i, MathHelper.lerp(0.5F, 0.5F, 0.5F) * i, 0.5F * i, 0.6F * i, 0.5F, toFloatFunction);
        Spline<NoisePoint> spline5 = createFlatOffsetSpline(f, j * i, g * i, 0.5F * i, 0.6F * i, 0.5F, toFloatFunction);
        Spline<NoisePoint> spline6 = createFlatOffsetSpline(f, j, j, g, h, 0.5F, toFloatFunction);
        Spline<NoisePoint> spline7 = createFlatOffsetSpline(f, j, j, g, h, 0.5F, toFloatFunction);
        Spline<NoisePoint> spline8 = Spline.builder(LocationFunction.RIDGES, toFloatFunction).add(-1.0F, f, 0.0F).add(-0.4F, spline6, 0.0F).add(0.0F, h + 0.07F, 0.0F).build();
        Spline<NoisePoint> spline9 = createFlatOffsetSpline(-0.02F, k, k, g, h, 0.0F, toFloatFunction);
        Spline.Builder<NoisePoint> builder = Spline.builder(LocationFunction.EROSION, toFloatFunction).add(-0.85F, spline1, 0.0F).add(-0.7F, spline2, 0.0F).add(-0.4F, spline3, 0.0F).add(-0.35F, spline4, 0.0F).add(-0.1F, spline5, 0.0F).add(0.2F, spline6, 0.0F);
        if (bl) {
            builder.add(0.4F, spline7, 0.0F).add(0.45F, spline8, 0.0F).add(0.55F, spline8, 0.0F).add(0.58F, spline7, 0.0F);
        }
        builder.add(0.7F, spline9, 0.0F);
        return builder.build();
    }

    private static Spline<NoisePoint> createFlatOffsetSpline(float f, float g, float h, float i, float j, float k, ToFloatFunction<Float> toFloatFunction) {
        float l = Math.max(0.5F * (g - f), k);
        float m = 5.0F * (h - g);
        return Spline.builder(LocationFunction.RIDGES, toFloatFunction).add(-1.0F, f, l).add(-0.4F, g, Math.min(l, m)).add(0.0F, h, m).add(0.4F, i, 2.0F * (i - h)).add(1.0F, j, 0.7F * (j - i)).build();
    }

    public float getOffset(NoisePoint noisePoint) {
        return this.offsetSpline.apply(noisePoint) - 0.50375F;
    }

    public float getFactor(NoisePoint noisePoint) {
        return this.factorSpline.apply(noisePoint);
    }

    public float getPeak(NoisePoint noisePoint) {
        return this.peakSpline.apply(noisePoint);
    }

    public NoisePoint createNoisePoint(float f, float g, float h) {
        return new NoisePoint(f, g, getNormalizedWeirdness(h), h);
    }

    public static float getNormalizedWeirdness(float f) {
        return -(Math.abs(Math.abs(f) - 0.6666667F) - 0.33333334F) * 3.0F;
    }

    static {
        field_35673 = (float_) -> float_;
    }

    @VisibleForTesting
    protected enum LocationFunction implements ToFloatFunction<NoisePoint> {
        CONTINENTS(NoisePoint::continentalnessNoise, "continents"),
        EROSION(NoisePoint::erosionNoise, "erosion"),
        WEIRDNESS(NoisePoint::weirdnessNoise, "weirdness"),
        RIDGES(NoisePoint::normalizedWeirdness, "ridges");

        private final ToFloatFunction<NoisePoint> noiseFunction;
        private final String id;

        LocationFunction(ToFloatFunction<NoisePoint> toFloatFunction, String string2) {
            this.noiseFunction = toFloatFunction;
            this.id = string2;
        }

        public String toString() {
            return this.id;
        }

        public float apply(NoisePoint noisePoint) {
            return this.noiseFunction.apply(noisePoint);
        }
    }

    public record NoisePoint(float continentalnessNoise, float erosionNoise, float normalizedWeirdness, float weirdnessNoise) {}
}
