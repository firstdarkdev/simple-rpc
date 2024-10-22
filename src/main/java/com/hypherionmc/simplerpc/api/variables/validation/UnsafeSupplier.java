package com.hypherionmc.simplerpc.api.variables.validation;

/**
 * @author HypherionSA
 * A supplier that CAN throw an exception when trying to resolve its value.
 * This means you should never trust that the supplier could have a valid value
 * @param <T>
 */
@FunctionalInterface
public interface  UnsafeSupplier<T> {
    T get() throws Exception;
}
