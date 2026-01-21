package com.fragrant.minecraft.world.gen.random;

public class Xoroshiro128PlusPlusRandomImpl {
    private long seedLo;
    private long seedHi;

    public Xoroshiro128PlusPlusRandomImpl(RandomSeed.XoroshiroSeed xoroshiroSeed) {
        this(xoroshiroSeed.comp_166(), xoroshiroSeed.comp_167());
    }

    public Xoroshiro128PlusPlusRandomImpl(long l, long m) {
        this.seedLo = l;
        this.seedHi = m;
        if ((this.seedLo | this.seedHi) == 0L) {
            this.seedLo = -7046029254386353131L;
            this.seedHi = 7640891576956012809L;
        }
    }

    public long next() {
        long l = this.seedLo;
        long m = this.seedHi;
        long n = Long.rotateLeft(l + m, 17) + l;
        m ^= l;
        this.seedLo = Long.rotateLeft(l, 49) ^ m ^ m << 21;
        this.seedHi = Long.rotateLeft(m, 28);
        return n;
    }
}