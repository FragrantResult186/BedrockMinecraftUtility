package com.fragrant.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import com.fragrant.minecraft.util.function.ToFloatFunction;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Spline<C> extends ToFloatFunction<C> {

    String getDebugString();

    static <C> Builder<C> builder(ToFloatFunction<C> toFloatFunction) {
        return new Builder<>(toFloatFunction);
    }

    static <C> Builder<C> builder(ToFloatFunction<C> toFloatFunction, ToFloatFunction<Float> toFloatFunction2) {
        return new Builder<>(toFloatFunction, toFloatFunction2);
    }

    record class_6738<C>(ToFloatFunction<C> comp_229, float[] comp_230, List<Spline<C>> comp_231, float[] comp_232) implements Spline<C> {
        public class_6738(ToFloatFunction<C> comp_229, float[] comp_230, List<Spline<C>> comp_231, float[] comp_232) {
            if (comp_230.length == comp_231.size() && comp_230.length == comp_232.length) {
                this.comp_229 = comp_229;
                this.comp_230 = comp_230;
                this.comp_231 = comp_231;
                this.comp_232 = comp_232;
            } else {
                throw new IllegalArgumentException("All lengths must be equal, got: " + comp_230.length + " " + comp_231.size() + " " + comp_232.length);
            }
        }

        public float apply(C object) {
            float f = this.comp_229.apply(object);
            int i = MathHelper.binarySearch(0, this.comp_230.length, (ix) -> f < this.comp_230[ix]) - 1;
            int j = this.comp_230.length - 1;
            if (i < 0) {
                return this.comp_231.get(0).apply(object) + this.comp_232[0] * (f - this.comp_230[0]);
            } else if (i == j) {
                return this.comp_231.get(j).apply(object) + this.comp_232[j] * (f - this.comp_230[j]);
            } else {
                float g = this.comp_230[i];
                float h = this.comp_230[i + 1];
                float k = (f - g) / (h - g);
                ToFloatFunction<C> toFloatFunction = this.comp_231.get(i);
                ToFloatFunction<C> toFloatFunction2 = this.comp_231.get(i + 1);
                float l = this.comp_232[i];
                float m = this.comp_232[i + 1];
                float n = toFloatFunction.apply(object);
                float o = toFloatFunction2.apply(object);
                float p = l * (h - g) - (o - n);
                float q = -m * (h - g) + (o - n);
                return MathHelper.lerp(k, n, o) + k * (1.0F - k) * MathHelper.lerp(k, p, q);
            }
        }

        @VisibleForTesting
        public String getDebugString() {
            return "Spline{coordinate=" + this.comp_229 + ", locations=" + this.method_39238(this.comp_230) + ", derivatives=" + this.method_39238(this.comp_232) + ", values=" + (String)this.comp_231.stream().map(Spline::getDebugString).collect(Collectors.joining(", ", "[", "]")) + "}";
        }

        private String method_39238(float[] fs) {
            Stream<String> var10000 = IntStream.range(0, fs.length).mapToDouble((i) -> (double)fs[i]).mapToObj((d) -> String.format(Locale.ROOT, "%.3f", d));
            return "[" + var10000.collect(Collectors.joining(", ")) + "]";
        }
    }

    record FixedFloatFunction<C>(float value) implements Spline<C> {
        public float apply(C object) {
            return this.value;
        }

        public String getDebugString() {
            return String.format("k=%.3f", this.value);
        }
    }

    final class Builder<C> {
        private final ToFloatFunction<C> locationFunction;
        private final ToFloatFunction<Float> field_35661;
        private final FloatList locations;
        private final List<Spline<C>> values;
        private final FloatList derivatives;

        private Builder(ToFloatFunction<C> toFloatFunction) {
            this(toFloatFunction, (float_) -> float_);
        }

        private Builder(ToFloatFunction<C> toFloatFunction, ToFloatFunction<Float> toFloatFunction2) {
            this.locations = new FloatArrayList();
            this.values = Lists.newArrayList();
            this.derivatives = new FloatArrayList();
            this.locationFunction = toFloatFunction;
            this.field_35661 = toFloatFunction2;
        }

        public Builder<C> add(float f, float g, float h) {
            return this.add(f, new FixedFloatFunction<>(this.field_35661.apply(g)), h);
        }

        public Builder<C> add(float f, Spline<C> spline, float g) {
            if (!this.locations.isEmpty() && f <= this.locations.getFloat(this.locations.size() - 1)) {
                throw new IllegalArgumentException("Please register points in ascending order");
            } else {
                this.locations.add(f);
                this.values.add(spline);
                this.derivatives.add(g);
                return this;
            }
        }

        public Spline<C> build() {
            if (this.locations.isEmpty()) {
                throw new IllegalStateException("No elements added");
            } else {
                return new class_6738<>(this.locationFunction, this.locations.toFloatArray(), ImmutableList.copyOf(this.values), this.derivatives.toFloatArray());
            }
        }
    }
}
