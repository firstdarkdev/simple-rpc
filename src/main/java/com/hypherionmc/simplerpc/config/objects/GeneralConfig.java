package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.craterlib.core.config.annotations.HideFromScreen;
import com.hypherionmc.craterlib.libs.moonconfig.core.conversion.Path;
import com.hypherionmc.craterlib.libs.moonconfig.core.conversion.SpecComment;
import com.hypherionmc.simplerpc.config.impl.ClientConfig;

/**
 * @author HypherionSA
 *
 * General Mod Settings Config Structure
 */
public final class GeneralConfig {

    @Path("applicationID")
    @SpecComment("The Application ID of the Discord App to use")
    public String discordid = "762726289341677668";

    @Path("enabled")
    @SpecComment("Enable/Disable the mod")
    public boolean enabled = true;

    @Path("debugging")
    @SpecComment("Enable/Disable debugging mode. WARNING: MAY CAUSE LOG SPAM!")
    public boolean debugging = false;

    @Path("launcherIntegration")
    @SpecComment("Enable the detection of certain compatible launchers, allowing you to use their name and icon in your config")
    public boolean launcherIntegration = false;

    @Path("rpcImageServer")
    @SpecComment("Allow using local image files, as RPC icons")
    public boolean rpcImageServer = false;

    @Path("rpcImageServerUrl")
    @SpecComment("The URL of the RPC image server that will be used for local images. Restart required when changed")
    public String rpcImageServerUrl = "https://rpcavatar.firstdark.dev";

    @Path("version")
    @SpecComment("Internal Version Number. NO TOUCHY!")
    @HideFromScreen
    public int version = ClientConfig.version;

}
