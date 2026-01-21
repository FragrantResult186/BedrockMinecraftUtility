package com.fragrant.minecraft.util;

import com.mojang.datafixers.DataFixUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Util {
    public static <T> T make(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

    public static <T> Stream<T> stream(Optional<? extends T> optional) {
        return DataFixUtils.orElseGet(optional.map(Stream::of), Stream::empty);
    }

    public static <T> T getRandom(T[] objects, Random random) {
        return objects[random.nextInt(objects.length)];
    }
}
