package com.hypherionmc.simplerpc.integrations.known;

import org.jetbrains.annotations.ApiStatus;

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

    /**
     * Register a Custom Biome to be resolved by the BIOME placeholder
     *
     * @param resourceLocation The ResourceLocation of the Biome. For example: minecraft:plains
     * @param displayName The name that will be displayed when the RPC placeholder is resolved
     */
    public static void addKnownBiome(String resourceLocation, String displayName) {
        knownNames.put(resourceLocation, displayName);
    }

    @ApiStatus.Internal
    public static String tryKnownBiomes(String biome) {
        if (knownNames.containsKey(biome)) {
            return knownNames.get(biome);
        }
        return biome;
    }

}
