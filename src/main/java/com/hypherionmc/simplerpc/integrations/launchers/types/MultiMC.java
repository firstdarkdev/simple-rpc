package com.hypherionmc.simplerpc.integrations.launchers.types;

import com.hypherionmc.simplerpc.api.utils.APIUtils;
import com.hypherionmc.simplerpc.enums.LauncherType;
import com.hypherionmc.simplerpc.integrations.launchers.Launcher;

import java.io.File;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author HypherionSA
 *
 * MultiMC/Prism Launcher Detector
 */
public final class MultiMC implements Launcher {

    private boolean hasLoaded = false;
    private String packName = "Unknown Pack";
    private String icon = "infinity";
    private LauncherType type = LauncherType.MULTIMC;

    @Override
    public LauncherType getLauncherType() {
        return type;
    }

    @Override
    public void tryLoadLauncher() {
        final File instanceFile = new File(new File(APIUtils.CUR_DIR).getParent(), "instance.cfg");
        final File prismInstance = new File(new File(APIUtils.CUR_DIR).getParentFile().getParentFile().getParentFile(), "prismlauncher.cfg");

        if (instanceFile.exists()) {
            try {
                String packString = readLauncherFile(instanceFile);
                Properties properties = new Properties();
                properties.load(new StringReader(packString));
                packName = properties.getProperty("name", "Unknown Pack");
                icon = properties.getProperty("iconKey", "infinity");

                if (prismInstance.exists() || System.getProperties().containsKey("org.prismlauncher.instance.name"))
                    type = LauncherType.PRISM;

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
        return getLauncherType() == LauncherType.MULTIMC ? "MultiMC" : "Prism Launcher";
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
