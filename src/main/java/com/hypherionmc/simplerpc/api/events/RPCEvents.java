package com.hypherionmc.simplerpc.api.events;

import com.hypherionmc.craterlib.core.event.CraterEvent;
import com.hypherionmc.craterlib.core.event.annot.Cancellable;
import dev.firstdark.rpc.enums.ErrorCode;
import dev.firstdark.rpc.models.DiscordRichPresence;
import dev.firstdark.rpc.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 *
 * Events that fire during the mod lifecycle. To be used by modders to control their own RPC's
 */
public final class RPCEvents {

    /**
     * Fired when the mod is connected to discord, ready to be used
     */
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static final class Ready extends CraterEvent {
        private final User user;
    }

    /**
     * Fired when the mod disconnected from Discord, either due to an error or for some other reason
     */
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static final class Disconnected extends CraterEvent {
        private final ErrorCode errorCode;

        @Nullable
        private final String disconnectReason;
    }

    /**
     * Fired when the mod encounters an error communicating with discord
     */
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static final class Errored extends CraterEvent {
        private final ErrorCode errorCode;

        @Nullable
        private final String disconnectReason;
    }

    /**
     * Fired when the game language is changed
     */
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static final class LanguageChanged extends CraterEvent {
        private final String languageCode;
    }

    /**
     * Fired just before the RPC is updated. Use this to provide your own RPC to override a specific RPC
     */
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    @Cancellable
    public static final class RichPresenceUpdated extends CraterEvent {
        @Nullable
        private final DiscordRichPresence presence;

        @Setter @Nullable
        private DiscordRichPresence newPresence = null;
    }

    /**
     * Fired when the mod is shut down, or reloaded
     */
    @NoArgsConstructor(staticName = "of")
    public static final class RichPresenceShutDown extends CraterEvent {}

    /**
     * Fired when placeholders are registered. They get cleared on reload, so you need this if you use custom placeholders
     */
    @NoArgsConstructor(staticName = "of")
    public static final class RegisterPlaceholders extends CraterEvent {}
}
