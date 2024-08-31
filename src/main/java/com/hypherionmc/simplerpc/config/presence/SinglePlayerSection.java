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
 * Main RPC that is shown while the player is in a single player game
 */
public final class SinglePlayerSection implements RichPresenceContainer {

    @Path("enabled")
    @SpecComment("Enable/Disable the Single Player Event")
    public boolean enabled = true;

    @Path("description")
    @SpecComment("The first line of text under the app name")
    public String description = "Currently In {{world.name}}";

    @Path("state")
    @SpecComment("The second line of text under the app name")
    public String state = "Playing lonely mode";

    @Path("largeImageKey")
    @SpecComment("The Asset ID of the image to display as the large image")
    @HideFromScreen
    public RandomArrayList<String> largeImageKey = RandomArrayList.of();

    @Path("largeImageText")
    @SpecComment("The text that gets displayed when the large image is hovered")
    public String largeImageText = "";

    @Path("smallImageKey")
    @SpecComment("The Asset ID of the image to display as the small image")
    @HideFromScreen
    public RandomArrayList<String> smallImageKey = RandomArrayList.of();

    @Path("smallImageText")
    @SpecComment("The text that gets displayed when the small image is hovered")
    public String smallImageText = "";

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
