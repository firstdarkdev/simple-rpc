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
 * CurseForge/GDLauncher Launcher Detector
 */
public final class CurseForge implements Launcher {

    private boolean hasLoaded = false;
    private String packName = "Unknown Pack";
    private LauncherType type = LauncherType.GDLAUNCHER;

    @Override
    public LauncherType getLauncherType() {
        return type;
    }

    @Override
    public void tryLoadLauncher() {
        final File pack = new File(new File(APIUtils.CUR_DIR), "manifest.json");
        final File alternative = new File(new File(APIUtils.CUR_DIR), "minecraftinstance.json");
        File gdDir = new File(new File(APIUtils.CUR_DIR).getParentFile().getParent());

        if (pack.exists() || alternative.exists()) {
            try {
                String packString = readLauncherFile(pack.exists() ? pack : alternative);
                JsonObject object = new Gson().fromJson(packString, JsonObject.class);
                packName = object.getAsJsonPrimitive("name").getAsString();
                hasLoaded = true;

                if (gdDir.getName().contains("gdl")) {
                    type = LauncherType.GDLAUNCHER;
                }

            } catch (Exception ignored) {}
        }

    }

    @Override
    public boolean hasLoaded() {
        return hasLoaded;
    }

    @Override
    public String getLauncherName() {
        return type == LauncherType.CURSEFORGE ? "CurseForge" : "GDLauncher";
    }

    @Override
    public String getPackName() {
        return packName;
    }

    @Override
    public String getPackIcon() {
        return type == LauncherType.CURSEFORGE ? "curseforge" : "gdlauncher";
    }
}
