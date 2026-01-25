package com.hypherionmc.simplerpc;

import com.hypherionmc.craterlib.api.loader.plugins.entrypoints.CraterClientPlugin;

public class SimpleRPC implements CraterClientPlugin {

    @Override
    public void onLoadClient() {
        SimpleRPCClient.setupEvents();
    }

    @Override
    public String getPluginId() {
        return "simplercp";
    }
}
