package com.hypherionmc.simplerpc.api.rpc;

import org.apache.commons.lang3.StringUtils;

/**
 * @author HypherionSA
 *
 * Helper interface that helps to convert Config to Rich Presence
 * Also implements logic for "overriding" one RPC with another
 */
public interface RichPresenceContainer {

    /**
     * Convert the config to a RichPresence
     *
     * @return A copy of {@link RichPresenceBuilder} ready to be used
     */
    RichPresenceBuilder buildPresence();

    /**
     * Is the current RPC section active, and should it be used
     *
     * @return {@link Boolean#TRUE} if enabled
     */
    boolean isActive();

    /**
     * Override the values of one rich presence, with the values of another
     *
     * @param overrideContainer The new rich presence that will override the old one
     * @return A {@link RichPresenceOverrideHolder} that can be overridden again with another rich presence
     */
    default RichPresenceOverrideHolder overrideWith(RichPresenceContainer overrideContainer) {
        RichPresenceBuilder original = buildPresence();

        if (!overrideContainer.isActive())
            return new RichPresenceOverrideHolder(original, this.isActive());

        RichPresenceBuilder override = overrideContainer.buildPresence();

        if (!StringUtils.isBlank(override.getDetails()))
            original.setDetails(override.getDetails());

        if (!StringUtils.isEmpty(override.getState()))
            original.setState(override.getState());

        if (!StringUtils.isEmpty(override.getLargeImage()) && !override.getLargeImage().equalsIgnoreCase("ignored"))
            original.setLargeImage(override.getLargeImage());

        if (!StringUtils.isEmpty(override.getLargeImageText()) && !override.getLargeImageText().equalsIgnoreCase("ignored"))
            original.setLargeImageText(override.getLargeImageText());

        if (!StringUtils.isEmpty(override.getSmallImage()) && !override.getSmallImage().equalsIgnoreCase("ignored"))
            original.setSmallImage(override.getSmallImage());

        if (!StringUtils.isEmpty(override.getSmallImageText()) && !override.getSmallImageText().equalsIgnoreCase("ignored"))
            original.setSmallImageText(override.getSmallImageText());

        if (!override.getButtons().isEmpty())
            original.setButtons(override.getButtons());

        if (override.getType() != original.getType())
            original.setType(override.getType());

        return new RichPresenceOverrideHolder(original, this.isActive());
    }

}
