package com.fragrant.minecraft.world.gen.random;

public class BedrockChunkRandom extends BedrockRandom {

    public BedrockChunkRandom() {
        super(0);
    }

    public BedrockChunkRandom(long seed) {
        super(seed);
    }

    @Override
    public void copy(BedrockRandom rand) {
        super.copy(rand);
    }

    public int setTerrainSeed(int chunkX, int chunkZ) {
        int a = chunkX * (int) 341873128712L;
        int b = chunkZ * (int) 132897987541L;
        int seed = a + b;
        this.setSeed(seed);
        return seed;
    }

    public int setPopulationSeed(long worldSeed, int x, int z) {
        this.setSeed(worldSeed);
        int a = this.nextInt() | 1;
        int b = this.nextInt() | 1;
        int seed = (x * a + z * b) ^ (int) worldSeed;
        this.setSeed(seed);
        return seed;
    }

    public int setDecorationSeed(long worldSeed, int chunkX, int chunkZ, int salt) {
        int populationSeed = this.setPopulationSeed(worldSeed, chunkX, chunkZ);
        int seed = (populationSeed >>> 2) + (populationSeed << 6) + salt - 1640531527 ^ populationSeed;
        this.setSeed(seed);
        return seed;
    }

    public int setCarverSeed(long worldSeed, int chunkX, int chunkZ) {
        this.setSeed(worldSeed);
        int a = this.nextInt();
        int b = this.nextInt();
        int seed = (chunkX * a) ^ (chunkZ * b) ^ (int) worldSeed;
        this.setSeed(seed);
        return seed;
    }

    public int setRegionSeed(long worldSeed, int regionX, int regionZ, int salt) {
        int a = regionX * (int) 341873128712L;
        int b = regionZ * (int) 132897987541L;
        int seed = a + b + (int) worldSeed + salt;
        this.setSeed(seed);
        return seed;
    }

    public int setStrongholdSeed(long worldSeed, int regionX, int regionZ, int salt) {
        int a = regionX * (int) 784295783249L;
        int b = regionZ * (int) 827828252345L;
        int seed = a + b + (int) worldSeed + salt;
        this.setSeed(seed);
        return seed;
    }

    public int setFortressSeed(long worldSeed, int chunkX, int chunkZ) {
        int sX = chunkX >> 4;
        int sZ = chunkZ >> 4;
        int seed = (sX ^ sZ << 4) ^ (int) worldSeed;
        this.setSeed(seed);
        return seed;
    }

    public void setSlimeRandom(int chunkX, int chunkZ) {
        this.setSeed((chunkX * 522133279L) ^ chunkZ);
    }
}