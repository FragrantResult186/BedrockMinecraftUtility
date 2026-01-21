package com.fragrant.minecraft.world.gen.random;

/**
 * An RNG implementation equivalent to that of Minecraft: Bedrock Edition.
 *
 * This is a version of the MT19937 Mersenne Twister algorithm.
 * This version contains the improved initialization from http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c
 * It also contains an optimization to only generate some of the MT array when the RNG is first initialized.
 *
 * @author Earthcomputer
 */
public class BedrockRandom implements AbstractRandom {

    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;
    private static final int UPPER_MASK = 0x80000000;
    private static final int LOWER_MASK = 0x7fffffff;
    private static final int[] MAG_01 = {0, MATRIX_A};
    private static final double TWO_POW_M32 = 1.0 / (1L << 32);

    private int seed;
    private int[] mt = new int[N];
    private int mti;
    private boolean haveNextNextGaussian;
    private float nextNextGaussian;
    private int mtiFast;

    public BedrockRandom() {
        this(System.currentTimeMillis());
    }

    public BedrockRandom(long seed) {
        setSeed(seed);
    }

    /**
     * Copy constructor
     */
    public void copy(BedrockRandom rand) {
        this.seed = rand.seed;
        System.arraycopy(rand.mt, 0, this.mt, 0, N);
        this.mti = rand.mti;
        this.haveNextNextGaussian = rand.haveNextNextGaussian;
        this.nextNextGaussian = rand.nextNextGaussian;
        this.mtiFast = rand.mtiFast;
    }

    // ===== AbstractRandom Interface Implementation ===== //

    @Override
    public AbstractRandom derive() {
        return new BedrockRandom(this._genRandInt32());
    }

    @Override
    public RandomDeriver createRandomDeriver() {
        return new BedrockRandomDeriver(this._genRandInt32(), this._genRandInt32());
    }

    @Override
    public void setSeed(long seed) {
        _setSeed((int) seed);
    }

    @Override
    public int nextInt() {
        return _genRandInt32() >>> 1;
    }

