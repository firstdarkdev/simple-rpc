package com.hypherionmc.simplerpc.api.variables.validation;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 *
 * An Object validator used by {@link com.hypherionmc.simplerpc.api.variables.PlaceholderEngine} to ensure the input object
 * is not NULL
 */
@RequiredArgsConstructor(staticName = "of")
public class NotNullValidator implements Validator {

    @Nullable
    private final Object testObject;

    @Override
    public boolean validate() {
        return testObject != null;
    }
}
