package com.hypherionmc.simplerpc.api.utils;

import com.hypherionmc.craterlib.nojang.client.BridgedMinecraft;
import com.hypherionmc.craterlib.nojang.resources.ResourceIdentifier;
import com.hypherionmc.simplerpc.api.variables.PlaceholderEngine;
import com.hypherionmc.simplerpc.config.objects.DimensionSection;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;

/**
 * @author HypherionSA
 *
 * Some internal use API values
 */
public class APIUtils {

    public static String CUR_DIR = System.getProperty("user.dir");

    /**
     * Helper method to make the world name a bit nicer to read
     *
     * @param name The raw world name
     * @return The formatted world name
     */
    public static String worldNameToReadable(String name) {
        if (name.split(":").length > 1) {
            name = name.split(":")[1];
        }
        name = name.replace("_", " ");
        return WordUtils.capitalizeFully(name);
    }

    /**
     * Tries to find a matching Biome/Dimension or both override from the config
     *
     * @param list A copy of the loaded Dimension overrides
     * @return The resolved RPC that will be displayed
     */
    @ApiStatus.Internal
    public static Optional<DimensionSection.Dimension> findDimension(List<DimensionSection.Dimension> list) {
        String dimensionName = getWorld();
        String biome = getBiome();

        if (dimensionName.toLowerCase().contains("minecraft:"))
            dimensionName = dimensionName.replace("minecraft:", "");

        if (biome.toLowerCase().contains("minecraft:"))
            biome = biome.replace("minecraft:", "");

        dimensionName = dimensionName.toLowerCase();
        biome = biome.toLowerCase();

        String finalDimensionName = dimensionName;
        String finalBiomeName = "biome:" + biome;

        if (list.stream().anyMatch(c -> c.name.equalsIgnoreCase(finalDimensionName))) {
            return list.stream().filter(c -> c.name.equalsIgnoreCase(finalDimensionName)).findFirst();
        }

        if (list.stream().anyMatch(c -> c.name.equalsIgnoreCase(finalBiomeName))) {
            return list.stream().filter(c -> c.name.equalsIgnoreCase(finalBiomeName)).findFirst();
        }

        String joined = finalDimensionName + "|" + finalBiomeName.replace("biome:", "");
        return list.stream().filter(c -> c.name.equalsIgnoreCase(joined)).findFirst();
    }

    /**
     * Helper method to resolve placeholders, and limit the output string length. This is required for some RPC fields
     *
     * @param input The Raw string before formatting
     * @param limit The length to limit the return string to
     * @return The formatted, and trimmed string
     */
    public static String parseAndLimit(String input, int limit) {
        String ret = PlaceholderEngine.INSTANCE.resolvePlaceholders(input);
        return ret.substring(0, Math.min(ret.length(), limit));
    }

    /**
     * Internal method to get the world registry key
     *
     * @return The world registry key or unknown
     */
    private static String getWorld() {
        if (BridgedMinecraft.getInstance().getLevel() != null && BridgedMinecraft.getInstance().getLevel().getDimensionKey() != null) {
            return BridgedMinecraft.getInstance().getLevel().getDimensionKey().getString();
        }

        return "unknown";
    }

    /**
     * Internal method to get the biome registry key
     *
     * @return The biome registry key or unknown
     */
    private static String getBiome() {
        if (BridgedMinecraft.getInstance().getLevel() != null && BridgedMinecraft.getInstance().getPlayer() != null) {
            ResourceIdentifier identifier = BridgedMinecraft.getInstance().getLevel().getBiomeIdentifier(BridgedMinecraft.getInstance().getPlayer().getOnPos());
            return identifier != null ? identifier.getString() : "unknown";
        }

        return "unknown";
    }

}
