package com.fragrant.minecraft.world.gen.random;

public interface AbstractRandom {
    AbstractRandom derive();

    RandomDeriver createRandomDeriver();

    void setSeed(long l);

    int nextInt();

    int nextInt(int i);

    default int nextBetween(int i, int j) {
        return this.nextInt(j - i + 1) + i;
    }

    long nextLong();

    boolean nextBoolean();

    float nextFloat();

    double nextDouble();

    double nextGaussian();

    default void skip(int i) {
        for(int j = 0; j < i; ++j) {
            this.nextInt();
        }
    }
}
