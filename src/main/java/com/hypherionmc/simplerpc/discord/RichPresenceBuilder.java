package com.hypherionmc.simplerpc.discord;

import com.hypherionmc.simplerpc.util.variables.PlaceholderEngine;
import dev.firstdark.rpc.enums.ActivityType;
import dev.firstdark.rpc.models.DiscordRichPresence;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

import static com.hypherionmc.simplerpc.util.APIUtils.parseAndLimit;

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

    public RichPresenceBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public RichPresenceBuilder setDetails(String details) {
        this.details = details;
        return this;
    }

    public RichPresenceBuilder setLargeImage(String largeImage) {
        this.largeImage = largeImage;
        return this;
    }

    public RichPresenceBuilder setLargeImageText(String largeImageText) {
        this.largeImageText = largeImageText;
        return this;
    }

    public RichPresenceBuilder setSmallImage(String smallImage) {
        this.smallImage = smallImage;
        return this;
    }

    public RichPresenceBuilder setSmallImageText(String smallImageText) {
        this.smallImageText = smallImageText;
        return this;
    }

    public RichPresenceBuilder setTimeStamp(OffsetDateTime offsetDateTime) {
        rpcTime = offsetDateTime;
        return this;
    }

    public RichPresenceBuilder setButtons(List<ButtonWrapper> buttons) {
        this.buttons = buttons;
        return this;
    }

    public RichPresenceBuilder setType(ActivityType type) {
        this.type = type;
        return this;
    }

    public DiscordRichPresence getPresence() {
        if (!state.isEmpty())
            presence.state(parseAndLimit(state, 128));

        if (!details.isEmpty())
            presence.details(parseAndLimit(details, 128));

        if (!largeImage.isEmpty()) {
            presence.largeImageKey(PlaceholderEngine.INSTANCE.resolvePlaceholders(largeImage));

            if (!largeImageText.isEmpty()) {
                presence.largeImageText(parseAndLimit(largeImageText, 128));
            }
        }

        if (!smallImage.isEmpty()) {
            presence.smallImageKey(PlaceholderEngine.INSTANCE.resolvePlaceholders(smallImage));

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

}
