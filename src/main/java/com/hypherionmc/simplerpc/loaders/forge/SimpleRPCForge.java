package com.hypherionmc.simplerpc.loaders.forge;

import com.hypherionmc.craterlib.core.platform.ModloaderEnvironment;
import com.hypherionmc.simplerpc.RPCConstants;
import com.hypherionmc.simplerpc.SimpleRPCClient;
import net.minecraftforge.fml.common.Mod;

@Mod(RPCConstants.MOD_ID)
public class SimpleRPCForge {

    public SimpleRPCForge() {
        if (ModloaderEnvironment.INSTANCE.getEnvironment().isClient()) {
            SimpleRPCClient.setupEvents();
        }
    }
}
