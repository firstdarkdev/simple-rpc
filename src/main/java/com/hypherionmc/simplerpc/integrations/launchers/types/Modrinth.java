package com.hypherionmc.simplerpc.integrations.launchers.types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypherionmc.simplerpc.enums.LauncherType;
import com.hypherionmc.simplerpc.integrations.launchers.Launcher;

import java.io.File;

/**
 * @author HypherionSA
 *
 * Modrinth Launcher Detector
 */
public final class Modrinth implements Launcher {

    private boolean hasLoaded = false;
    private String packName = "Unknown Pack";

    @Override
    public LauncherType getLauncherType() {
        return LauncherType.MODRINTH;
    }

    @Override
    public void tryLoadLauncher() {
        final File pack = new File("profile.json");

        if (pack.exists()) {
            String packString = readLauncherFile(pack);
            try {
                JsonObject object = new Gson().fromJson(packString, JsonObject.class).getAsJsonObject("metadata");
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
        return "Modrinth";
    }

    @Override
    public String getPackName() {
        return packName;
    }

    @Override
    public String getPackIcon() {
        return "modrinth";
    }

}
