package com.hypherionmc.simplerpc.api.variables.validation;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 *
 * A String validator used by {@link com.hypherionmc.simplerpc.api.variables.PlaceholderEngine} to ensure the input string
 * is not NULL or empty
 */
@RequiredArgsConstructor(staticName = "of")
public class NotEmptyValidator implements Validator {

    @Nullable
    private final String testObject;

    @Override
    public boolean validate() {
        return testObject != null && !testObject.isEmpty();
    }
}
