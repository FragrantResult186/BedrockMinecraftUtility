package com.fragrant.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.fragrant.minecraft.block.BlockState;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import com.fragrant.minecraft.util.math.ChunkPos;
import com.fragrant.minecraft.util.math.MathHelper;
import com.fragrant.minecraft.world.biome.source.BiomeCoords;
import com.fragrant.minecraft.world.biome.source.TerrainNoisePoint;
import com.fragrant.minecraft.world.chunk.Chunk;

import java.util.List;
import java.util.function.Supplier;

public class ChunkNoiseSampler {
    final int x;
    final int z;
    private final int biomeX;
    private final int biomeZ;
    final List<NoiseInterpolator> interpolators;
    private final NoiseColumnSampler field_36272;
    private final class_6747[][] field_35486;
    private final Long2IntMap field_36273 = new Long2IntOpenHashMap();
    private final AquiferSampler aquiferSampler;
    private final BlockStateSampler initialNoiseBlockStateSampler;

    public static ChunkNoiseSampler create(
            Chunk chunk,
            NoiseColumnSampler noiseColumnSampler,
            Supplier<ColumnSampler> supplier,
            AquiferSampler.FluidLevelSampler fluidLevelSampler
    ) {
        ChunkPos chunkPos = chunk.getPos();
        return new ChunkNoiseSampler(
                noiseColumnSampler,
                chunkPos.getStartX(),
                chunkPos.getStartZ(),
                supplier.get(),
                fluidLevelSampler
        );
    }

    private ChunkNoiseSampler(
            NoiseColumnSampler noiseColumnSampler,
            int l, int m,
            ColumnSampler columnSampler,
            AquiferSampler.FluidLevelSampler fluidLevelSampler
    ) {
        this.field_36272 = noiseColumnSampler;
        this.x = Math.floorDiv(l, 4);
        this.z = Math.floorDiv(m, 4);
        this.interpolators = Lists.newArrayList();
        this.biomeX = BiomeCoords.fromBlock(l);
        this.biomeZ = BiomeCoords.fromBlock(m);
        int o = BiomeCoords.fromBlock(16);
        this.field_35486 = new class_6747[o + 1][];

        for (int p = 0; p <= o; ++p) {
            int q = this.biomeX + p;
            this.field_35486[p] = new class_6747[o + 1];

            for (int r = 0; r <= o; ++r) {
                int s = this.biomeZ + r;
                this.field_35486[p][r] = noiseColumnSampler.method_39330(q, s);
            }
        }

        this.aquiferSampler = noiseColumnSampler.createAquiferSampler(this, l, m, fluidLevelSampler);
        this.initialNoiseBlockStateSampler = noiseColumnSampler.createInitialNoiseBlockStateSampler(this, columnSampler);
    }

    public class_6747 createMultiNoisePoint(int i, int j) {
        return this.field_35486[i - this.biomeX][j - this.biomeZ];
    }

    public int method_39900(int i, int j) {
        return this.field_36273.computeIfAbsent(ChunkPos.toLong(BiomeCoords.fromBlock(i), BiomeCoords.fromBlock(j)), this::method_39899);
    }

    private int method_39899(long l) {
        int i = ChunkPos.getPackedX(l);
        int j = ChunkPos.getPackedZ(l);
        int k = i - this.biomeX;
        int m = j - this.biomeZ;
        int n = this.field_35486.length;
        TerrainNoisePoint terrainNoisePoint;
        if (k >= 0 && m >= 0 && k < n && m < n) {
            terrainNoisePoint = this.field_35486[k][m].comp_246();
        } else {
            terrainNoisePoint = this.field_36272.method_39330(i, j).comp_246();
        }

        return this.field_36272.method_38383(BiomeCoords.toBlock(i), BiomeCoords.toBlock(j), terrainNoisePoint);
    }

    protected NoiseInterpolator createNoiseInterpolator(ColumnSampler columnSampler) {
        return new NoiseInterpolator(this, columnSampler);
    }

    public void sampleStartNoise() {
        this.interpolators.forEach(NoiseInterpolator::sampleStartNoise);
    }

    public void sampleEndNoise(int i) {
        this.interpolators.forEach((noiseInterpolator) -> noiseInterpolator.sampleEndNoise(i));
    }

    public void sampleNoiseCorners(int i, int j) {
        this.interpolators.forEach((noiseInterpolator) -> noiseInterpolator.sampleNoiseCorners(i, j));
    }

    public void sampleNoiseY(double d) {
        this.interpolators.forEach((noiseInterpolator) -> noiseInterpolator.sampleNoiseY(d));
    }

    public void sampleNoiseX(double d) {
        this.interpolators.forEach((noiseInterpolator) -> noiseInterpolator.sampleNoiseX(d));
    }

    public void sampleNoise(double d) {
        this.interpolators.forEach((noiseInterpolator) -> noiseInterpolator.sampleNoise(d));
    }

    public void swapBuffers() {
        this.interpolators.forEach(NoiseInterpolator::swapBuffers);
    }

