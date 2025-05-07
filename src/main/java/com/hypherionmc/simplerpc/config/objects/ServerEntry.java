package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.simplerpc.api.rpc.RichPresenceBuilder;
import com.hypherionmc.simplerpc.api.rpc.RichPresenceContainer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;

/**
 * @author HypherionSA
 *
 * Main Config Structure representing a Server Entry
 */
@NoArgsConstructor
@AllArgsConstructor
public final class ServerEntry implements RichPresenceContainer {

    @SpecComment("The IP address of the server as used to connect to the server")
    public String ip = "";

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
