package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.simplerpc.api.rpc.ButtonWrapper;
import dev.firstdark.rpc.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HypherionSA
 * A data class that handles how RPCs are definied in lists
 */
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RichPresenceModel {

    @Path("type")
    @SpecComment("The Activity type to use for this RPC. Valid types are: PLAYING, STREAMING, LISTENING, WATCHING, CUSTOM, COMPETING")
    private ActivityType type = ActivityType.PLAYING;

    @Path("description")
    @SpecComment("The first line of text under the app name")
    private String description = "";

    @Path("state")
    @SpecComment("The second line of text under the app name")
    private String state = "";

    @Path("largeImageKey")
    @SpecComment("The Asset ID or URLs of images to randomly use for the large image")
    private RandomArrayList<String> largeImageKey = new RandomArrayList<>();

    @Path("largeImageText")
    @SpecComment("The text to show when someone hovers over the largeImage")
    private String largeImageText = "";

    @Path("smallImageKey")
    @SpecComment("The Asset ID or URLs of images to randomly use for the small image")
    private RandomArrayList<String> smallImageKey = new RandomArrayList<>();

    @Path("smallImageText")
    @SpecComment("The text to show when someone hovers over the smallImage")
    private String smallImageText = "";

    @Path("streamingActivityUrl")
    @SpecComment("The Twitch or Youtube URL to use when type is set to STREAMING")
    private String streamingActivityUrl = "https://twitch.tv/twitch";

    @Path("buttons")
    @SpecComment("List of buttons (max 2) to show on the RPC")
    private List<ButtonWrapper> buttons = new ArrayList<>();
}
