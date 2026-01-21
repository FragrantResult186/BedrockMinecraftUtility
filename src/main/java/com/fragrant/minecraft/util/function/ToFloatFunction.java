package com.fragrant.minecraft.util.function;

@FunctionalInterface
public interface ToFloatFunction<C> {
    float apply(C object);
}
