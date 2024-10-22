package com.hypherionmc.simplerpc.integrations.launchers.types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypherionmc.simplerpc.enums.LauncherType;
import com.hypherionmc.simplerpc.integrations.launchers.Launcher;

import java.io.File;

/**
 * @author HypherionSA
 *
 * ATLauncher Launcher Detector
 */
public final class ATLauncher implements Launcher {

    private boolean hasLoaded = false;
    private String packName = "Unknown Pack";

    @Override
    public LauncherType getLauncherType() {
        return LauncherType.ATLAUNCHER;
    }

    @Override
    public void tryLoadLauncher() {
        final File pack = new File("instance.json");

        if (pack.exists()) {
            String packString = readLauncherFile(pack);
            try {
                JsonObject object = new Gson().fromJson(packString, JsonObject.class).getAsJsonObject("launcher");
                packName = object.getAsJsonPrimitive("name").getAsString();
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
        return "ATLauncher";
    }

    @Override
    public String getPackName() {
        return packName;
    }

    @Override
    public String getPackIcon() {
        return "atlauncher";
    }
}
