package com.hypherionmc.simplerpc.util;

/**
 * @author HypherionSA
 *
 * Compat utils to check if certain mods are present. Mostly just used for ReplayMod
 */
public final class CompatUtils {

    public static final boolean hasReplay = checkReplayMod();

    public static boolean checkReplayMod() {
        try {
            Class.forName("com.replaymod.replay.Setting");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
       return false;
    }

}
