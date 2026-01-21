package com.fragrant.minecraft.world.gen;

import com.fragrant.minecraft.util.math.BlockPos;
import com.fragrant.minecraft.util.math.ChunkPos;
import com.fragrant.minecraft.util.math.ChunkSectionPos;
import com.fragrant.minecraft.util.math.MathHelper;
import com.fragrant.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import com.fragrant.minecraft.world.gen.random.AbstractRandom;
import com.fragrant.minecraft.world.gen.random.RandomDeriver;
import com.fragrant.minecraft.block.BlockState;
import com.fragrant.minecraft.block.Blocks;
import org.apache.commons.lang3.mutable.MutableDouble;

import java.util.Arrays;

public interface AquiferSampler {
    static AquiferSampler aquifer(ChunkNoiseSampler chunkNoiseSampler, ChunkPos chunkPos, DoublePerlinNoiseSampler doublePerlinNoiseSampler, DoublePerlinNoiseSampler doublePerlinNoiseSampler2, DoublePerlinNoiseSampler doublePerlinNoiseSampler3, DoublePerlinNoiseSampler doublePerlinNoiseSampler4, RandomDeriver randomDeriver, FluidLevelSampler fluidLevelSampler) {
        return new Impl(chunkNoiseSampler, chunkPos, doublePerlinNoiseSampler, doublePerlinNoiseSampler2, doublePerlinNoiseSampler3, doublePerlinNoiseSampler4, randomDeriver, -64, 384, fluidLevelSampler);
    }

    BlockState apply(int i, int j, int k, double d, double e);

