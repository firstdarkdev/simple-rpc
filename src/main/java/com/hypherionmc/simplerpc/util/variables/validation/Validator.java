package com.hypherionmc.simplerpc.util.variables.validation;

/**
 * @author HypherionSA
 *
 * A helper interfact to determine if an RPC placeholder can be used
 */
@FunctionalInterface
public interface Validator {

    boolean vaildate();

}
