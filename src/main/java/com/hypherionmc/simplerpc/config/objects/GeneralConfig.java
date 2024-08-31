package com.hypherionmc.simplerpc.config.objects;

import com.hypherionmc.craterlib.core.config.annotations.HideFromScreen;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;

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
    @SpecComment("Display the Icon and Pack Name in place of LargeImage from compatible launchers. DOES NOT WORK WITH CUSTOM APPS! ONLY THE DEFAULT ONE!")
    public boolean launcherIntegration = false;

    @Path("version")
    @SpecComment("Internal Version Number. NO TOUCHY!")
    @HideFromScreen
    public static int version = 22;

}
