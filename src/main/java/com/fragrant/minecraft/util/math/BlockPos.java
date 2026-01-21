package com.fragrant.minecraft.util.math;

import com.google.common.collect.AbstractIterator;
import com.fragrant.minecraft.util.BlockRotation;
import com.fragrant.minecraft.util.math.Direction.Axis;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BlockPos extends Vec3i {
    public static final BlockPos ORIGIN;
    private static final int SIZE_BITS_X;
    private static final int SIZE_BITS_Z;
    public static final int SIZE_BITS_Y;
    private static final long BITS_X;
    private static final long BITS_Y;
    private static final long BITS_Z;
    private static final int BIT_SHIFT_Z;
    private static final int BIT_SHIFT_X;

    public BlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPos(double d, double e, double f) {
        super(d, e, f);
    }

    public BlockPos(Position position) {
        this(position.getX(), position.getY(), position.getZ());
    }

    public BlockPos(Vec3i vec3i) {
        this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static long offset(long l, Direction direction) {
        return add(l, direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    public static long add(long l, int i, int j, int k) {
        return asLong(unpackLongX(l) + i, unpackLongY(l) + j, unpackLongZ(l) + k);
    }

    public static int unpackLongX(long l) {
        return (int)(l << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
    }

    public static int unpackLongY(long l) {
        return (int)(l << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
    }

    public static int unpackLongZ(long l) {
        return (int)(l << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
    }

    public static BlockPos fromLong(long l) {
        return new BlockPos(unpackLongX(l), unpackLongY(l), unpackLongZ(l));
    }

    public long asLong() {
        return asLong(this.getX(), this.getY(), this.getZ());
    }

    public static long asLong(int i, int j, int k) {
        long l = 0L;
        l |= ((long)i & BITS_X) << BIT_SHIFT_X;
        l |= ((long)j & BITS_Y) << 0;
        l |= ((long)k & BITS_Z) << BIT_SHIFT_Z;
        return l;
    }

    public BlockPos add(double d, double e, double f) {
        return d == (double)0.0F && e == (double)0.0F && f == (double)0.0F ? this : new BlockPos((double)this.getX() + d, (double)this.getY() + e, (double)this.getZ() + f);
    }

    public BlockPos add(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPos(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    public BlockPos add(Vec3i vec3i) {
        return this.add(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public BlockPos subtract(Vec3i vec3i) {
        return this.add(-vec3i.getX(), -vec3i.getY(), -vec3i.getZ());
    }

    public BlockPos multiply(int i) {
        if (i == 1) {
            return this;
        } else {
            return i == 0 ? ORIGIN : new BlockPos(this.getX() * i, this.getY() * i, this.getZ() * i);
        }
    }

    public BlockPos up() {
        return this.offset(Direction.UP);
    }

    public BlockPos up(int i) {
        return this.offset(Direction.UP, i);
    }

    public BlockPos down() {
        return this.offset(Direction.DOWN);
    }

    public BlockPos down(int i) {
        return this.offset(Direction.DOWN, i);
    }

    public BlockPos north() {
        return this.offset(Direction.NORTH);
    }

    public BlockPos north(int i) {
        return this.offset(Direction.NORTH, i);
    }

    public BlockPos south() {
        return this.offset(Direction.SOUTH);
    }

    public BlockPos south(int i) {
        return this.offset(Direction.SOUTH, i);
    }

    public BlockPos west() {
        return this.offset(Direction.WEST);
    }

    public BlockPos west(int i) {
        return this.offset(Direction.WEST, i);
    }

    public BlockPos east() {
        return this.offset(Direction.EAST);
    }

    public BlockPos east(int i) {
        return this.offset(Direction.EAST, i);
    }

    public BlockPos offset(Direction direction) {
        return new BlockPos(this.getX() + direction.getOffsetX(), this.getY() + direction.getOffsetY(), this.getZ() + direction.getOffsetZ());
    }

    public BlockPos offset(Direction direction, int i) {
        return i == 0 ? this : new BlockPos(this.getX() + direction.getOffsetX() * i, this.getY() + direction.getOffsetY() * i, this.getZ() + direction.getOffsetZ() * i);
    }

    public BlockPos offset(Axis axis, int i) {
        if (i == 0) {
            return this;
        } else {
            int j = axis == Axis.X ? i : 0;
            int k = axis == Axis.Y ? i : 0;
            int l = axis == Axis.Z ? i : 0;
            return new BlockPos(this.getX() + j, this.getY() + k, this.getZ() + l);
        }
    }

    public BlockPos rotate(BlockRotation blockRotation) {
        switch (blockRotation) {
            case NONE:
            default:
                return this;
            case CLOCKWISE_90:
                return new BlockPos(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180:
                return new BlockPos(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90:
                return new BlockPos(this.getZ(), this.getY(), -this.getX());
        }
    }

    public BlockPos crossProduct(Vec3i vec3i) {
        return new BlockPos(this.getY() * vec3i.getZ() - this.getZ() * vec3i.getY(), this.getZ() * vec3i.getX() - this.getX() * vec3i.getZ(), this.getX() * vec3i.getY() - this.getY() * vec3i.getX());
    }

    public BlockPos withY(int i) {
        return new BlockPos(this.getX(), i, this.getZ());
    }

    public BlockPos toImmutable() {
        return this;
    }

    public static Iterable<BlockPos> iterateOutwards(BlockPos blockPos, int i, int j, int k) {
        int l = i + j + k;
        int m = blockPos.getX();
        int n = blockPos.getY();
        int o = blockPos.getZ();
        return () -> new AbstractIterator<BlockPos>() {
            private final Mutable pos = new Mutable();
            private int manhattanDistance;
            private int limitX;
            private int limitY;
            private int dx;
            private int dy;
            private boolean swapZ;

            protected BlockPos computeNext() {
                if (this.swapZ) {
                    this.swapZ = false;
                    this.pos.setZ(o - (this.pos.getZ() - o));
                    return this.pos;
                } else {
                    BlockPos blockPos;
                    for(blockPos = null; blockPos == null; ++this.dy) {
                        if (this.dy > this.limitY) {
                            ++this.dx;
                            if (this.dx > this.limitX) {
                                ++this.manhattanDistance;
                                if (this.manhattanDistance > l) {
                                    return (BlockPos)this.endOfData();
                                }

                                this.limitX = Math.min(i, this.manhattanDistance);
                                this.dx = -this.limitX;
                            }

                            this.limitY = Math.min(j, this.manhattanDistance - Math.abs(this.dx));
                            this.dy = -this.limitY;
                        }

                        int i = this.dx;
                        int j = this.dy;
                        int k = this.manhattanDistance - Math.abs(i) - Math.abs(j);
                        if (k <= k) {
                            this.swapZ = k != 0;
                            blockPos = this.pos.set(m + i, n + j, o + k);
                        }
                    }

                    return blockPos;
                }
            }
        };
    }

    public static Optional<BlockPos> findClosest(BlockPos blockPos, int i, int j, Predicate<BlockPos> predicate) {
        for(BlockPos blockPos2 : iterateOutwards(blockPos, i, j, i)) {
            if (predicate.test(blockPos2)) {
                return Optional.of(blockPos2);
            }
        }

        return Optional.empty();
    }

    public static Stream<BlockPos> streamOutwards(BlockPos blockPos, int i, int j, int k) {
        return StreamSupport.stream(iterateOutwards(blockPos, i, j, k).spliterator(), false);
    }

    public static Iterable<BlockPos> iterate(BlockPos blockPos, BlockPos blockPos2) {
        return iterate(Math.min(blockPos.getX(), blockPos2.getX()), Math.min(blockPos.getY(), blockPos2.getY()), Math.min(blockPos.getZ(), blockPos2.getZ()), Math.max(blockPos.getX(), blockPos2.getX()), Math.max(blockPos.getY(), blockPos2.getY()), Math.max(blockPos.getZ(), blockPos2.getZ()));
    }

    public static Stream<BlockPos> stream(BlockPos blockPos, BlockPos blockPos2) {
        return StreamSupport.stream(iterate(blockPos, blockPos2).spliterator(), false);
    }

    public static Stream<BlockPos> stream(BlockBox blockBox) {
        return stream(Math.min(blockBox.getMinX(), blockBox.getMaxX()), Math.min(blockBox.getMinY(), blockBox.getMaxY()), Math.min(blockBox.getMinZ(), blockBox.getMaxZ()), Math.max(blockBox.getMinX(), blockBox.getMaxX()), Math.max(blockBox.getMinY(), blockBox.getMaxY()), Math.max(blockBox.getMinZ(), blockBox.getMaxZ()));
    }

    public static Stream<BlockPos> stream(Box box) {
        return stream(MathHelper.floor(box.minX), MathHelper.floor(box.minY), MathHelper.floor(box.minZ), MathHelper.floor(box.maxX), MathHelper.floor(box.maxY), MathHelper.floor(box.maxZ));
    }

    public static Stream<BlockPos> stream(int i, int j, int k, int l, int m, int n) {
        return StreamSupport.stream(iterate(i, j, k, l, m, n).spliterator(), false);
    }

    public static Iterable<BlockPos> iterate(int i, int j, int k, int l, int m, int n) {
        int o = l - i + 1;
        int p = m - j + 1;
        int q = n - k + 1;
        int r = o * p * q;
        return () -> new AbstractIterator<BlockPos>() {
            private final Mutable pos = new Mutable();
            private int index;

            protected BlockPos computeNext() {
                if (this.index == r) {
                    return (BlockPos)this.endOfData();
                } else {
                    int i = this.index % o;
                    int j = this.index / o;
                    int k = j % p;
                    int l = j / p;
                    ++this.index;
                    return this.pos.set(i + i, j + k, k + l);
                }
            }
        };
    }

    static {
        ORIGIN = new BlockPos(0, 0, 0);
        SIZE_BITS_X = 1 + MathHelper.floorLog2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
        SIZE_BITS_Z = SIZE_BITS_X;
        SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
        BITS_X = (1L << SIZE_BITS_X) - 1L;
        BITS_Y = (1L << SIZE_BITS_Y) - 1L;
        BITS_Z = (1L << SIZE_BITS_Z) - 1L;
        BIT_SHIFT_Z = SIZE_BITS_Y;
        BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;
    }

    public static class Mutable extends BlockPos {
        public Mutable() {
            this(0, 0, 0);
        }

        public Mutable(int i, int j, int k) {
            super(i, j, k);
        }

        public Mutable(double d, double e, double f) {
            this(MathHelper.floor(d), MathHelper.floor(e), MathHelper.floor(f));
        }

        public BlockPos add(double d, double e, double f) {
            return super.add(d, e, f).toImmutable();
        }

        public BlockPos add(int i, int j, int k) {
            return super.add(i, j, k).toImmutable();
        }

        public BlockPos multiply(int i) {
            return super.multiply(i).toImmutable();
        }

        public BlockPos offset(Direction direction, int i) {
            return super.offset(direction, i).toImmutable();
        }

        public BlockPos offset(Axis axis, int i) {
            return super.offset(axis, i).toImmutable();
        }

        public BlockPos rotate(BlockRotation blockRotation) {
            return super.rotate(blockRotation).toImmutable();
        }

        public Mutable set(int i, int j, int k) {
            this.setX(i);
            this.setY(j);
            this.setZ(k);
            return this;
        }

        public Mutable set(double d, double e, double f) {
            return this.set(MathHelper.floor(d), MathHelper.floor(e), MathHelper.floor(f));
        }

        public Mutable set(Vec3i vec3i) {
            return this.set(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        }

        public Mutable set(long l) {
            return this.set(unpackLongX(l), unpackLongY(l), unpackLongZ(l));
        }

        public Mutable set(Vec3i vec3i, Direction direction) {
            return this.set(vec3i.getX() + direction.getOffsetX(), vec3i.getY() + direction.getOffsetY(), vec3i.getZ() + direction.getOffsetZ());
        }

        public Mutable set(Vec3i vec3i, int i, int j, int k) {
            return this.set(vec3i.getX() + i, vec3i.getY() + j, vec3i.getZ() + k);
        }

        public Mutable set(Vec3i vec3i, Vec3i vec3i2) {
            return this.set(vec3i.getX() + vec3i2.getX(), vec3i.getY() + vec3i2.getY(), vec3i.getZ() + vec3i2.getZ());
        }

        public Mutable move(Direction direction) {
            return this.move(direction, 1);
        }

        public Mutable move(Direction direction, int i) {
            return this.set(this.getX() + direction.getOffsetX() * i, this.getY() + direction.getOffsetY() * i, this.getZ() + direction.getOffsetZ() * i);
        }

        public Mutable move(int i, int j, int k) {
            return this.set(this.getX() + i, this.getY() + j, this.getZ() + k);
        }

        public Mutable move(Vec3i vec3i) {
            return this.set(this.getX() + vec3i.getX(), this.getY() + vec3i.getY(), this.getZ() + vec3i.getZ());
        }

        public Mutable clamp(Axis axis, int i, int j) {
            switch (axis) {
                case X -> {
                    return this.set(MathHelper.clamp(this.getX(), i, j), this.getY(), this.getZ());
                }
                case Y -> {
                    return this.set(this.getX(), MathHelper.clamp(this.getY(), i, j), this.getZ());
                }
                case Z -> {
                    return this.set(this.getX(), this.getY(), MathHelper.clamp(this.getZ(), i, j));
                }
                default -> throw new IllegalStateException("Unable to clamp axis " + axis);
            }
        }

        public Mutable setX(int i) {
            super.setX(i);
            return this;
        }

        public Mutable setY(int i) {
            super.setY(i);
            return this;
        }

        public Mutable setZ(int i) {
            super.setZ(i);
            return this;
        }

        public BlockPos toImmutable() {
            return new BlockPos(this);
        }
    }
}
