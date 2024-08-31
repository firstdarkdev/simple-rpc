package com.hypherionmc.simplerpc.integrations.known;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HypherionSA
 *
 * Compat for a list of known Dimensions that don't resolve properly
 */
public class KnownDimensionHelper {

    private static final Map<String, String> knownNames = new HashMap<>() {{
        put("dimensionalpocketsii:pocket", "Pocket Dimension");
    }};

    public static String tryKnownDimensions(String dimension) {
        if (knownNames.containsKey(dimension)) {
            return knownNames.get(dimension);
        }
        return dimension;
    }

}
