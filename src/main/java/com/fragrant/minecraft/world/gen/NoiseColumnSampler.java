package com.fragrant.minecraft.world.gen;

import com.fragrant.minecraft.world.biome.source.TerrainNoisePoint;
import com.fragrant.minecraft.world.biome.source.VanillaTerrainParameters;
import com.fragrant.minecraft.world.gen.random.RandomDeriver;
import com.fragrant.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import com.fragrant.minecraft.util.math.MathHelper;
import com.fragrant.minecraft.world.biome.source.BiomeCoords;
import com.fragrant.minecraft.util.Identifier;
import com.fragrant.minecraft.util.math.ChunkPos;
import com.fragrant.minecraft.util.math.ChunkSectionPos;

public class NoiseColumnSampler {
    private final VanillaTerrainParameters vanillaTerrainParameters;
    private final ChunkNoiseSampler.ValueSamplerFactory initialNoiseSampler;
    private final NoiseSampler terrainNoise;
    private final DoublePerlinNoiseSampler aquiferBarrierNoise;
    private final DoublePerlinNoiseSampler aquiferFluidLevelFloodednessNoise;
    private final DoublePerlinNoiseSampler aquiferFluidLevelSpreadNoise;
    private final DoublePerlinNoiseSampler aquiferLavaNoise;
    private final DoublePerlinNoiseSampler caveLayerNoise;
    private final DoublePerlinNoiseSampler pillarNoise;
    private final DoublePerlinNoiseSampler pillarRarenessNoise;
    private final DoublePerlinNoiseSampler pillarThicknessNoise;
    private final DoublePerlinNoiseSampler spaghetti2dNoise;
    private final DoublePerlinNoiseSampler spaghetti2dElevationNoise;
    private final DoublePerlinNoiseSampler spaghetti2dModulatorNoise;
    private final DoublePerlinNoiseSampler spaghetti2dThicknessNoise;
    private final DoublePerlinNoiseSampler spaghetti3dFirstNoise;
    private final DoublePerlinNoiseSampler spaghetti3dSecondNoise;
    private final DoublePerlinNoiseSampler spaghetti3dRarityNoise;
    private final DoublePerlinNoiseSampler spaghetti3dThicknessNoise;
    private final DoublePerlinNoiseSampler spaghettiRoughnessNoise;
    private final DoublePerlinNoiseSampler spaghettiRoughnessModulatorNoise;
    private final DoublePerlinNoiseSampler caveEntranceNoise;
    private final DoublePerlinNoiseSampler caveCheeseNoise;
    private final DoublePerlinNoiseSampler continentalnessNoise;
    private final DoublePerlinNoiseSampler erosionNoise;
    private final DoublePerlinNoiseSampler weirdnessNoise;
    private final DoublePerlinNoiseSampler shiftNoise;
    private final ChunkNoiseSampler.ValueSamplerFactory noodleNoiseFactory;
    private final ChunkNoiseSampler.ValueSamplerFactory noodleThicknessNoiseFactory;
    private final ChunkNoiseSampler.ValueSamplerFactory noodleRidgeFirstNoiseFactory;
    private final ChunkNoiseSampler.ValueSamplerFactory noodleRidgeSecondNoiseFactory;
    private final RandomDeriver aquiferRandomDeriver;

