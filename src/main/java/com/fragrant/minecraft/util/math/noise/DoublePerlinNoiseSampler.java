package com.fragrant.minecraft.util.math.noise;

import com.fragrant.minecraft.world.gen.random.AbstractRandom;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import java.util.List;

public class DoublePerlinNoiseSampler {
    private final double amplitude;
    private final OctavePerlinNoiseSampler firstSampler;
    private final OctavePerlinNoiseSampler secondSampler;

    public static DoublePerlinNoiseSampler create(AbstractRandom abstractRandom, int i, double... ds) {
        return new DoublePerlinNoiseSampler(abstractRandom, i, new DoubleArrayList(ds), true);
    }

    public static DoublePerlinNoiseSampler create(AbstractRandom abstractRandom, NoiseParameters noiseParameters) {
        return new DoublePerlinNoiseSampler(abstractRandom, noiseParameters.getFirstOctave(), noiseParameters.getAmplitudes(), true);
    }

    public static DoublePerlinNoiseSampler create(AbstractRandom abstractRandom, int i, DoubleList doubleList) {
        return new DoublePerlinNoiseSampler(abstractRandom, i, doubleList, true);
    }

    private DoublePerlinNoiseSampler(AbstractRandom abstractRandom, int i, DoubleList doubleList, boolean bl) {
        if (bl) {
            this.firstSampler = OctavePerlinNoiseSampler.create(abstractRandom, i, doubleList);
            this.secondSampler = OctavePerlinNoiseSampler.create(abstractRandom, i, doubleList);
        } else {
            this.firstSampler = OctavePerlinNoiseSampler.createLegacy(abstractRandom, i, doubleList);
            this.secondSampler = OctavePerlinNoiseSampler.createLegacy(abstractRandom, i, doubleList);
        }

        int j = Integer.MAX_VALUE;
        int k = Integer.MIN_VALUE;
        DoubleListIterator doubleListIterator = doubleList.iterator();

        while(doubleListIterator.hasNext()) {
            int l = doubleListIterator.nextIndex();
            double d = doubleListIterator.nextDouble();
            if (d != (double)0.0F) {
                j = Math.min(j, l);
                k = Math.max(k, l);
            }
        }

        this.amplitude = 0.16666666666666666 / createAmplitude(k - j);
    }

    private static double createAmplitude(int i) {
        return 0.1 * ((double)1.0F + (double)1.0F / (double)(i + 1));
    }

    public double sample(double d, double e, double f) {
        double g = d * 1.0181268882175227;
        double h = e * 1.0181268882175227;
        double i = f * 1.0181268882175227;
        return (this.firstSampler.sample(d, e, f) + this.secondSampler.sample(g, h, i)) * this.amplitude;
    }

    public NoiseParameters copy() {
        return new NoiseParameters(this.firstSampler.getFirstOctave(), this.firstSampler.getAmplitudes());
    }

    public static class NoiseParameters {
        private final int firstOctave;
        private final DoubleList amplitudes;

        public NoiseParameters(int i, List<Double> list) {
            this.firstOctave = i;
            this.amplitudes = new DoubleArrayList(list);
        }

        public NoiseParameters(int i, double d, double... ds) {
            this.firstOctave = i;
            this.amplitudes = new DoubleArrayList(ds);
            this.amplitudes.add(0, d);
        }

        public int getFirstOctave() {
            return this.firstOctave;
        }

        public DoubleList getAmplitudes() {
            return this.amplitudes;
        }
    }
}
