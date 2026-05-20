package com.hypherionmc.simplerpc.integrations.launchers.types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypherionmc.simplerpc.api.utils.APIUtils;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.enums.LauncherType;
import com.hypherionmc.simplerpc.integrations.launchers.Launcher;
import com.hypherionmc.simplerpc.util.rpcavatar.RPCImageServer;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author HypherionSA
 *
 * CurseForge/GDLauncher Launcher Detector
 */
public final class CurseForge implements Launcher {

    private boolean hasLoaded = false;
    private String packName = "Unknown Pack";
    private String icon = "curse";
    private LauncherType type = LauncherType.CURSEFORGE;

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
                String additionalIcon = getAdditionalIcon(object);
                hasLoaded = true;

                if (gdDir.getName().contains("gdl")) {
                    type = LauncherType.GDLAUNCHER;
                    icon = "gdlauncher";
                } else {
                    if (additionalIcon != null && additionalIcon.startsWith("http")) {
                        icon = additionalIcon;
                    }

                    if (!SimpleRPCCore.INSTANCE.getClientConfig().general.rpcImageServer) return;

                    if (additionalIcon != null && !additionalIcon.startsWith("http")) {
                        File iconFile = new File(additionalIcon);

                        if (iconFile.exists()) {
                            RPCImageServer.INSTANCE.processLauncherIcon(iconFile);
                            icon = iconFile.getName();
                        }
                    }
                }

            } catch (Exception ignored) {}
        }
    }

    @Nullable
    private String getAdditionalIcon(JsonObject object) {
        if (object.has("profileImagePath")) {
            return object.getAsJsonPrimitive("profileImagePath").getAsString();
        }

        if (object.has("installedModpack")) {
            JsonObject modpack = object.getAsJsonObject("installedModpack");
            if (modpack.has("thumbnailUrl")) {
                return modpack.getAsJsonPrimitive("thumbnailUrl").getAsString();
            }
        }

        return null;
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
        return icon;
    }
}
