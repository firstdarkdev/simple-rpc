package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.craterlib.core.config.annotations.HideFromScreen;
import com.hypherionmc.simplerpc.config.base.RichPresenceContainer;
import com.hypherionmc.simplerpc.discord.ButtonWrapper;
import com.hypherionmc.simplerpc.discord.RichPresenceBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HypherionSA
 *
 * Main Config Structure that allows the user to override their single/multiplayer RPC based on the
 * biome/dimension they are in
 */
public final class DimensionSection implements Serializable {

    @Path("enabled")
    @SpecComment("Allows you to override the displayed values for dimensions")
    public boolean enabled = false;

    @Path("dimensions")
    @SpecComment("The Dimensions to override")
    @HideFromScreen
    public List<Dimension> dimensions = new ArrayList<>();

    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Dimension implements RichPresenceContainer {

        @Path("name")
        @SpecComment("The name of the Dimension/Biome to override. FORMAT: modid:dimension or modid:biome")
        public String name = "";

        @Path("description")
        @SpecComment("The first line of text under the app name")
        public String description = "";

        @Path("state")
        @SpecComment("The second line of text under the app name")
        public String state = "";

        @Path("largeImageKey")
        @SpecComment("The Asset ID of the image to display as the large image")
        public RandomArrayList<String> largeImageKey = RandomArrayList.of();

        @Path("largeImageText")
        @SpecComment("The text that gets displayed when the large image is hovered")
        public String largeImageText = "";

        @Path("smallImageKey")
        @SpecComment("The Asset ID of the image to display as the small image")
        public RandomArrayList<String> smallImageKey = RandomArrayList.of();


        @Path("smallImageText")
        @SpecComment("The text that gets displayed when the small image is hovered")
        public String smallImageText = "";

        @Path("buttons")
        @SpecComment("The buttons to display on Discord")
        public List<ButtonWrapper> buttonsList = new ArrayList<>();

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
            return true;
        }
    }
}
