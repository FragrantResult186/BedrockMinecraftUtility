package com.fragrant.minecraft.world.gen.random;

import java.util.Random;
import java.util.function.LongFunction;

public class ChunkRandom extends Random implements AbstractRandom {
    private final AbstractRandom baseRandom;
    private int sampleCount;

    public ChunkRandom(AbstractRandom abstractRandom) {
        super(0L);
        this.baseRandom = abstractRandom;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    public AbstractRandom derive() {
        return this.baseRandom.derive();
    }

    public RandomDeriver createRandomDeriver() {
        return this.baseRandom.createRandomDeriver();
    }

    public int next(int i) {
        ++this.sampleCount;
        return (int)(this.baseRandom.nextLong() >>> 64 - i);
    }

    public synchronized void setSeed(long l) {
        if (this.baseRandom != null) {
            this.baseRandom.setSeed(l);
        }
    }

    public long setPopulationSeed(long l, int i, int j) {
        this.setSeed(l);
        long m = this.nextInt() | 1L;
        long n = this.nextInt() | 1L;
        long o = (long)i * m + (long)j * n ^ l;
        this.setSeed(o);
        return o;
    }

    public void setDecoratorSeed(long l, int i, int j) {
        long m = l + (long)i + (long)(10000L * j);
        this.setSeed(m);
    }

    public void setCarverSeed(long l, int i, int j) {
        this.setSeed(l);
        long m = this.nextInt();
        long n = this.nextInt();
        long o = (long)i * m ^ (long)j * n ^ l;
        this.setSeed(o);
    }

    public void setRegionSeed(long l, int i, int j, int k) {
        long m = (long)i * 341873128712L + (long)j * 132897987541L + l + (long)k;
        this.setSeed(m);
    }

    public static Random getSlimeRandom(int i, int j, long l, long m) {
        return new Random(l + ((long) i * i * 4987142) + (i * 5947611L) + (long) j * j * 4392871L + (j * 389711L) ^ m);
    }

    public enum RandomProvider {
        XOROSHIRO(Xoroshiro128PlusPlusRandom::new);

        private final LongFunction<AbstractRandom> provider;

        RandomProvider(LongFunction<AbstractRandom> longFunction) {
            this.provider = longFunction;
        }

        public AbstractRandom create(long l) {
            return this.provider.apply(l);
        }
    }
}