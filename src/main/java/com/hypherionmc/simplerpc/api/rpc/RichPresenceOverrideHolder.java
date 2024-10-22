package com.hypherionmc.simplerpc.api.rpc;

import dev.firstdark.rpc.models.DiscordRichPresence;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

/**
 * @author HypherionSA
 *
 * A holder that contains an overidden Rich Presence
 */
@RequiredArgsConstructor
public class RichPresenceOverrideHolder implements RichPresenceContainer, Supplier<DiscordRichPresence> {

    private final RichPresenceBuilder builder;
    private final boolean isActive;

    /**
     * Convert the config to a RichPresence
     *
     * @return A copy of {@link RichPresenceBuilder} ready to be used
     */
    @Override
    public RichPresenceBuilder buildPresence() {
        return builder;
    }

    /**
     * Is the current RPC section active, and should it be used
     *
     * @return {@link Boolean#TRUE} if enabled
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public DiscordRichPresence get() {
        return buildPresence().getPresence();
    }
}