    class Impl implements AquiferSampler, FluidLevelSampler {
        private final ChunkNoiseSampler chunkNoiseSampler;
        private final DoublePerlinNoiseSampler barrierNoise;
        private final DoublePerlinNoiseSampler fluidLevelFloodednessNoise;
        private final DoublePerlinNoiseSampler fluidLevelSpreadNoise;
        private final DoublePerlinNoiseSampler fluidTypeNoise;
        private final RandomDeriver randomDeriver;
        private final FluidLevel[] waterLevels;
        private final long[] blockPositions;
        private final FluidLevelSampler fluidLevelSampler;
        private final int startX;
        private final int startY;
        private final int startZ;
        private final int sizeX;
        private final int sizeZ;
        private static final int[][] field_34581 = new int[][]{{-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {-3, 0}, {-2, 0}, {-1, 0}, {0, 0}, {1, 0}, {-2, 1}, {-1, 1}, {0, 1}, {1, 1}};

        Impl(ChunkNoiseSampler chunkNoiseSampler, ChunkPos chunkPos, DoublePerlinNoiseSampler doublePerlinNoiseSampler1, DoublePerlinNoiseSampler doublePerlinNoiseSampler2, DoublePerlinNoiseSampler doublePerlinNoiseSampler3, DoublePerlinNoiseSampler doublePerlinNoiseSampler4, RandomDeriver randomDeriver, int i, int j, FluidLevelSampler fluidLevelSampler) {
            this.chunkNoiseSampler = chunkNoiseSampler;
            this.barrierNoise = doublePerlinNoiseSampler1;
            this.fluidLevelFloodednessNoise = doublePerlinNoiseSampler2;
            this.fluidLevelSpreadNoise = doublePerlinNoiseSampler3;
            this.fluidTypeNoise = doublePerlinNoiseSampler4;
            this.fluidLevelSampler = fluidLevelSampler;
            this.randomDeriver = randomDeriver;
            this.startX = this.getLocalX(chunkPos.getStartX()) - 1;
            this.startY = this.getLocalY(i) - 1;
            this.startZ = this.getLocalZ(chunkPos.getStartZ()) - 1;
            this.sizeX = this.getLocalX(chunkPos.getEndX()) - this.startX + 2;
            this.sizeZ = this.getLocalZ(chunkPos.getEndZ()) - this.startZ + 2;
            int k = this.getLocalY(i + j) - this.startY + 2;
            int l = k * this.sizeX * this.sizeZ;
            this.waterLevels = new FluidLevel[l];
            this.blockPositions = new long[l];
            Arrays.fill(this.blockPositions, Long.MAX_VALUE);
        }

        private int index(int i, int j, int k) {
            int l = i - this.startX;
            int m = j - this.startY;
            int n = k - this.startZ;
            return (m * this.sizeZ + n) * this.sizeX + l;
        }

        public BlockState apply(int i, int j, int k, double d, double e) {
            if (d <= -64.0F) {
                return this.fluidLevelSampler.getFluidLevel(i, j, k).getBlockState(j);
            } else {
                if (e <= 0.0F) {
                    FluidLevel fluidLevel = this.fluidLevelSampler.getFluidLevel(i, j, k);
                    double f;
                    BlockState blockState;
                    if (fluidLevel.getBlockState(j).getBlock().equals(Blocks.LAVA)) {
                        blockState = new BlockState(Blocks.LAVA);
                        f = 0.0F;
                    } else {
                        int l = Math.floorDiv(i - 5, 16);
                        int m = Math.floorDiv(j + 1, 12);
                        int n = Math.floorDiv(k - 5, 16);
                        int o = Integer.MAX_VALUE;
                        int p = Integer.MAX_VALUE;
                        int q = Integer.MAX_VALUE;
                        long r = 0L;
                        long s = 0L;
                        long t = 0L;

                        for(int u = 0; u <= 1; ++u) {
                            for(int v = -1; v <= 1; ++v) {
                                for(int w = 0; w <= 1; ++w) {
                                    int x = l + u;
                                    int y = m + v;
                                    int z = n + w;
                                    int aa = this.index(x, y, z);
                                    long ab = this.blockPositions[aa];
                                    long ac;
                                    if (ab != Long.MAX_VALUE) {
                                        ac = ab;
                                    } else {
                                        AbstractRandom abstractRandom = this.randomDeriver.createRandom(x, y, z);
                                        ac = BlockPos.asLong(
                                                x * 16 + abstractRandom.nextInt(10),
                                                y * 12 + abstractRandom.nextInt(9),
                                                z * 16 + abstractRandom.nextInt(10)
                                        );
                                        this.blockPositions[aa] = ac;
                                    }

                                    int abstractRandom = BlockPos.unpackLongX(ac) - i;
                                    int ad = BlockPos.unpackLongY(ac) - j;
                                    int ae = BlockPos.unpackLongZ(ac) - k;
                                    int af = abstractRandom * abstractRandom + ad * ad + ae * ae;
                                    if (o >= af) {
                                        t = s;
                                        s = r;
                                        r = ac;
                                        q = p;
                                        p = o;
                                        o = af;
                                    } else if (p >= af) {
                                        t = s;
                                        s = ac;
                                        q = p;
                                        p = af;
                                    } else if (q >= af) {
                                        t = ac;
                                        q = af;
                                    }
                                }
                            }
                        }

                        FluidLevel fluidLevel1 = this.getWaterLevel(r);
                        FluidLevel fluidLevel2 = this.getWaterLevel(s);
                        FluidLevel fluidLevel3 = this.getWaterLevel(t);
                        double dist12 = maxDistance(o, p);
                        double dist13 = maxDistance(o, q);
                        double dist23 = maxDistance(p, q);
                        if (fluidLevel1.getBlockState(j).getBlock().equals(Blocks.WATER) && this.fluidLevelSampler.getFluidLevel(i, j - 1, k).getBlockState(j - 1).getBlock().equals(Blocks.LAVA)) {
                            f = 1.0F;
                        } else if (dist12 > -1.0F) {
                            MutableDouble ab = new MutableDouble(Double.NaN);
                            double g = this.calculateDensity(i, j, k, ab, fluidLevel1, fluidLevel2);
                            double ad = this.calculateDensity(i, j, k, ab, fluidLevel1, fluidLevel3);
                            double af = this.calculateDensity(i, j, k, ab, fluidLevel2, fluidLevel3);
                            double h = Math.max(0.0F, dist12);
                            double ag = Math.max(0.0F, dist13);
                            double ah = Math.max(0.0F, dist23);
                            double ai = 2.0F * h * Math.max(g, Math.max(ad * ag, af * ah));
                            f = Math.max(0.0F, ai);
                        } else {
                            f = 0.0F;
                        }

                        blockState = fluidLevel1.getBlockState(j);
                    }

                    if (e + f <= 0.0F) {
                        return blockState;
                    }
                }

                return null;
            }
        }

        private static double maxDistance(int i, int j) {
            return 1.0F - Math.abs(j - i) / 25.0F;
        }

        private double calculateDensity(int i, int j, int k, MutableDouble mutableDouble, FluidLevel fluidLevel, FluidLevel fluidLevel2) {
            BlockState blockState = fluidLevel.getBlockState(j);
            BlockState blockState2 = fluidLevel2.getBlockState(j);
            if ((!blockState.getBlock().equals(Blocks.LAVA) || !blockState2.getBlock().equals(Blocks.WATER)) && (!blockState.getBlock().equals(Blocks.WATER) || !blockState2.getBlock().equals(Blocks.LAVA))) {
                int l = Math.abs(fluidLevel.y - fluidLevel2.y);
                if (l == 0) {
                    return 0.0F;
                } else {
                    double d = 0.5F * (fluidLevel.y + fluidLevel2.y);
                    double e = j + 0.5F - d;
                    double f = l / 2.0F;
                    double q = f - Math.abs(e);
                    double s;
                    if (e > 0.0F) {
                        double r = 0.0F + q;
                        if (r > 0.0F) {
                            s = r / 1.5F;
                        } else {
                            s = r / 2.5F;
                        }
                    } else {
                        double r = 3.0F + q;
                        if (r > 0.0F) {
                            s = r / 3.0F;
                        } else {
                            s = r / 10.0F;
                        }
                    }

                    if (!(s < -2.0F) && !(s > 2.0F)) {
                        double r = mutableDouble.getValue();
                        if (Double.isNaN(r)) {
                            double u = this.barrierNoise.sample(i, j * 0.5F, k);
                            mutableDouble.setValue(u);
                            return u + s;
                        } else {
                            return r + s;
                        }
                    } else {
                        return s;
                    }
                }
            } else {
                return 1.0F;
            }
        }

        private int getLocalX(int i) {
            return Math.floorDiv(i, 16);
        }

        private int getLocalY(int i) {
            return Math.floorDiv(i, 12);
        }

        private int getLocalZ(int i) {
            return Math.floorDiv(i, 16);
        }

        private FluidLevel getWaterLevel(long l) {
            int i = BlockPos.unpackLongX(l);
            int j = BlockPos.unpackLongY(l);
            int k = BlockPos.unpackLongZ(l);
            int m = this.getLocalX(i);
            int n = this.getLocalY(j);
            int o = this.getLocalZ(k);
            int p = this.index(m, n, o);
            FluidLevel fluidLevel = this.waterLevels[p];
            if (fluidLevel != null) {
                return fluidLevel;
            } else {
                FluidLevel fluidLevel2 = this.getFluidLevel(i, j, k);
                this.waterLevels[p] = fluidLevel2;
                return fluidLevel2;
            }
        }

        public FluidLevel getFluidLevel(int i, int j, int k) {
            FluidLevel fluidLevel = this.fluidLevelSampler.getFluidLevel(i, j, k);
            int l = Integer.MAX_VALUE;
            int m = j + 12;
            int n = j - 12;
            boolean bl = false;

            for(int[] is : field_34581) {
                int o = i + ChunkSectionPos.getBlockCoord(is[0]);
                int p = k + ChunkSectionPos.getBlockCoord(is[1]);
                int q = this.chunkNoiseSampler.method_39900(o, p);
                int r = q + 8;
                boolean bl2 = is[0] == 0 && is[1] == 0;
                if (bl2 && n > r) {
                    return fluidLevel;
                }

                boolean bl3 = m > r;
                if (bl3 || bl2) {
                    FluidLevel fluidLevel2 = this.fluidLevelSampler.getFluidLevel(o, r, p);
                    if (!fluidLevel2.getBlockState(r).isEmpty()) {
                        if (bl2) {
                            bl = true;
                        }

                        if (bl3) {
                            return fluidLevel2;
                        }
                    }
                }

                l = Math.min(l, q);
            }

            int s = l + 8 - j;
            double d = bl ? MathHelper.clampedLerpFromProgress(s, 0.0F, 64.0F, 1.0F, 0.0F) : 0.0F;
            double q = MathHelper.clamp(this.fluidLevelFloodednessNoise.sample(i, j * 0.67, k), -1.0F, 1.0F);
            double bl2 = MathHelper.lerpFromProgress(d, 1.0F, 0.0F, -0.3, 0.8);
            if (q > bl2) {
                return fluidLevel;
            } else {
                double fluidLevel2 = MathHelper.lerpFromProgress(d, 1.0F, 0.0F, -0.8, 0.4);
                if (q <= fluidLevel2) {
                    return new FluidLevel(
                            -1024,
                            fluidLevel.state
                    );
                } else {
                    int w = Math.floorDiv(i, 16);
                    int x = Math.floorDiv(j, 40);
                    int y = Math.floorDiv(k, 16);
                    int z = x * 40 + 20;
                    double e = this.fluidLevelSpreadNoise.sample(w, x / 1.4, y) * 10.0F;
                    int ab = MathHelper.roundDownToMultiple(e, 3);
                    int ac = z + ab;
                    int ad = Math.min(l, ac);
                    BlockState blockState = this.method_38993(i, j, k, fluidLevel, ac);
                    return new FluidLevel(ad, blockState);
                }
            }
        }

        private BlockState method_38993(int i, int j, int k, FluidLevel fluidLevel, int l) {
            if (l <= -10) {
                int o = Math.floorDiv(i, 64);
                int p = Math.floorDiv(j, 40);
                int q = Math.floorDiv(k, 64);
                double d = this.fluidTypeNoise.sample(o, p, q);
                if (Math.abs(d) > 0.3) {
                    return new BlockState(Blocks.LAVA);
                }
            }

            return fluidLevel.state;
        }
    }

    final class FluidLevel {
        final int y;
        final BlockState state;

        public FluidLevel(int i, BlockState blockState) {
            this.y = i;
            this.state = blockState;
        }

        public BlockState getBlockState(int i) {
            return i < this.y ? this.state : new BlockState(Blocks.AIR);
        }
    }

    interface FluidLevelSampler {
        FluidLevel getFluidLevel(int i, int j, int k);
    }
}