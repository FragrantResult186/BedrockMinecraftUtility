package com.fragrant.minecraft.util.math.noise;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.fragrant.minecraft.util.math.MathHelper;
import com.fragrant.minecraft.world.gen.random.AbstractRandom;
import com.fragrant.minecraft.world.gen.random.RandomDeriver;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class OctavePerlinNoiseSampler {
    private final PerlinNoiseSampler[] octaveSamplers;
    private final int firstOctave;
    private final DoubleList amplitudes;
    private final double persistence;
    private final double lacunarity;

    public static OctavePerlinNoiseSampler createLegacy(AbstractRandom abstractRandom, IntStream intStream) {
        return new OctavePerlinNoiseSampler(abstractRandom, calculateAmplitudes(new IntRBTreeSet(intStream.boxed().collect(ImmutableList.toImmutableList()))), false);
    }

    public static OctavePerlinNoiseSampler createLegacy(AbstractRandom abstractRandom, int i, DoubleList doubleList) {
        return new OctavePerlinNoiseSampler(abstractRandom, Pair.of(i, doubleList), false);
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom abstractRandom, IntStream intStream) {
        return create(abstractRandom, intStream.boxed().collect(ImmutableList.toImmutableList()));
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom abstractRandom, List<Integer> list) {
        return new OctavePerlinNoiseSampler(abstractRandom, calculateAmplitudes(new IntRBTreeSet(list)), true);
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom abstractRandom, int i, double d, double... ds) {
        DoubleArrayList doubleArrayList = new DoubleArrayList(ds);
        doubleArrayList.add(0, d);
        return new OctavePerlinNoiseSampler(abstractRandom, Pair.of(i, doubleArrayList), true);
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom abstractRandom, int i, DoubleList doubleList) {
        return new OctavePerlinNoiseSampler(abstractRandom, Pair.of(i, doubleList), true);
    }

    private static Pair<Integer, DoubleList> calculateAmplitudes(IntSortedSet intSortedSet) {
        if (intSortedSet.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        } else {
            int i = -intSortedSet.firstInt();
            int j = intSortedSet.lastInt();
            int k = i + j + 1;
            if (k < 1) {
                throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
            } else {
                DoubleList doubleList = new DoubleArrayList(new double[k]);
                IntBidirectionalIterator intBidirectionalIterator = intSortedSet.iterator();

                while(intBidirectionalIterator.hasNext()) {
                    int l = intBidirectionalIterator.nextInt();
                    doubleList.set(l + i, 1.0F);
                }

                return Pair.of(-i, doubleList);
            }
        }
    }

    protected OctavePerlinNoiseSampler(AbstractRandom abstractRandom, Pair<Integer, DoubleList> pair, boolean bl) {
        this.firstOctave = pair.getFirst();
        this.amplitudes = pair.getSecond();
        int i = this.amplitudes.size();
        int j = -this.firstOctave;
        this.octaveSamplers = new PerlinNoiseSampler[i];
        if (bl) {
            RandomDeriver randomDeriver = abstractRandom.createRandomDeriver();

            for(int k = 0; k < i; ++k) {
                if (this.amplitudes.getDouble(k) != (double)0.0F) {
                    int l = this.firstOctave + k;
                    this.octaveSamplers[k] = new PerlinNoiseSampler(randomDeriver.createRandom("octave_" + l));
                }
            }
        } else {
            PerlinNoiseSampler randomDeriver = new PerlinNoiseSampler(abstractRandom);
            if (j >= 0 && j < i) {
                double k = this.amplitudes.getDouble(j);
                if (k != (double)0.0F) {
                    this.octaveSamplers[j] = randomDeriver;
                }
            }

            for(int k = j - 1; k >= 0; --k) {
                if (k < i) {
                    double l = this.amplitudes.getDouble(k);
                    if (l != (double)0.0F) {
                        this.octaveSamplers[k] = new PerlinNoiseSampler(abstractRandom);
                    } else {
                        skipCalls(abstractRandom);
                    }
                } else {
                    skipCalls(abstractRandom);
                }
            }

            if (Arrays.stream(this.octaveSamplers).filter(Objects::nonNull).count() != this.amplitudes.stream().filter((double_) -> double_ != (double)0.0F).count()) {
                throw new IllegalStateException("Failed to create correct number of noise levels for given non-zero amplitudes");
            }

            if (j < i - 1) {
                throw new IllegalArgumentException("Positive octaves are temporarily disabled");
            }
        }

        this.lacunarity = Math.pow(2.0F, -j);
        this.persistence = Math.pow(2.0F, i - 1) / (Math.pow(2.0F, i) - (double)1.0F);
    }

    private static void skipCalls(AbstractRandom abstractRandom) {
        abstractRandom.skip(262);
    }

    public double sample(double d, double e, double f) {
        return this.sample(d, e, f, 0.0F, 0.0F, false);
    }

    public double sample(double d, double e, double f, double g, double h, boolean bl) {
        double i = 0.0F;
        double j = this.lacunarity;
        double k = this.persistence;

        for(int l = 0; l < this.octaveSamplers.length; ++l) {
            PerlinNoiseSampler perlinNoiseSampler = this.octaveSamplers[l];
            if (perlinNoiseSampler != null) {
                double m = perlinNoiseSampler.sample(maintainPrecision(d * j), bl ? -perlinNoiseSampler.originY : maintainPrecision(e * j), maintainPrecision(f * j), g * j, h * j);
                i += this.amplitudes.getDouble(l) * m * k;
            }

            j *= 2.0F;
            k /= 2.0F;
        }

        return i;
    }

    public PerlinNoiseSampler getOctave(int i) {
        return this.octaveSamplers[this.octaveSamplers.length - 1 - i];
    }

    public static double maintainPrecision(double d) {
        return d - (double) MathHelper.lfloor(d / (double)3.3554432E7F + (double)0.5F) * (double)3.3554432E7F;
    }

    protected int getFirstOctave() {
        return this.firstOctave;
    }

    protected DoubleList getAmplitudes() {
        return this.amplitudes;
    }

}
