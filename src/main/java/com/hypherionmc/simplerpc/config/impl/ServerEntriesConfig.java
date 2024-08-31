package com.hypherionmc.simplerpc.config.impl;

import com.hypherionmc.craterlib.core.config.annotations.NoConfigScreen;
import com.hypherionmc.simplerpc.config.base.BaseRPCConfig;
import com.hypherionmc.simplerpc.config.objects.ServerEntry;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HypherionSA
 *
 * Main Config allowing users to override the Multiplayer RPC based on the server they
 * are connected to
 */
@NoConfigScreen
public final class ServerEntriesConfig extends BaseRPCConfig<ServerEntriesConfig> {

    private transient final SimpleRPCCore core;

    @Path("enabled")
    @SpecComment("Enable/Disable Server Entries overrides")
    public boolean enabled = false;

    @Path("version")
    @SpecComment("Internal Version Number. NO TOUCHY!")
    public static int version = 2;

    @Path("entry")
    @SpecComment("Server override entries")
    public List<ServerEntry> serverEntries = new ArrayList<>();

    public ServerEntriesConfig(SimpleRPCCore core) {
        super("server-entries", core.getLangCode());
        this.core = core;
        registerAndSetup(this);
    }

    @Override
    public void configReloaded() {
        core.setServerEntriesConfig(this.readConfig(this));
    }

    @Override
    public int getConfigVersion() {
        return version;
    }
}
