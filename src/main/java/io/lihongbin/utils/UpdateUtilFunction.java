package io.lihongbin.utils;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface UpdateUtilFunction <T, R> extends Function<T, R>, Serializable {
}
