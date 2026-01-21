package com.fragrant.minecraft.util.math;

import com.google.common.base.MoreObjects;
import com.fragrant.minecraft.util.math.Direction.Axis;

public class Vec3i implements Comparable<Vec3i> {
    public static final Vec3i ZERO;
    private int x;
    private int y;
    private int z;

    public Vec3i(int i, int j, int k) {
        this.x = i;
        this.y = j;
        this.z = k;
    }

    public Vec3i(double d, double e, double f) {
        this(MathHelper.floor(d), MathHelper.floor(e), MathHelper.floor(f));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec3i)) {
            return false;
        } else {
            Vec3i vec3i = (Vec3i)object;
            if (this.getX() != vec3i.getX()) {
                return false;
            } else if (this.getY() != vec3i.getY()) {
                return false;
            } else {
                return this.getZ() == vec3i.getZ();
            }
        }
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3i vec3i) {
        if (this.getY() == vec3i.getY()) {
            return this.getZ() == vec3i.getZ() ? this.getX() - vec3i.getX() : this.getZ() - vec3i.getZ();
        } else {
            return this.getY() - vec3i.getY();
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    protected Vec3i setX(int i) {
        this.x = i;
        return this;
    }

    protected Vec3i setY(int i) {
        this.y = i;
        return this;
    }

    protected Vec3i setZ(int i) {
        this.z = i;
        return this;
    }

    public Vec3i add(double d, double e, double f) {
        return d == (double)0.0F && e == (double)0.0F && f == (double)0.0F ? this : new Vec3i((double)this.getX() + d, (double)this.getY() + e, (double)this.getZ() + f);
    }

    public Vec3i add(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new Vec3i(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    public Vec3i add(Vec3i vec3i) {
        return this.add(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public Vec3i subtract(Vec3i vec3i) {
        return this.add(-vec3i.getX(), -vec3i.getY(), -vec3i.getZ());
    }

    public Vec3i multiply(int i) {
        if (i == 1) {
            return this;
        } else {
            return i == 0 ? ZERO : new Vec3i(this.getX() * i, this.getY() * i, this.getZ() * i);
        }
    }

    public Vec3i up() {
        return this.up(1);
    }

    public Vec3i up(int i) {
        return this.offset(Direction.UP, i);
    }

    public Vec3i down() {
        return this.down(1);
    }

    public Vec3i down(int i) {
        return this.offset(Direction.DOWN, i);
    }

    public Vec3i north() {
        return this.north(1);
    }

    public Vec3i north(int i) {
        return this.offset(Direction.NORTH, i);
    }

    public Vec3i south() {
        return this.south(1);
    }

    public Vec3i south(int i) {
        return this.offset(Direction.SOUTH, i);
    }

    public Vec3i west() {
        return this.west(1);
    }

    public Vec3i west(int i) {
        return this.offset(Direction.WEST, i);
    }

    public Vec3i east() {
        return this.east(1);
    }

    public Vec3i east(int i) {
        return this.offset(Direction.EAST, i);
    }

    public Vec3i offset(Direction direction) {
        return this.offset((Direction)direction, 1);
    }

    public Vec3i offset(Direction direction, int i) {
        return i == 0 ? this : new Vec3i(this.getX() + direction.getOffsetX() * i, this.getY() + direction.getOffsetY() * i, this.getZ() + direction.getOffsetZ() * i);
    }

    public Vec3i offset(Axis axis, int i) {
        if (i == 0) {
            return this;
        } else {
            int j = axis == Axis.X ? i : 0;
            int k = axis == Axis.Y ? i : 0;
            int l = axis == Axis.Z ? i : 0;
            return new Vec3i(this.getX() + j, this.getY() + k, this.getZ() + l);
        }
    }

    public Vec3i crossProduct(Vec3i vec3i) {
        return new Vec3i(this.getY() * vec3i.getZ() - this.getZ() * vec3i.getY(), this.getZ() * vec3i.getX() - this.getX() * vec3i.getZ(), this.getX() * vec3i.getY() - this.getY() * vec3i.getX());
    }

    public boolean isWithinDistance(Vec3i vec3i, double d) {
        return this.getSquaredDistance((double)vec3i.getX(), (double)vec3i.getY(), (double)vec3i.getZ(), false) < d * d;
    }

    public boolean isWithinDistance(Position position, double d) {
        return this.getSquaredDistance(position.getX(), position.getY(), position.getZ(), true) < d * d;
    }

    public double getSquaredDistance(Vec3i vec3i) {
        return this.getSquaredDistance((double)vec3i.getX(), (double)vec3i.getY(), (double)vec3i.getZ(), true);
    }

    public double getSquaredDistance(Position position, boolean bl) {
        return this.getSquaredDistance(position.getX(), position.getY(), position.getZ(), bl);
    }

    public double getSquaredDistance(Vec3i vec3i, boolean bl) {
        return this.getSquaredDistance((double)vec3i.x, (double)vec3i.y, (double)vec3i.z, bl);
    }

    public double getSquaredDistance(double d, double e, double f, boolean bl) {
        double g = bl ? (double)0.5F : (double)0.0F;
        double h = (double)this.getX() + g - d;
        double i = (double)this.getY() + g - e;
        double j = (double)this.getZ() + g - f;
        return h * h + i * i + j * j;
    }

    public int getManhattanDistance(Vec3i vec3i) {
        float f = (float)Math.abs(vec3i.getX() - this.getX());
        float g = (float)Math.abs(vec3i.getY() - this.getY());
        float h = (float)Math.abs(vec3i.getZ() - this.getZ());
        return (int)(f + g + h);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    static {
        ZERO = new Vec3i(0, 0, 0);
    }
}
