package com.fragrant.minecraft.world.gen;

import com.fragrant.minecraft.util.math.ChunkPos;
import com.fragrant.minecraft.world.gen.AquiferSampler.FluidLevel;
import com.fragrant.minecraft.world.gen.AquiferSampler.FluidLevelSampler;
import com.fragrant.minecraft.block.BlockSource;
import com.fragrant.minecraft.world.chunk.Chunk;
import com.fragrant.minecraft.world.chunk.ChunkSection;
import com.fragrant.minecraft.world.gen.random.RandomDeriver;
import com.fragrant.minecraft.block.Block;
import com.fragrant.minecraft.block.BlockState;
import com.fragrant.minecraft.block.Blocks;

public class NoiseChunkGenerator {
    private final Block defaultBlock;
    private final NoiseColumnSampler noiseColumnSampler;
    private final BlockSource blockSource;
    private final FluidLevelSampler fluidLevelSampler;

    public NoiseChunkGenerator(RandomDeriver randomDeriver) {
        this.defaultBlock = Blocks.STONE;
        this.noiseColumnSampler = new NoiseColumnSampler(randomDeriver);
        this.blockSource = ChunkNoiseSampler::sampleInitialNoiseBlockState;
        this.fluidLevelSampler = (x, y, z) -> y < -54 ? new FluidLevel(-54, new BlockState(Blocks.LAVA)) : new FluidLevel(63, new BlockState(Blocks.WATER));
    }

    public void populateNoise(Chunk chunk) {
        ChunkNoiseSampler noiseSampler = chunk.getOrCreateChunkNoiseSampler(
                this.noiseColumnSampler,
                () -> (x, y, z) -> 0.0,
                this.fluidLevelSampler
        );

        NoisePopulator populator = new NoisePopulator(chunk, noiseSampler);
        populator.populate();
    }

    private class NoisePopulator {
        private final Chunk chunk;
        private final ChunkNoiseSampler noiseSampler;
        private final ChunkPos chunkPos;

        NoisePopulator(Chunk chunk, ChunkNoiseSampler noiseSampler) {
            this.chunk = chunk;
            this.noiseSampler = noiseSampler;
            this.chunkPos = chunk.getPos();
        }

        void populate() {
            this.noiseSampler.sampleStartNoise();
            for (int x = 0; x < 4; x++) {
                this.noiseSampler.sampleEndNoise(x);
                for (int z = 0; z < 4; z++) {
                    populateColumn(x, z);
                }
                this.noiseSampler.swapBuffers();
            }
        }

        private void populateColumn(int x, int z) {
            ChunkSection currentSection = this.chunk.getSection(23);
            int currentSectionIndex = 23;

            int xOffset = x * 4;
            int zOffset = z * 4;
            int worldX = this.chunkPos.getStartX();
            int worldZ = this.chunkPos.getStartZ();

            for (int sectionY = 47; sectionY >= 0; sectionY--) {
                this.noiseSampler.sampleNoiseCorners(sectionY, z);
                int baseY = sectionY * 8 - 64;

                for (int dy = 7; dy >= 0; dy--) {
                    int wy = baseY + dy;
                    int localY = wy & 15;
                    int sectionIndex = this.chunk.getSectionIndex(wy);

                    if (currentSectionIndex != sectionIndex) {
                        currentSection = this.chunk.getSection(sectionIndex);
                        currentSectionIndex = sectionIndex;
                    }

                    this.noiseSampler.sampleNoiseY(dy * 0.125);

                    for (int dx = 0; dx < 4; dx++) {
                        int wx = worldX + xOffset + dx;
                        int localX = wx & 15;
                        this.noiseSampler.sampleNoiseX(dx * 0.25);

                        for (int dz = 0; dz < 4; dz++) {
                            int wz = worldZ + zOffset + dz;
                            int localZ = wz & 15;
                            this.noiseSampler.sampleNoise(dz * 0.25);

                            BlockState state = blockSource.apply(
                                    this.noiseSampler,
                                    wx, wy, wz
                            );

                            if (state == null) {
                                state = new BlockState(defaultBlock);
                            }

                            if (state.getBlock().equals(Blocks.AIR)) {
                                continue;
                            }

                            currentSection.setBlockState(localX, localY, localZ, state);
                        }
                    }
                }
            }
        }
    }
}