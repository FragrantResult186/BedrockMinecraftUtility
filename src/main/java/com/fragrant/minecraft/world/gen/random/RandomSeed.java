package com.fragrant.minecraft.world.gen.random;

import com.google.common.annotations.VisibleForTesting;

public final class RandomSeed {
    public static final long XOROSHIRO64_SEED_LO_FALLBACK = -7046029254386353131L;
    public static final long XOROSHIRO64_SEED_HI_FALLBACK = 7640891576956012809L;

    @VisibleForTesting
    public static long nextSplitMix64Int(long l) {
        l = (l ^ l >>> 30) * -4658895280553007687L;
        l = (l ^ l >>> 27) * -7723592293110705685L;
        return l ^ l >>> 31;
    }

    public static XoroshiroSeed createXoroshiroSeed(long l) {
        long m = l ^ XOROSHIRO64_SEED_HI_FALLBACK;
        long n = m + XOROSHIRO64_SEED_LO_FALLBACK;
        return new XoroshiroSeed(nextSplitMix64Int(m), nextSplitMix64Int(n));
    }

    public record XoroshiroSeed(long comp_166, long comp_167) {}
}
