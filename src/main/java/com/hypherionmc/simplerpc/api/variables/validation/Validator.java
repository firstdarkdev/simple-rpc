package com.hypherionmc.simplerpc.api.variables.validation;

/**
 * @author HypherionSA
 *
 * A helper interfact to determine if an RPC placeholder can be used
 */
@FunctionalInterface
public interface Validator {

    boolean validate();

}
