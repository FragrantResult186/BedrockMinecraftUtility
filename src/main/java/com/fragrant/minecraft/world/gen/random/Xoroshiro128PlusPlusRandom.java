package com.fragrant.minecraft.world.gen.random;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import com.fragrant.minecraft.util.math.MathHelper;

public class Xoroshiro128PlusPlusRandom implements AbstractRandom {
    private static final float FLOAT_MULTIPLIER = 5.9604645E-8F;
    private static final double DOUBLE_MULTIPLIER = 1.110223E-16F;
    private Xoroshiro128PlusPlusRandomImpl implementation;
    private final GaussianGenerator gaussianGenerator = new GaussianGenerator(this);

    public Xoroshiro128PlusPlusRandom(long l) {
        this.implementation = new Xoroshiro128PlusPlusRandomImpl(RandomSeed.createXoroshiroSeed(l));
    }

    public Xoroshiro128PlusPlusRandom(long l, long m) {
        this.implementation = new Xoroshiro128PlusPlusRandomImpl(l, m);
    }

    public AbstractRandom derive() {
        return new Xoroshiro128PlusPlusRandom(this.implementation.next(), this.implementation.next());
    }

    public RandomDeriver createRandomDeriver() {
        return new RandomDeriver(this.implementation.next(), this.implementation.next());
    }

    public void setSeed(long l) {
        this.implementation = new Xoroshiro128PlusPlusRandomImpl(RandomSeed.createXoroshiroSeed(l));
        this.gaussianGenerator.reset();
    }

    public int nextInt() {
        return (int)this.implementation.next();
    }

    public int nextInt(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        } else {
            long l = Integer.toUnsignedLong(this.nextInt());
            long m = l * (long)i;
            long n = m & 4294967295L;
            if (n < (long)i) {
                for(int j = Integer.remainderUnsigned(~i + 1, i); n < (long)j; n = m & 4294967295L) {
                    l = Integer.toUnsignedLong(this.nextInt());
                    m = l * (long)i;
                }
            }

            long j = m >> 32;
            return (int)j;
        }
    }

    public long nextLong() {
        return this.implementation.next();
    }

    public boolean nextBoolean() {
        return (this.implementation.next() & 1L) != 0L;
    }

    public float nextFloat() {
        return (float)this.next(24) * FLOAT_MULTIPLIER;
    }

    public double nextDouble() {
        return (double)this.next(53) * DOUBLE_MULTIPLIER;
    }

    public double nextGaussian() {
        return this.gaussianGenerator.next();
    }

    public void skip(int i) {
        for(int j = 0; j < i; ++j) {
            this.implementation.next();
        }
    }

    private long next(int i) {
        return this.implementation.next() >>> 64 - i;
    }

    public static class RandomDeriver implements com.fragrant.minecraft.world.gen.random.RandomDeriver {
        private static final HashFunction MD5_HASHER = Hashing.md5();
        private final long seedLo;
        private final long seedHi;

        public RandomDeriver(long l, long m) {
            this.seedLo = l;
            this.seedHi = m;
        }

        public AbstractRandom createRandom(int i, int j, int k) {
            long l = MathHelper.hashCode(i, j, k);
            long m = l ^ this.seedLo;
            return new Xoroshiro128PlusPlusRandom(m, this.seedHi);
        }

        public AbstractRandom createRandom(String string) {
            byte[] bs = MD5_HASHER.hashString(string, Charsets.UTF_8).asBytes();
            long l = Longs.fromBytes(bs[0], bs[1], bs[2], bs[3], bs[4], bs[5], bs[6], bs[7]);
            long m = Longs.fromBytes(bs[8], bs[9], bs[10], bs[11], bs[12], bs[13], bs[14], bs[15]);
            return new Xoroshiro128PlusPlusRandom(l ^ this.seedLo, m ^ this.seedHi);
        }
    }
}
