package com.fragrant.minecraft.util.math;

import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.fragrant.minecraft.util.Util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {
    DOWN(0, 1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
    UP(1, 0, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
    NORTH(2, 3, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
    SOUTH(3, 2, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
    WEST(4, 5, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
    EAST(5, 4, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));

    private final int id;
    private final int idOpposite;
    private final String name;
    private final Axis axis;
    private final AxisDirection direction;
    private final Vec3i vector;
    private static final Direction[] ALL = values();
    private static final Direction[] VALUES = Arrays.stream(ALL).sorted(Comparator.comparingInt((direction) -> direction.id)).toArray(Direction[]::new);

    static {
        Arrays.stream(ALL).collect(Collectors.toMap((direction) -> (new BlockPos(direction.getVector())).asLong(), (direction) -> direction, (direction, direction2) -> {
            throw new IllegalArgumentException("Duplicate keys");
        }, Long2ObjectOpenHashMap::new));
    }

    Direction(int j, int k, String string2, AxisDirection axisDirection, Axis axis, Vec3i vec3i) {
        this.id = j;
        this.idOpposite = k;
        this.name = string2;
        this.axis = axis;
        this.direction = axisDirection;
        this.vector = vec3i;
    }

    public AxisDirection getDirection() {
        return this.direction;
    }

    public Direction getOpposite() {
        return byId(this.idOpposite);
    }

    public int getOffsetX() {
        return this.vector.getX();
    }

    public int getOffsetY() {
        return this.vector.getY();
    }

    public int getOffsetZ() {
        return this.vector.getZ();
    }

    public String getName() {
        return this.name;
    }

    public Axis getAxis() {
        return this.axis;
    }

    public static Direction byId(int i) {
        return VALUES[MathHelper.abs(i % VALUES.length)];
    }

    public static Direction random(Random random) {
        return Util.getRandom(ALL, random);
    }

    public String toString() {
        return this.name;
    }

    public static Direction get(AxisDirection axisDirection, Axis axis) {
        for(Direction direction : ALL) {
            if (direction.getDirection() == axisDirection && direction.getAxis() == axis) {
                return direction;
            }
        }

        throw new IllegalArgumentException("No such direction: " + axisDirection + " " + axis);
    }

    public Vec3i getVector() {
        return this.vector;
    }

    public enum Axis implements Predicate<Direction> {
        X("x") {
            public int choose(int i, int j, int k) {
                return i;
            }

            public double choose(double d, double e, double f) {
                return d;
            }
        },
        Y("y") {
            public int choose(int i, int j, int k) {
                return j;
            }

            public double choose(double d, double e, double f) {
                return e;
            }
        },
        Z("z") {
            public int choose(int i, int j, int k) {
                return k;
            }

            public double choose(double d, double e, double f) {
                return f;
            }
        };

        private final String name;

        Axis(String string2) {
            this.name = string2;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        public boolean test(Direction direction) {
            return direction != null && direction.getAxis() == this;
        }

        public Type getType() {

            return switch (this) {
                case X, Z -> Type.HORIZONTAL;
                case Y -> Type.VERTICAL;
            };
        }

        public abstract int choose(int i, int j, int k);

        public abstract double choose(double d, double e, double f);
    }

    public enum AxisDirection {
        POSITIVE("Towards positive"),
        NEGATIVE("Towards negative");

        private final String description;

        AxisDirection(String string2) {
            this.description = string2;
        }

        public String toString() {
            return this.description;
        }
    }

    public enum Type implements Iterable<Direction>, Predicate<Direction> {
        HORIZONTAL(new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}),
        VERTICAL(new Direction[]{Direction.UP, Direction.DOWN});

        private final Direction[] facingArray;

        Type(Direction[] directions) {
            this.facingArray = directions;
        }

        public Direction random(Random random) {
            return Util.getRandom(this.facingArray, random);
        }

        public boolean test(Direction direction) {
            return direction != null && direction.getAxis().getType() == this;
        }

        public Iterator<Direction> iterator() {
            return Iterators.forArray(this.facingArray);
        }

        public Stream<Direction> stream() {
            return Arrays.stream(this.facingArray);
        }
    }
}
