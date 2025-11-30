package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.simplerpc.api.rpc.RichPresenceBuilder;
import com.hypherionmc.simplerpc.api.rpc.RichPresenceContainer;
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
    public List<Dimension> dimensions = new ArrayList<>();

    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Dimension implements RichPresenceContainer {

        @Path("name")
        @SpecComment("The name of the Dimension/Biome to override. FORMAT: modid:dimension or modid:biome")
        public String name = "";

        @Path("presence")
        @SpecComment("List of RPCs that will be displayed at random")
        public RandomArrayList<RichPresenceModel> presence = new RandomArrayList<>();

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
            return true;
        }
    }
}
