package com.fragrant.minecraft.util.math;

import com.google.common.base.MoreObjects;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class BlockBox {
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public BlockBox(BlockPos blockPos) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public BlockBox(int i, int j, int k, int l, int m, int n) {
        this.minX = i;
        this.minY = j;
        this.minZ = k;
        this.maxX = l;
        this.maxY = m;
        this.maxZ = n;
        if (l < i || m < j || n < k) {
            this.minX = Math.min(i, l);
            this.minY = Math.min(j, m);
            this.minZ = Math.min(k, n);
            this.maxX = Math.max(i, l);
            this.maxY = Math.max(j, m);
            this.maxZ = Math.max(k, n);
        }
    }

    public static BlockBox create(Vec3i vec3i, Vec3i vec3i2) {
        return new BlockBox(Math.min(vec3i.getX(), vec3i2.getX()), Math.min(vec3i.getY(), vec3i2.getY()), Math.min(vec3i.getZ(), vec3i2.getZ()), Math.max(vec3i.getX(), vec3i2.getX()), Math.max(vec3i.getY(), vec3i2.getY()), Math.max(vec3i.getZ(), vec3i2.getZ()));
    }

    public static BlockBox rotated(int i, int j, int k, int l, int m, int n, int o, int p, int q, Direction direction) {
        switch (direction) {
            case SOUTH:
            default:
                return new BlockBox(i + l, j + m, k + n, i + o - 1 + l, j + p - 1 + m, k + q - 1 + n);
            case NORTH:
                return new BlockBox(i + l, j + m, k - q + 1 + n, i + o - 1 + l, j + p - 1 + m, k + n);
            case WEST:
                return new BlockBox(i - q + 1 + n, j + m, k + l, i + n, j + p - 1 + m, k + o - 1 + l);
            case EAST:
                return new BlockBox(i + n, j + m, k + l, i + q - 1 + n, j + p - 1 + m, k + o - 1 + l);
        }
    }

    public boolean intersects(BlockBox blockBox) {
        return this.maxX >= blockBox.minX && this.minX <= blockBox.maxX && this.maxZ >= blockBox.minZ && this.minZ <= blockBox.maxZ && this.maxY >= blockBox.minY && this.minY <= blockBox.maxY;
    }

    public boolean intersectsXZ(int i, int j, int k, int l) {
        return this.maxX >= i && this.minX <= k && this.maxZ >= j && this.minZ <= l;
    }

    public static Optional<BlockBox> encompassPositions(Iterable<BlockPos> iterable) {
        Iterator<BlockPos> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else {
            BlockBox blockBox = new BlockBox((BlockPos)iterator.next());
            Objects.requireNonNull(blockBox);
            iterator.forEachRemaining(blockBox::encompass);
            return Optional.of(blockBox);
        }
    }

    public static Optional<BlockBox> encompass(Iterable<BlockBox> iterable) {
        Iterator<BlockBox> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else {
            BlockBox blockBox = (BlockBox)iterator.next();
            BlockBox blockBox2 = new BlockBox(blockBox.minX, blockBox.minY, blockBox.minZ, blockBox.maxX, blockBox.maxY, blockBox.maxZ);
            Objects.requireNonNull(blockBox2);
            iterator.forEachRemaining(blockBox2::encompass);
            return Optional.of(blockBox2);
        }
    }

    public BlockBox encompass(BlockBox blockBox) {
        this.minX = Math.min(this.minX, blockBox.minX);
        this.minY = Math.min(this.minY, blockBox.minY);
        this.minZ = Math.min(this.minZ, blockBox.minZ);
        this.maxX = Math.max(this.maxX, blockBox.maxX);
        this.maxY = Math.max(this.maxY, blockBox.maxY);
        this.maxZ = Math.max(this.maxZ, blockBox.maxZ);
        return this;
    }

    public BlockBox encompass(BlockPos blockPos) {
        this.minX = Math.min(this.minX, blockPos.getX());
        this.minY = Math.min(this.minY, blockPos.getY());
        this.minZ = Math.min(this.minZ, blockPos.getZ());
        this.maxX = Math.max(this.maxX, blockPos.getX());
        this.maxY = Math.max(this.maxY, blockPos.getY());
        this.maxZ = Math.max(this.maxZ, blockPos.getZ());
        return this;
    }

    public BlockBox move(int i, int j, int k) {
        this.minX += i;
        this.minY += j;
        this.minZ += k;
        this.maxX += i;
        this.maxY += j;
        this.maxZ += k;
        return this;
    }

    public BlockBox move(Vec3i vec3i) {
        return this.move(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public BlockBox offset(int i, int j, int k) {
        return new BlockBox(this.minX + i, this.minY + j, this.minZ + k, this.maxX + i, this.maxY + j, this.maxZ + k);
    }

    public BlockBox expand(int i) {
        return new BlockBox(this.getMinX() - i, this.getMinY() - i, this.getMinZ() - i, this.getMaxX() + i, this.getMaxY() + i, this.getMaxZ() + i);
    }

    public boolean contains(Vec3i vec3i) {
        return vec3i.getX() >= this.minX && vec3i.getX() <= this.maxX && vec3i.getZ() >= this.minZ && vec3i.getZ() <= this.maxZ && vec3i.getY() >= this.minY && vec3i.getY() <= this.maxY;
    }

    public Vec3i getDimensions() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }

    public int getBlockCountX() {
        return this.maxX - this.minX + 1;
    }

    public int getBlockCountY() {
        return this.maxY - this.minY + 1;
    }

    public int getBlockCountZ() {
        return this.maxZ - this.minZ + 1;
    }

    public BlockPos getCenter() {
        return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }

    public void forEachVertex(Consumer<BlockPos> consumer) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        consumer.accept(mutable.set(this.maxX, this.maxY, this.maxZ));
        consumer.accept(mutable.set(this.minX, this.maxY, this.maxZ));
        consumer.accept(mutable.set(this.maxX, this.minY, this.maxZ));
        consumer.accept(mutable.set(this.minX, this.minY, this.maxZ));
        consumer.accept(mutable.set(this.maxX, this.maxY, this.minZ));
        consumer.accept(mutable.set(this.minX, this.maxY, this.minZ));
        consumer.accept(mutable.set(this.maxX, this.minY, this.minZ));
        consumer.accept(mutable.set(this.minX, this.minY, this.minZ));
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("minX", this.minX).add("minY", this.minY).add("minZ", this.minZ).add("maxX", this.maxX).add("maxY", this.maxY).add("maxZ", this.maxZ).toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof BlockBox blockBox)) {
            return false;
        } else {
            return this.minX == blockBox.minX && this.minY == blockBox.minY && this.minZ == blockBox.minZ && this.maxX == blockBox.maxX && this.maxY == blockBox.maxY && this.maxZ == blockBox.maxZ;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMaxZ() {
        return this.maxZ;
    }
}
