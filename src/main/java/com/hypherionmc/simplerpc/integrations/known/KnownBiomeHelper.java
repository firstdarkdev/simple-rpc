package com.hypherionmc.simplerpc.integrations.known;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HypherionSA
 *
 * Compat for a list of known Biomes that don't resolve properly
 */
public class KnownBiomeHelper {

    private static final Map<String, String> knownNames = new HashMap<>() {{
        put("biome.dimensionalpocketsii.pocket", "Pocket Dimension");
    }};

    public static String tryKnownBiomes(String biome) {
        if (knownNames.containsKey(biome)) {
            return knownNames.get(biome);
        }
        return biome;
    }

}
