package com.hypherionmc.simplerpc.api.variables;

import com.hypherionmc.simplerpc.RPCConstants;
import com.hypherionmc.simplerpc.api.variables.validation.UnsafeSupplier;
import com.hypherionmc.simplerpc.api.variables.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HypherionSA
 *
 * Placeholder registry and resolver system
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlaceholderEngine {

    // Public Instance
    public static final PlaceholderEngine INSTANCE = new PlaceholderEngine();

    // Holders for registered placeholders
    private final HashMap<String, Variable> placeholders = new LinkedHashMap<>();
    private final Pattern placeholderPattern = Pattern.compile("\\{\\{\\s*([\\w.]+)\\s*\\}\\}");
    private final Validator EMPTY = () -> true;

    /**
     * Register a new placeholder that is always active, and will always return a value
     *
     * @param holder The placeholder name. Will resolve to {{placeholdername}}
     * @param defaultValue The default value to return, if the intended value cannot be resolved
     * @param resolver The {@link UnsafeSupplier} that will resolve the final value
     */
    public void registerPlaceholder(String holder, String defaultValue, UnsafeSupplier<String> resolver) {
        this.registerPlaceholder(holder, EMPTY, defaultValue, resolver);
    }

    /**
     * Register a new placeholder, that is only active when certain checks are met
     *
     * @param holder The placeholder name. Will resolve to {{placeholdername}}
     * @param validator A {@link Validator} to use to determine if the placeholder can be used
     * @param defaultValue The default value to return, if the intended value cannot be resolved
     * @param resolver The {@link UnsafeSupplier} that will resolve the final value
     */
    public void registerPlaceholder(String holder, Validator validator, String defaultValue, UnsafeSupplier<String> resolver) {
        if (placeholders.containsKey(holder))
            throw new RuntimeException("Tried to register duplicate RPC Variable " + holder);

        placeholders.put(holder, new Variable(holder, defaultValue, resolver, validator));
    }

    /**
     * Resolve all valid placeholders in a string
     *
     * @param inputString The string to process
     * @return The fully resolved string, with placeholders removed
     */
    public String resolvePlaceholders(String inputString) {
        Matcher matcher = placeholderPattern.matcher(inputString);

        while (matcher.find()) {
            String key = matcher.group(1);
            Variable replacement = placeholders.get(key);

            if (replacement != null) {
                inputString = inputString.replace(matcher.group(0), replacement.resolve());
            } else {
                inputString = inputString.replace(matcher.group(0), "Unknown Placeholder");
            }
        }

        return inputString;
    }

    /**
     * Clear all registered placeholders
     */
    public void clear() {
        this.placeholders.clear();
    }

    /**
     * Internal holder for Placeholders
     *
     * @param holder The placeholder key
     * @param defaultValue The default value to return
     * @param resolver The {@link UnsafeSupplier} to use to resolve the placeholder
     * @param validator The {@link Validator} to use to see if the placeholder is active
     */
    record Variable(String holder, String defaultValue, UnsafeSupplier<String> resolver, Validator validator) {

        /**
         * Try to safely resolve the placeholder, or return the default value
         *
         * @return The resolved value, or default value
         */
        String resolve() {
            if (!validator().validate())
                return defaultValue;

            try {
                String val = resolver.get();
                return val == null ? defaultValue : val;
            } catch (Exception e) {
                RPCConstants.logger.error("Failed to parse placeholder {}: {}", holder, e);
            }

            return defaultValue;
        }
    }
}