    public NoiseColumnSampler(RandomDeriver randomDeriver) {
        this.vanillaTerrainParameters = VanillaTerrainParameters.createSurfaceParameters();
        this.initialNoiseSampler = (chunkNoiseSampler)
                -> chunkNoiseSampler.createNoiseInterpolator((i, j, k)
                -> this.sampleNoiseColumn(
                i, j, k, chunkNoiseSampler.createMultiNoisePoint(
                        BiomeCoords.fromBlock(i),
                        BiomeCoords.fromBlock(k)
                ).comp_246()
        ));
        this.terrainNoise = new NoiseSampler(
                randomDeriver.createRandom(new Identifier("terrain")),
                new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0),
                4, 8);
        this.shiftNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("offset")),
                -3,
                1.0, 1.0, 1.0, 0.0);
        this.aquiferRandomDeriver = randomDeriver.createRandom(new Identifier("aquifer")).createRandomDeriver();
        this.aquiferBarrierNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("aquifer_barrier")),
                -3,
                1.0);
        this.aquiferFluidLevelFloodednessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("aquifer_fluid_level_floodedness")),
                -7,
                1.0);
        this.aquiferLavaNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("aquifer_lava")),
                -1,
                1.0);
        this.aquiferFluidLevelSpreadNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("aquifer_fluid_level_spread")),
                -5,
                1.0);
        this.pillarNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("pillar")),
                -7,
                1.0, 1.0);
        this.pillarRarenessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("pillar_rareness")),
                -8,
                1.0);
        this.pillarThicknessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("pillar_thickness")),
                -8,
                1.0);
        this.spaghetti2dNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_2d")),
                -7,
                1.0);
        this.spaghetti2dElevationNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_2d_elevation")),
                -8,
                1.0);
        this.spaghetti2dModulatorNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_2d_modulator")),
                -11,
                1.0);
        this.spaghetti2dThicknessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_2d_thickness")),
                -11,
                1.0);
        this.spaghetti3dFirstNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_3d_1")),
                -7,
                1.0);
        this.spaghetti3dSecondNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_3d_2")),
                -7,
                1.0);
        this.spaghetti3dRarityNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_3d_rarity")),
                -11,
                1.0);
        this.spaghetti3dThicknessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_3d_thickness")),
                -8,
                1.0);
        this.spaghettiRoughnessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_roughness")),
                -5,
                1.0);
        this.spaghettiRoughnessModulatorNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("spaghetti_roughness_modulator")),
                -8,
                1.0);
        this.caveEntranceNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("cave_entrance")),
                -7,
                0.75, 0.5, 1.0, 0.0);
        this.caveLayerNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("cave_layer")),
                -8,
                1.0);
        this.caveCheeseNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("cave_cheese")),
                -8,
                0.5, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 2.0, 0.0);
        this.continentalnessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("continentalness")),
                -9,
                1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0);
        this.erosionNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("erosion")),
                -9,
                1.0, 1.0, 0.0, 2.0, 1.0);
        this.weirdnessNoise = DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("ridge")),
                -7,
                1.0, 2.0, 1.0, 0.0, 0.0, 0.0);
        this.noodleNoiseFactory = createNoiseSamplerFactory(DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("noodle")),
                -8,
                1.0), -1, 1.0);
        this.noodleThicknessNoiseFactory = createNoiseSamplerFactory(DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("noodle_thickness")),
                -8,
                1.0), 0, 1.0);
        this.noodleRidgeFirstNoiseFactory = createNoiseSamplerFactory(DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("noodle_ridge_a")),
                -7,
                1.0), 0, 2.6666666666666665);
        this.noodleRidgeSecondNoiseFactory = createNoiseSamplerFactory(DoublePerlinNoiseSampler.create(
                randomDeriver.createRandom(new Identifier("noodle_ridge_b")),
                -7,
                1.0), 0, 2.6666666666666665);
    }

    private static ChunkNoiseSampler.ValueSamplerFactory createNoiseSamplerFactory(
            DoublePerlinNoiseSampler doublePerlinNoiseSampler, int k, double d
    ) {
        ChunkNoiseSampler.ColumnSampler columnSampler = (l, m, n) ->
                m <= 320 && m >= -60 ? doublePerlinNoiseSampler.sample(l * d, m * d, n * d) : k;
        return (chunkNoiseSampler) -> chunkNoiseSampler.createNoiseInterpolator(columnSampler);
    }

    public double sampleNoiseColumn(int i, int j, int k, TerrainNoisePoint terrainNoisePoint) {
        double d = this.terrainNoise.calculateNoise(i, j, k);
        return this.sampleNoiseColumn(i, j, k, terrainNoisePoint, d, false);
    }

    private double sampleNoiseColumn(int i, int j, int k, TerrainNoisePoint terrainNoisePoint, double d, boolean bl) {
        double g = (this.method_39331(j, terrainNoisePoint) + 0.0F) * terrainNoisePoint.factor();
        double e = g * (g > 0.0F ? 4 : 1);
        double f = e + d;
        double h;
        double l;
        double m;
        if (!bl && !(f < -64.0F)) {
            double n = f - 1.5625F;
            boolean bl3 = n < 0.0F;
            double o = this.sampleCaveEntranceNoise(i, j, k);
            double p = this.sampleSpaghettiRoughnessNoise(i, j, k);
            double q = this.sampleSpaghetti3dNoise(i, j, k);
            double r = Math.min(o, q + p);
            if (bl3) {
                h = f;
                l = r * 5.0F;
                m = -64.0F;
            } else {
                double s = this.sampleCaveLayerNoise(i, j, k);
                if (s > 64.0F) {
                    h = 64.0F;
                } else {
                    double t = this.caveCheeseNoise.sample(i, j / 1.5F, k);
                    double u = MathHelper.clamp(t + 0.27, -1.0F, 1.0F);
                    double v = n * 1.28;
                    double w = u + MathHelper.clampedLerp(0.5F, 0.0F, v);
                    h = w + s;
                }

                double t = this.sampleSpaghetti2dNoise(i, j, k);
                l = Math.min(r, t + p);
                m = this.samplePillarNoise(i, j, k);
            }
        } else {
            h = f;
            l = 64.0F;
            m = -64.0F;
        }

        double n = Math.max(Math.min(h, l), m);
        n = this.applySlides(n, j / 8);
        n = MathHelper.clamp(n, -64.0F, 64.0F);
        return n;
    }

    private double method_39331(int i, TerrainNoisePoint terrainNoisePoint) {
        double d = 1.0F - i / 128.0F;
        return d + terrainNoisePoint.offset();
    }

    private double applySlides(double d, int i) {
        d = new SlideConfig(-0.078125, 2, 8).method_38414(d, 56 - i);
        d = new SlideConfig(0.1171875, 3, 0).method_38414(d, 8 + i);
        return d;
    }

    protected ChunkNoiseSampler.BlockStateSampler createInitialNoiseBlockStateSampler(ChunkNoiseSampler chunkNoiseSampler, ChunkNoiseSampler.ColumnSampler columnSampler) {
        ChunkNoiseSampler.ValueSampler valueSampler1 = this.initialNoiseSampler.create(chunkNoiseSampler);
        ChunkNoiseSampler.ValueSampler valueSampler2 = this.noodleNoiseFactory.create(chunkNoiseSampler);
        ChunkNoiseSampler.ValueSampler valueSampler3 = this.noodleThicknessNoiseFactory.create(chunkNoiseSampler);
        ChunkNoiseSampler.ValueSampler valueSampler4 = this.noodleRidgeFirstNoiseFactory.create(chunkNoiseSampler);
        ChunkNoiseSampler.ValueSampler valueSampler5 = this.noodleRidgeSecondNoiseFactory.create(chunkNoiseSampler);
        return (i, j, k) -> {
            double d = valueSampler1.sample();
            double e = MathHelper.clamp(d * 0.64, -1.0F, 1.0F);
            e = e / 2.0F - e * e * e / 24.0F;
            if (valueSampler2.sample() >= 0.0F) {
                double h = MathHelper.clampedLerpFromProgress(valueSampler3.sample(), -1.0F, 1.0F, 0.05, 0.1);
                double l = Math.abs(1.5F * valueSampler4.sample()) - h;
                double m = Math.abs(1.5F * valueSampler5.sample()) - h;
                e = Math.min(e, Math.max(l, m));
            }

            e += columnSampler.calculateNoise(i, j, k);
            return chunkNoiseSampler.getAquiferSampler().apply(i, j, k, d, e);
        };
    }

    protected int method_38383(int i, int j, TerrainNoisePoint terrainNoisePoint) {
        for(int k = 40; k >= -8; --k) {
            int l = k * 8;
            double e = this.sampleNoiseColumn(i, l, j, terrainNoisePoint, -0.703125F, true);
            if (e > 0.390625F) {
                return l;
            }
        }
        return Integer.MAX_VALUE;
    }

    protected AquiferSampler createAquiferSampler(ChunkNoiseSampler chunkNoiseSampler, int i, int j, AquiferSampler.FluidLevelSampler fluidLevelSampler) {
        return AquiferSampler.aquifer(
                chunkNoiseSampler,
                new ChunkPos(
                        ChunkSectionPos.getSectionCoord(i),
                        ChunkSectionPos.getSectionCoord(j)
                ),
                this.aquiferBarrierNoise,
                this.aquiferFluidLevelFloodednessNoise,
                this.aquiferFluidLevelSpreadNoise,
                this.aquiferLavaNoise,
                this.aquiferRandomDeriver,
                fluidLevelSampler
        );
    }

    public ChunkNoiseSampler.class_6747 method_39330(int i, int j) {
        double d = i + this.sampleShiftNoise(i, 0, j);
        double e = j + this.sampleShiftNoise(j, i, 0);
        double f = this.sampleContinentalnessNoise(d, 0.0F, e);
        double g = this.sampleWeirdnessNoise(d, 0.0F, e);
        double h = this.sampleErosionNoise(d, 0.0F, e);
        TerrainNoisePoint terrainNoisePoint = this.createTerrainNoisePoint((float)f, (float)g, (float)h);
        return new ChunkNoiseSampler.class_6747(d, e, f, g, h, terrainNoisePoint);
    }

    public TerrainNoisePoint createTerrainNoisePoint(float f, float g, float h) {
        VanillaTerrainParameters.NoisePoint noisePoint = this.vanillaTerrainParameters.createNoisePoint(f, h, g);
        float k = this.vanillaTerrainParameters.getOffset(noisePoint);
        float l = this.vanillaTerrainParameters.getFactor(noisePoint);
        float m = this.vanillaTerrainParameters.getPeak(noisePoint);
        return new TerrainNoisePoint(k, l, m);
    }

    public double sampleShiftNoise(int i, int j, int k) {
        return this.shiftNoise.sample(i, j, k) * 4.0F;
    }

    public double sampleContinentalnessNoise(double d, double e, double f) {
        return this.continentalnessNoise.sample(d, e, f);
    }

    public double sampleErosionNoise(double d, double e, double f) {
        return this.erosionNoise.sample(d, e, f);
    }

    public double sampleWeirdnessNoise(double d, double e, double f) {
        return this.weirdnessNoise.sample(d, e, f);
    }

    private double sampleCaveEntranceNoise(int i, int j, int k) {
        double g = this.caveEntranceNoise.sample(i * 0.75F, j * 0.5F, k * 0.75F) + 0.37;
        double h = (j + 10) / 40.0F;
        return g + MathHelper.clampedLerp(0.3, 0.0F, h);
    }

    private double samplePillarNoise(int i, int j, int k) {
        double f = NoiseHelper.lerpFromProgress(this.pillarRarenessNoise, i, j, k, 0.0F, 2.0F);
        double l = NoiseHelper.lerpFromProgress(this.pillarThicknessNoise, i, j, k, 0.0F, 1.1);
        l = Math.pow(l, 3.0F);
        double o = this.pillarNoise.sample(i * 25.0F, j * 0.3, k * 25.0F);
        o = l * (o * 2.0F - f);
        return o > 0.03 ? o : Double.NEGATIVE_INFINITY;
    }

    private double sampleCaveLayerNoise(int i, int j, int k) {
        double d = this.caveLayerNoise.sample(i, j * 8, k);
        return MathHelper.square(d) * 4.0F;
    }

    private double sampleSpaghetti3dNoise(int i, int j, int k) {
        double d = this.spaghetti3dRarityNoise.sample(i * 2, j, k * 2);
        double e = ChunkNoiseSampler.CaveScaler.scaleTunnels(d);
        double h = NoiseHelper.lerpFromProgress(this.spaghetti3dThicknessNoise, i, j, k, 0.065, 0.088);
        double l = sample(this.spaghetti3dFirstNoise, i, j, k, e);
        double m = Math.abs(e * l) - h;
        double n = sample(this.spaghetti3dSecondNoise, i, j, k, e);
        double o = Math.abs(e * n) - h;
        return clampBetweenNoiseRange(Math.max(m, o));
    }

    private double sampleSpaghetti2dNoise(int i, int j, int k) {
        int i2 = i * 2;
        int k2 = k * 2;
        double d = this.spaghetti2dModulatorNoise.sample(i2, j, k2);
        double e = ChunkNoiseSampler.CaveScaler.scaleCaves(d);
        double h = NoiseHelper.lerpFromProgress(this.spaghetti2dThicknessNoise, i2, j, k2, 0.6, 1.3);
        double l = sample(this.spaghetti2dNoise, i, j, k, e);
        double n = Math.abs(e * l) - 0.083 * h;
        double q = NoiseHelper.lerpFromProgress(this.spaghetti2dElevationNoise, i, 0.0F, k, -8, 8.0F);
        double r = Math.abs(q -  j /  8.0F) - h;
        r = r * r * r;
        return clampBetweenNoiseRange(Math.max(r, n));
    }

    private double sampleSpaghettiRoughnessNoise(int i, int j, int k) {
        double d = NoiseHelper.lerpFromProgress(this.spaghettiRoughnessModulatorNoise, i, j, k, 0.0F, 0.1);
        return (0.4 - Math.abs(this.spaghettiRoughnessNoise.sample(i, j, k))) * d;
    }

    private static double clampBetweenNoiseRange(double d) {
        return MathHelper.clamp(d, -1.0F, 1.0F);
    }

    private static double sample(DoublePerlinNoiseSampler doublePerlinNoiseSampler, double d, double e, double f, double g) {
        return doublePerlinNoiseSampler.sample(d / g, e / g, f / g);
    }
}