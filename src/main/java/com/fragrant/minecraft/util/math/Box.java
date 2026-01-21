package com.fragrant.minecraft.util.math;

public class Box {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public Box(double d, double e, double f, double g, double h, double i) {
        this.minX = Math.min(d, g);
        this.minY = Math.min(e, h);
        this.minZ = Math.min(f, i);
        this.maxX = Math.max(d, g);
        this.maxY = Math.max(e, h);
        this.maxZ = Math.max(f, i);
    }

    public Box(BlockPos blockPos) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);
    }

    public Box(BlockPos blockPos, BlockPos blockPos2) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
    }

    public static Box from(BlockBox blockBox) {
        return new Box(blockBox.getMinX(), blockBox.getMinY(), blockBox.getMinZ(), blockBox.getMaxX() + 1, blockBox.getMaxY() + 1, blockBox.getMaxZ() + 1);
    }

    public Box withMinX(double d) {
        return new Box(d, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public Box withMinY(double d) {
        return new Box(this.minX, d, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public Box withMinZ(double d) {
        return new Box(this.minX, this.minY, d, this.maxX, this.maxY, this.maxZ);
    }

    public Box withMaxX(double d) {
        return new Box(this.minX, this.minY, this.minZ, d, this.maxY, this.maxZ);
    }

    public Box withMaxY(double d) {
        return new Box(this.minX, this.minY, this.minZ, this.maxX, d, this.maxZ);
    }

    public Box withMaxZ(double d) {
        return new Box(this.minX, this.minY, this.minZ, this.maxX, this.maxY, d);
    }

    public double getMin(Direction.Axis axis) {
        return axis.choose(this.minX, this.minY, this.minZ);
    }

    public double getMax(Direction.Axis axis) {
        return axis.choose(this.maxX, this.maxY, this.maxZ);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Box box)) {
            return false;
        } else {
            if (Double.compare(box.minX, this.minX) != 0) {
                return false;
            } else if (Double.compare(box.minY, this.minY) != 0) {
                return false;
            } else if (Double.compare(box.minZ, this.minZ) != 0) {
                return false;
            } else if (Double.compare(box.maxX, this.maxX) != 0) {
                return false;
            } else if (Double.compare(box.maxY, this.maxY) != 0) {
                return false;
            } else {
                return Double.compare(box.maxZ, this.maxZ) == 0;
            }
        }
    }

    public int hashCode() {
        int i = Double.hashCode(this.minX);
        i = 31 * i + Double.hashCode(this.minY);
        i = 31 * i + Double.hashCode(this.minZ);
        i = 31 * i + Double.hashCode(this.maxX);
        i = 31 * i + Double.hashCode(this.maxY);
        i = 31 * i + Double.hashCode(this.maxZ);
        return i;
    }

    public Box shrink(double d, double e, double f) {
        double g = this.minX;
        double h = this.minY;
        double i = this.minZ;
        double j = this.maxX;
        double k = this.maxY;
        double l = this.maxZ;
        if (d < (double)0.0F) {
            g -= d;
        } else if (d > (double)0.0F) {
            j -= d;
        }

        if (e < (double)0.0F) {
            h -= e;
        } else if (e > (double)0.0F) {
            k -= e;
        }

        if (f < (double)0.0F) {
            i -= f;
        } else if (f > (double)0.0F) {
            l -= f;
        }

        return new Box(g, h, i, j, k, l);
    }

    public Box stretch(double d, double e, double f) {
        double g = this.minX;
        double h = this.minY;
        double i = this.minZ;
        double j = this.maxX;
        double k = this.maxY;
        double l = this.maxZ;
        if (d < (double)0.0F) {
            g += d;
        } else if (d > (double)0.0F) {
            j += d;
        }

        if (e < (double)0.0F) {
            h += e;
        } else if (e > (double)0.0F) {
            k += e;
        }

        if (f < (double)0.0F) {
            i += f;
        } else if (f > (double)0.0F) {
            l += f;
        }

        return new Box(g, h, i, j, k, l);
    }

    public Box expand(double d, double e, double f) {
        double g = this.minX - d;
        double h = this.minY - e;
        double i = this.minZ - f;
        double j = this.maxX + d;
        double k = this.maxY + e;
        double l = this.maxZ + f;
        return new Box(g, h, i, j, k, l);
    }

    public Box expand(double d) {
        return this.expand(d, d, d);
    }

    public Box intersection(Box box) {
        double d = Math.max(this.minX, box.minX);
        double e = Math.max(this.minY, box.minY);
        double f = Math.max(this.minZ, box.minZ);
        double g = Math.min(this.maxX, box.maxX);
        double h = Math.min(this.maxY, box.maxY);
        double i = Math.min(this.maxZ, box.maxZ);
        return new Box(d, e, f, g, h, i);
    }

    public Box union(Box box) {
        double d = Math.min(this.minX, box.minX);
        double e = Math.min(this.minY, box.minY);
        double f = Math.min(this.minZ, box.minZ);
        double g = Math.max(this.maxX, box.maxX);
        double h = Math.max(this.maxY, box.maxY);
        double i = Math.max(this.maxZ, box.maxZ);
        return new Box(d, e, f, g, h, i);
    }

    public Box offset(double d, double e, double f) {
        return new Box(this.minX + d, this.minY + e, this.minZ + f, this.maxX + d, this.maxY + e, this.maxZ + f);
    }

    public Box offset(BlockPos blockPos) {
        return new Box(this.minX + (double)blockPos.getX(), this.minY + (double)blockPos.getY(), this.minZ + (double)blockPos.getZ(), this.maxX + (double)blockPos.getX(), this.maxY + (double)blockPos.getY(), this.maxZ + (double)blockPos.getZ());
    }

    public boolean intersects(Box box) {
        return this.intersects(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public boolean intersects(double d, double e, double f, double g, double h, double i) {
        return this.minX < g && this.maxX > d && this.minY < h && this.maxY > e && this.minZ < i && this.maxZ > f;
    }

    public boolean contains(double d, double e, double f) {
        return d >= this.minX && d < this.maxX && e >= this.minY && e < this.maxY && f >= this.minZ && f < this.maxZ;
    }

    public double getXLength() {
        return this.maxX - this.minX;
    }

    public double getYLength() {
        return this.maxY - this.minY;
    }

    public double getZLength() {
        return this.maxZ - this.minZ;
    }

    public String toString() {
        return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean isValid() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }
}
