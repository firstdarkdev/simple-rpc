package com.hypherionmc.simplerpc.integrations.known;

import org.jetbrains.annotations.ApiStatus;

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

    /**
     * Register a Custom Dimension to be resolved by the DIMENSION placeholder
     *
     * @param resourceLocation The ResourceLocation of the Dimension. For example: minecraft:overworld
     * @param displayName The name that will be displayed when the RPC placeholder is resolved
     */
    public static void addKnownDimension(String resourceLocation, String displayName) {
        knownNames.put(resourceLocation, displayName);
    }

    @ApiStatus.Internal
    public static String tryKnownDimensions(String dimension) {
        if (knownNames.containsKey(dimension)) {
            return knownNames.get(dimension);
        }
        return dimension;
    }

}
