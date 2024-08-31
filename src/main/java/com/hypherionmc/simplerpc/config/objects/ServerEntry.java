package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.simplerpc.config.base.RichPresenceContainer;
import com.hypherionmc.simplerpc.discord.RichPresenceBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

    @SpecComment("Text to display instead of the default Description. Leave Empty to ignore")
    public String description = "";

    @SpecComment("Text to display instead of the default State. Leave Empty to ignore")
    public String state = "";

    @SpecComment("Direct URL or Image Key to display instead of the default Large Image. Leave Empty to ignore")
    public RandomArrayList<String> largeImageKey = RandomArrayList.of();

    @SpecComment("Text to display instead of the default Large Image Text. Leave Empty to ignore")
    public String largeImageText = "";

    @SpecComment("Direct URL or Image Key to display instead of the default Small Image. Leave Empty to ignore")
    public RandomArrayList<String> smallImageKey = RandomArrayList.of();

    @SpecComment("Text to display instead of the default Small Image Text. Leave Empty to ignore")
    public String smallImageText = "";

    @Override
    public RichPresenceBuilder buildPresence() {
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