    @Override
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return (int) (Integer.toUnsignedLong(_genRandInt32()) % bound);
    }

    @Override
    public long nextLong() {
        return ((long) nextInt() << 32) | (nextInt() & 0xFFFFFFFFL);
    }

    @Override
    public boolean nextBoolean() {
        return (_genRandInt32() & 0x8000000) != 0;
    }

    @Override
    public float nextFloat() {
        return (float) _genRandReal2();
    }

    @Override
    public double nextDouble() {
        return _genRandReal2();
    }

    @Override
    public double nextGaussian() {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        }

        float v1, v2, s;
        do {
            v1 = nextFloat() * 2 - 1;
            v2 = nextFloat() * 2 - 1;
            s = v1 * v1 + v2 * v2;
        } while (s == 0 || s > 1);

        float multiplier = (float) Math.sqrt(-2 * (float) Math.log(s) / s);
        nextNextGaussian = v2 * multiplier;
        haveNextNextGaussian = true;
        return v1 * multiplier;
    }

    // ===== Additional Bedrock-specific methods ===== //

    public int getSeed() {
        return seed;
    }

    /**
     * Generates a random integer k such that a <= k < b
     */
    public int nextInt(int a, int b) {
        if (a < b)
            return a + nextInt(b - a);
        else
            return a;
    }

    /**
     * Generates a random integer k such that a <= k <= b
     */
    public int nextIntInclusive(int a, int b) {
        return nextInt(a, b + 1);
    }

    /**
     * Generates a random long k such that 0 <= k < 2^32, i.e. a random unsigned int
     */
    public long nextUnsignedInt() {
        return Integer.toUnsignedLong(_genRandInt32());
    }

    /**
     * Generates a random short k such that 0 <= k < 256, i.e. a random unsigned byte
     */
    public short nextUnsignedChar() {
        return (short) (_genRandInt32() & 0xff);
    }

    /**
     * Generates a uniform random float k such that 0 <= k < bound
     */
    public float nextFloat(float bound) {
        return nextFloat() * bound;
    }

    /**
     * Generates a uniform random float k such that a <= k < b
     */
    public float nextFloat(float a, float b) {
        return a + (nextFloat() * (b - a));
    }

    /**
     * Returns a triangularly distributed int k with mode 0 such that -bound < k < bound.
     *
     * Note that the method name, used in the Bedrock code, incorrectly describes this as a Gaussian distribution.
     */
    public int nextGaussianInt(int bound) {
        return nextInt(bound) - nextInt(bound);
    }

    /**
     * Returns a triangularly distributed float k with mode 0 such that -1 < k < 1.
     *
     * Note that the method name, used in the Bedrock code, incorrectly describes this as a Gaussian distribution.
     */
    public float nextGaussianFloat() {
        return nextFloat() - nextFloat();
    }

    // ===== MT19937 Mersenne Twister Implementation ===== //

    private void _setSeed(int seed) {
        this.seed = seed;
        this.mti = N + 1;
        this.haveNextNextGaussian = false;
        this.nextNextGaussian = 0;
        _initGenRandFast(seed);
    }

    private void _initGenRand(int initialValue) {
        this.mt[0] = initialValue;
        for (this.mti = 1; this.mti < N; this.mti++) {
            this.mt[mti] = 1812433253
                    * ((this.mt[this.mti - 1] >>> 30) ^ this.mt[this.mti - 1])
                    + this.mti;
        }
        this.mtiFast = N;
    }

    private void _initGenRandFast(int initialValue) {
        this.mt[0] = initialValue;
        for (this.mtiFast = 1; this.mtiFast <= M; this.mtiFast++) {
            this.mt[this.mtiFast] = 1812433253
                    * ((this.mt[this.mtiFast - 1] >>> 30) ^ this.mt[this.mtiFast - 1])
                    + this.mtiFast;
        }
        this.mti = N;
    }

    public int _genRandInt32() {
        if (this.mti == N) {
            this.mti = 0;
        } else if (this.mti > N) {
            _initGenRand(5489);
            this.mti = 0;
        }

        if (this.mti >= N - M) {
            if (this.mti >= N - 1) {
                this.mt[N - 1] = MAG_01[this.mt[0] & 1]
                        ^ ((this.mt[0] & LOWER_MASK | this.mt[N - 1] & UPPER_MASK) >>> 1)
                        ^ this.mt[M - 1];
            } else {
                this.mt[this.mti] = MAG_01[this.mt[this.mti + 1] & 1]
                        ^ ((this.mt[this.mti + 1] & LOWER_MASK | this.mt[this.mti] & UPPER_MASK) >>> 1)
                        ^ this.mt[this.mti - (N - M)];
            }
        } else {
            this.mt[this.mti] = MAG_01[this.mt[this.mti + 1] & 1]
                    ^ ((this.mt[this.mti + 1] & LOWER_MASK | this.mt[this.mti] & UPPER_MASK) >>> 1)
                    ^ this.mt[this.mti + M];

            if (this.mtiFast < N) {
                this.mt[this.mtiFast] = 1812433253
                        * ((this.mt[this.mtiFast - 1] >>> 30) ^ this.mt[this.mtiFast - 1])
                        + this.mtiFast;
                this.mtiFast++;
            }
        }

        int ret = this.mt[this.mti++];
        ret = ((ret ^ (ret >>> 11)) << 7) & 0x9d2c5680 ^ ret ^ (ret >>> 11);
        ret = (ret << 15) & 0xefc60000 ^ ret ^ (((ret << 15) & 0xefc60000 ^ ret) >>> 18);
        return ret;
    }

    private double _genRandReal2() {
        return Integer.toUnsignedLong(_genRandInt32()) * TWO_POW_M32;
    }

    // ===== RandomDeriver Implementation ===== //

    private static class BedrockRandomDeriver implements RandomDeriver {
        private final int seedA;
        private final int seedB;

        public BedrockRandomDeriver(int seedA, int seedB) {
            this.seedA = seedA;
            this.seedB = seedB;
        }

        @Override
        public AbstractRandom createRandom(String string) {
            int hash = string.hashCode();
            return new BedrockRandom(hash ^ seedA ^ (long)seedB << 32);
        }

        @Override
        public AbstractRandom createRandom(int i, int j, int k) {
            long hash = ((long)i * 3129871L) ^ ((long)j * 116129781L) ^ ((long)k * 1376312589L);
            return new BedrockRandom(hash ^ seedA ^ (long)seedB << 32);
        }
    }
}