package com.hypherionmc.simplerpc.integrations.launchers;

import com.hypherionmc.simplerpc.enums.LauncherType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author HypherionSA
 *
 * Generic Launcher Detector
 */
public interface Launcher {

    LauncherType getLauncherType();
    void tryLoadLauncher();
    boolean hasLoaded();

    String getLauncherName();
    String getPackName();
    String getPackIcon();

    default String readLauncherFile(File file) {
        try {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (Exception ignored) {}

        return "";
    }
}
