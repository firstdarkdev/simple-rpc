package com.hypherionmc.simplerpc.integrations;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author HypherionSA
 *
 * Helper class to help the ReplayMod placeholders resolve
 */
public final class ReplayModCompat {

    public static final AtomicReference<String> renderTimeTaken = new AtomicReference<>("");
    public static final AtomicReference<String> renderTimeLeft = new AtomicReference<>("");
    public static final AtomicInteger renderFramesDone = new AtomicInteger(0);
    public static final AtomicInteger renderFramesTotal = new AtomicInteger(0);

}
