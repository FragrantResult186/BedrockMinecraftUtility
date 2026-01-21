package com.fragrant.minecraft.world.chunk;

import com.fragrant.minecraft.block.BlockState;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.fragrant.minecraft.util.collection.PackedIntegerArray;
import com.fragrant.minecraft.util.collection.PaletteStorage;
import com.fragrant.minecraft.util.math.BlockPos;
import com.fragrant.minecraft.util.math.MathHelper;

import java.util.Set;
import java.util.function.Predicate;

public class Heightmap {
    static final Predicate<BlockState> NOT_AIR = (blockState) -> !blockState.isEmpty();
    private final PaletteStorage storage;
    private final Predicate<BlockState> blockPredicate;

    public Heightmap(Type type) {
        this.blockPredicate = type.getBlockPredicate();
        int i = MathHelper.ceilLog2(385);
        this.storage = new PackedIntegerArray(i, 256);
    }

    public static void populateHeightmaps(Chunk chunk, Set<Type> set) {
        int i = set.size();
        ObjectList<Heightmap> objectList = new ObjectArrayList<>(i);
        ObjectListIterator<Heightmap> objectListIterator = objectList.iterator();
        int j = chunk.getHighestNonEmptySectionYOffset() + 16;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int k = 0; k < 16; ++k) {
            for(int l = 0; l < 16; ++l) {
                for(Type type : set) {
                    objectList.add(chunk.getHeightmap(type));
                }

                for(int m = j - 1; m >= -64; --m) {
                    mutable.set(k, m, l);
                    BlockState type = chunk.getBlockState(mutable);
                    if (!type.isEmpty()) {
                        while(objectListIterator.hasNext()) {
                            Heightmap heightmap = objectListIterator.next();
                            if (heightmap.blockPredicate.test(type)) {
                                heightmap.set(k, l, m + 1);
                                objectListIterator.remove();
                            }
                        }

                        if (objectList.isEmpty()) {
                            break;
                        }

                        objectListIterator.back(i);
                    }
                }
            }
        }

    }

    public int get(int i, int j) {
        return this.get(toIndex(i, j));
    }

    private int get(int i) {
        return this.storage.get(i) - 64;
    }

    private void set(int i, int j, int k) {
        this.storage.set(toIndex(i, j), k + 64);
    }

    private static int toIndex(int i, int j) {
        return i + j * 16;
    }

    public enum Type {
        WORLD_SURFACE_WG(Heightmap.NOT_AIR),
        WORLD_SURFACE(Heightmap.NOT_AIR);

        private final Predicate<BlockState> blockPredicate;

        Type(Predicate<BlockState> predicate) {
            this.blockPredicate = predicate;
        }

        public Predicate<BlockState> getBlockPredicate() {
            return this.blockPredicate;
        }

    }
}