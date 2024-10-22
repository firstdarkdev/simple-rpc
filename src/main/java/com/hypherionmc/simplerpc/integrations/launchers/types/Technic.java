package com.hypherionmc.simplerpc.integrations.launchers.types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypherionmc.simplerpc.api.utils.APIUtils;
import com.hypherionmc.simplerpc.enums.LauncherType;
import com.hypherionmc.simplerpc.integrations.launchers.Launcher;

import java.io.File;

/**
 * @author HypherionSA
 *
 * Technic Launcher Detector
 */
public final class Technic implements Launcher {

    private boolean hasLoaded = false;
    private String packName = "Unknown Pack";

    @Override
    public LauncherType getLauncherType() {
        return LauncherType.TECHNIC;
    }

    @Override
    public void tryLoadLauncher() {
        final File pack = new File(new File(APIUtils.CUR_DIR).getParentFile().getParentFile() + File.separator + "installedPacks");

        if (pack.exists()) {
            String packString = readLauncherFile(pack);
            try {
                JsonObject object = new Gson().fromJson(packString, JsonObject.class);
                packName = object.getAsJsonPrimitive("selected").getAsString();
                hasLoaded = true;
            } catch (Exception ignored) {}
        }

    }

    @Override
    public boolean hasLoaded() {
        return hasLoaded;
    }

    @Override
    public String getLauncherName() {
        return "Technic";
    }

    @Override
    public String getPackName() {
        return packName;
    }

    @Override
    public String getPackIcon() {
        return "technic";
    }

}