    public AquiferSampler getAquiferSampler() {
        return this.aquiferSampler;
    }

    protected BlockState sampleInitialNoiseBlockState(int i, int j, int k) {
        return this.initialNoiseBlockStateSampler.sample(i, j, k);
    }

    public class NoiseInterpolator implements ValueSampler {
        private double[][] startNoiseBuffer;
        private double[][] endNoiseBuffer;
        private final ColumnSampler columnSampler;
        private double x0y0z0;
        private double x0y0z1;
        private double x1y0z0;
        private double x1y0z1;
        private double x0y1z0;
        private double x0y1z1;
        private double x1y1z0;
        private double x1y1z1;
        private double x0z0;
        private double x1z0;
        private double x0z1;
        private double x1z1;
        private double z0;
        private double z1;
        private double result;

        NoiseInterpolator(ChunkNoiseSampler chunkNoiseSampler, ColumnSampler columnSampler) {
            this.columnSampler = columnSampler;
            this.startNoiseBuffer = this.createBuffer(48, 4);
            this.endNoiseBuffer = this.createBuffer(48, 4);
            chunkNoiseSampler.interpolators.add(this);
        }

        private double[][] createBuffer(int i, int j) {
            int k = j + 1;
            int l = i + 1;
            double[][] ds = new double[k][l];

            for (int m = 0; m < k; ++m) {
                ds[m] = new double[l];
            }

            return ds;
        }

        void sampleStartNoise() {
            this.sampleNoise(this.startNoiseBuffer, ChunkNoiseSampler.this.x);
        }

        void sampleEndNoise(int i) {
            this.sampleNoise(this.endNoiseBuffer, ChunkNoiseSampler.this.x + i + 1);
        }

        private void sampleNoise(double[][] ds, int i) {
            for (int l = 0; l < 5; ++l) {
                int m = ChunkNoiseSampler.this.z + l;
                for (int n = 0; n < 49; ++n) {
                    int p = (n - 8) * 8;
                    double d = this.columnSampler.calculateNoise(i * 4, p, m * 4);
                    ds[l][n] = d;
                }
            }
        }

        void sampleNoiseCorners(int i, int j) {
            this.x0y0z0 = this.startNoiseBuffer[j][i];
            this.x0y0z1 = this.startNoiseBuffer[j + 1][i];
            this.x1y0z0 = this.endNoiseBuffer[j][i];
            this.x1y0z1 = this.endNoiseBuffer[j + 1][i];
            this.x0y1z0 = this.startNoiseBuffer[j][i + 1];
            this.x0y1z1 = this.startNoiseBuffer[j + 1][i + 1];
            this.x1y1z0 = this.endNoiseBuffer[j][i + 1];
            this.x1y1z1 = this.endNoiseBuffer[j + 1][i + 1];
        }

        void sampleNoiseY(double d) {
            this.x0z0 = MathHelper.lerp(d, this.x0y0z0, this.x0y1z0);
            this.x1z0 = MathHelper.lerp(d, this.x1y0z0, this.x1y1z0);
            this.x0z1 = MathHelper.lerp(d, this.x0y0z1, this.x0y1z1);
            this.x1z1 = MathHelper.lerp(d, this.x1y0z1, this.x1y1z1);
        }

        void sampleNoiseX(double d) {
            this.z0 = MathHelper.lerp(d, this.x0z0, this.x1z0);
            this.z1 = MathHelper.lerp(d, this.x0z1, this.x1z1);
        }

        void sampleNoise(double d) {
            this.result = MathHelper.lerp(d, this.z0, this.z1);
        }

        public double sample() {
            return this.result;
        }

        private void swapBuffers() {
            double[][] ds = this.startNoiseBuffer;
            this.startNoiseBuffer = this.endNoiseBuffer;
            this.endNoiseBuffer = ds;
        }
    }

    @FunctionalInterface
    public interface BlockStateSampler {
        BlockState sample(int i, int j, int k);
    }

    @FunctionalInterface
    public interface ColumnSampler {
        double calculateNoise(int i, int j, int k);
    }

    @FunctionalInterface
    public interface ValueSampler {
        double sample();
    }

    @FunctionalInterface
    public interface ValueSamplerFactory {
        ValueSampler create(ChunkNoiseSampler chunkNoiseSampler);
    }

    public record class_6747(double comp_241, double comp_242, double comp_243, double comp_244, double comp_245, TerrainNoisePoint comp_246) {}

    static final class CaveScaler {
        static double scaleCaves(double d) {
            if (d < -0.75F) {
                return 0.5F;
            } else if (d < -0.5F) {
                return 0.75F;
            } else if (d < 0.5F) {
                return 1.0F;
            } else {
                return d < 0.75F ? 2.0F : 3.0F;
            }
        }

        static double scaleTunnels(double d) {
            if (d < -0.5F) {
                return 0.75F;
            } else if (d < 0.0F) {
                return 1.0F;
            } else {
                return d < 0.5F ? 1.5F : 2.0F;
            }
        }
    }
}