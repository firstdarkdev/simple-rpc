package com.hypherionmc.simplerpc.api.rpc;

import com.hypherionmc.simplerpc.api.variables.PlaceholderEngine;
import com.hypherionmc.simplerpc.util.rpcavatar.RPCImageServer;
import dev.firstdark.rpc.enums.ActivityType;
import dev.firstdark.rpc.models.DiscordRichPresence;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.time.OffsetDateTime;
import java.util.List;

import static com.hypherionmc.simplerpc.api.utils.APIUtils.parseAndLimit;

/**
 * @author HypherionSA
 *
 * Helper class to convert Config values to the required Discord SDK format
 */
@Getter
public final class RichPresenceBuilder {

    public static OffsetDateTime offsetDateTime = OffsetDateTime.now();
    public static OffsetDateTime rpcTime;
    private List<ButtonWrapper> buttons;
    private final DiscordRichPresence.DiscordRichPresenceBuilder presence;
    private String state = "";
    private String details = "";
    private String largeImage = "";
    private String largeImageText = "";
    private String smallImage = "";
    private String smallImageText = "";
    private ActivityType type = ActivityType.PLAYING;

    public RichPresenceBuilder() {
        this.presence = DiscordRichPresence.builder();
    }

    /**
     * The state value to be used on the RPC
     *
     * @param state The new text to use for the RPC
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * The details value to be used on the RPC
     *
     * @param details The new text to use for the RPC
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setDetails(String details) {
        this.details = details;
        return this;
    }

    /**
     * The Large Image URL or key to be used on the RPC
     *
     * @param largeImage URL or key to be used
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setLargeImage(String largeImage) {
        this.largeImage = largeImage;
        return this;
    }

    /**
     * The Large Image Text that will be displayed on hover
     *
     * @param largeImageText The new text to use for the RPC
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setLargeImageText(String largeImageText) {
        this.largeImageText = largeImageText;
        return this;
    }

    /**
     * The Small Image URL or key to be used on the RPC
     *
     * @param smallImage URL or key to be used
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setSmallImage(String smallImage) {
        this.smallImage = smallImage;
        return this;
    }

    /**
     * The Small Image Text that will be displayed on hover
     *
     * @param smallImageText The new text to use for the RPC
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setSmallImageText(String smallImageText) {
        this.smallImageText = smallImageText;
        return this;
    }

    /**
     * Override the timestamp displayed on the RPC
     *
     * @param offsetDateTime The new {@link OffsetDateTime} to use for the RPC
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setTimeStamp(OffsetDateTime offsetDateTime) {
        rpcTime = offsetDateTime;
        return this;
    }

    /**
     * Add buttons to the RPC
     * Use {@link ButtonWrapper} to construct the buttons
     *
     * @param buttons New list of buttons to use
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setButtons(List<ButtonWrapper> buttons) {
        this.buttons = buttons;
        return this;
    }

    /**
     * What type of activity is this RPC. Defaults to {@link ActivityType#PLAYING}
     * @param type The new {@link ActivityType} to use for the RPC
     * @return Updated instance of {@link dev.firstdark.rpc.models.DiscordRichPresence.DiscordRichPresenceBuilder}
     */
    public RichPresenceBuilder setType(ActivityType type) {
        this.type = type;
        return this;
    }

    /**
     * Used internally to build the RPC for the Discord SDK
     *
     * @return The fully constructed {@link DiscordRichPresence} ready for the API
     */
    @ApiStatus.Internal
    public DiscordRichPresence getPresence() {
        if (!state.isEmpty())
            presence.state(parseAndLimit(state, 128));

        if (!details.isEmpty())
            presence.details(parseAndLimit(details, 128));

        if (!largeImage.isEmpty()) {
            presence.largeImageKey(processImage(largeImage));

            if (!largeImageText.isEmpty()) {
                presence.largeImageText(parseAndLimit(largeImageText, 128));
            }
        }

        if (!smallImage.isEmpty()) {
            presence.smallImageKey(processImage(smallImage));

            if (!smallImageText.isEmpty()) {
                presence.smallImageText(parseAndLimit(smallImageText, 128));
            }

        }

        if (buttons != null && !buttons.isEmpty()) {
            int length = Math.min(buttons.size(), 2);
            presence.buttons(this.buttons.subList(0, length).stream().map(ButtonWrapper::rpcButton).toList());
        }

        presence.startTimestamp(offsetDateTime.toEpochSecond());
        presence.activityType(this.type);

        return presence.build();
    }

    /**
     * Process image keys, to check if the specified image key is a local file, URL or discord asset key
     *
     * @param input The config input to parse
     * @return The image URL for the RPC Image server, or the raw input with placeholders parsed
     */
    private String processImage(String input) {
        input = PlaceholderEngine.INSTANCE.resolvePlaceholders(input);

        if (!RPCImageServer.INSTANCE.isUploading() && !input.startsWith("http") && (input.endsWith(".png")
                || input.endsWith(".jpg") || input.endsWith(".jpeg") || input.endsWith(".gif") || input.endsWith(".webp") || input.endsWith(".svg"))) {
            return RPCImageServer.INSTANCE.getImageUrl() + "/" + RPCImageServer.INSTANCE.getHash(input);
        }

        return input;
    }

}
