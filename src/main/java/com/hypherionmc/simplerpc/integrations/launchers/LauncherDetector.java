package com.hypherionmc.simplerpc.integrations.launchers;

import com.hypherionmc.simplerpc.RPCConstants;
import com.hypherionmc.simplerpc.enums.LauncherType;
import com.hypherionmc.simplerpc.integrations.launchers.types.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @author HypherionSA
 *
 * Launcher detector to detect the launcher the user is using
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class LauncherDetector {

    public static final LauncherDetector INSTANCE = new LauncherDetector();

    // Accessible
    private LauncherType launcherType = LauncherType.UNKNOWN;
    private String launcherName = "Unknown Launcher";
    private String launcherPackName = "Unknown Pack";
    private String launcherIcon = "unknown";

    private final ArrayList<Launcher> supportedLaunchers = new ArrayList<>() {{
        add(new ATLauncher());
        add(new CurseForge());
        add(new Modrinth());
        add(new MultiMC());
        add(new Technic());
    }};

    public void loadLaunchers() {
        for (Launcher l : supportedLaunchers) {
            l.tryLoadLauncher();

            if (l.hasLoaded()) {
                launcherType = l.getLauncherType();
                launcherName = l.getLauncherName();
                launcherIcon = l.getPackIcon();
                launcherPackName = l.getPackName();
                RPCConstants.logger.info("Found {} pack", l.getLauncherName());
                break;
            }
        }
    }

}
