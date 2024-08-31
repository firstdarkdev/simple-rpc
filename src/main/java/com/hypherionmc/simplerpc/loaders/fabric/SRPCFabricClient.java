package com.hypherionmc.simplerpc.loaders.fabric;

import com.hypherionmc.simplerpc.SimpleRPCClient;
import net.fabricmc.api.ClientModInitializer;

public class SRPCFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SimpleRPCClient.setupEvents();
    }
}
