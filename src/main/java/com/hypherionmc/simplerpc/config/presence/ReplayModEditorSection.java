package com.hypherionmc.simplerpc.config.presence;

import com.hypherionmc.simplerpc.api.rpc.RichPresenceBuilder;
import com.hypherionmc.simplerpc.api.rpc.RichPresenceContainer;
import com.hypherionmc.simplerpc.config.objects.RichPresenceModel;
import dev.firstdark.rpc.enums.ActivityType;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;

import java.util.ArrayList;

/**
 * @author HypherionSA
 *
 * Main RPC that is shown while the player is editing a replay mod video
 */
public final class ReplayModEditorSection implements RichPresenceContainer {

    @Path("enabled")
    @SpecComment("Enable/Disable the ReplayMod Editor Event")
    public boolean enabled = true;

    @Path("presence")
    @SpecComment("List of RPCs that will be displayed at random")
    public RandomArrayList<RichPresenceModel> presence = new RandomArrayList<>() {{
        add(RichPresenceModel.of(
                ActivityType.PLAYING,
                "Editing a video",
                "{{player.name}} is reviewing footage",
                RandomArrayList.of("mclogonew"),
                "It's Minecraft {{game.version}}, but modded",
                RandomArrayList.of("{{images.player}}"),
                "{{player.name}}",
                "https://twitch.tv/twitch",
                new ArrayList<>()
        ));
    }};

    @Override
    public RichPresenceBuilder buildPresence() {
        RichPresenceModel model = this.presence.getNextRandom().orElse(new RichPresenceModel());

        return new RichPresenceBuilder()
                .setType(model.getType())
                .setDetails(model.getDescription())
                .setLargeImage(model.getLargeImageKey().getNextRandom().orElse(""))
                .setLargeImageText(model.getLargeImageText())
                .setSmallImage(model.getSmallImageKey().getNextRandom().orElse(""))
                .setSmallImageText(model.getSmallImageText())
                .setState(model.getState())
                .setButtons(model.getButtons());
    }

    @Override
    public boolean isActive() {
        return this.enabled;
    }
}
