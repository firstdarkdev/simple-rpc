package com.hypherionmc.simplerpc.config.presence;

import com.hypherionmc.craterlib.core.config.annotations.HideFromScreen;
import com.hypherionmc.simplerpc.config.base.RichPresenceContainer;
import com.hypherionmc.simplerpc.discord.ButtonWrapper;
import com.hypherionmc.simplerpc.discord.RichPresenceBuilder;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;

import java.util.LinkedList;
import java.util.List;

/**
 * @author HypherionSA
 *
 * Main RPC that is shown while the player is browing recorded videos
 */
public final class ReplayModMenuSection implements RichPresenceContainer {

    @Path("enabled")
    @SpecComment("Enable/Disable the ReplayMod Replay Viewer Event")
    public boolean enabled = true;

    @Path("description")
    @SpecComment("The first line of text under the app name")
    public String description = "Browsing Replays";

    @Path("state")
    @SpecComment("The second line of text under the app name")
    public String state = "{{player.name}} is browsing the library";

    @Path("largeImageKey")
    @SpecComment("The Asset ID of the image to display as the large image")
    public RandomArrayList<String> largeImageKey = RandomArrayList.of("mclogonew");

    @Path("largeImageText")
    @SpecComment("The text that gets displayed when the large image is hovered")
    public String largeImageText = "It's Minecraft {{game.version}}, but modded";

    @Path("smallImageKey")
    @SpecComment("The Asset ID of the image to display as the small image")
    public RandomArrayList<String> smallImageKey = RandomArrayList.of("mclogo");

    @Path("smallImageText")
    @SpecComment("The text that gets displayed when the small image is hovered")
    public String smallImageText = "{{game.mods}} mods installed";

    @Path("buttons")
    @SpecComment("The buttons to display on Discord")
    @HideFromScreen
    public List<ButtonWrapper> buttonsList = new LinkedList<>();

    @Override
    public RichPresenceBuilder buildPresence() {
        return new RichPresenceBuilder()
                .setDetails(this.description)
                .setLargeImage(this.largeImageKey.getNextRandom().orElse(""))
                .setLargeImageText(this.largeImageText)
                .setSmallImage(this.smallImageKey.getNextRandom().orElse(""))
                .setSmallImageText(this.smallImageText)
                .setState(this.state)
                .setButtons(this.buttonsList);
    }

    @Override
    public boolean isActive() {
        return this.enabled;
    }
}
