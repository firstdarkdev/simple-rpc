package com.hypherionmc.simplerpc.config.base;

import com.hypherionmc.simplerpc.discord.RichPresenceBuilder;
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

    @Override
    public RichPresenceBuilder buildPresence() {
        return builder;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public DiscordRichPresence get() {
        return buildPresence().getPresence();
    }
}
