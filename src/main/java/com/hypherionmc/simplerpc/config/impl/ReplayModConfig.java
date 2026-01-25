package com.hypherionmc.simplerpc.config.impl;

import com.hypherionmc.simplerpc.config.base.BaseRPCConfig;
import com.hypherionmc.simplerpc.config.presence.ReplayModEditorSection;
import com.hypherionmc.simplerpc.config.presence.ReplayModMenuSection;
import com.hypherionmc.simplerpc.config.presence.ReplayModRenderSection;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.craterlib.libs.moonconfig.core.conversion.Path;
import com.hypherionmc.craterlib.libs.moonconfig.core.conversion.SpecComment;

/**
 * @author HypherionSA
 *
 * Main Config for the Replay Mod integration
 */
public final class ReplayModConfig extends BaseRPCConfig<ReplayModConfig> {

    private transient final SimpleRPCCore core;

    @Path("general.enabled")
    @SpecComment("Enable/Disable ReplayMod Support")
    public boolean enabled = true;

    @Path("general.version")
    @SpecComment("Internal Version Number. NO TOUCHY!")
    public static int version = 2;

    @Path("replay_viewer")
    @SpecComment("The Replay Browser Event")
    public ReplayModMenuSection replayModMenuSection = new ReplayModMenuSection();

    @Path("replay_editor")
    @SpecComment("The Replay Editor Event")
    public ReplayModEditorSection replayModEditorSection = new ReplayModEditorSection();

    @Path("replay_render")
    @SpecComment("The Replay Rendering Event")
    public ReplayModRenderSection replayModRenderSection = new ReplayModRenderSection();

    public ReplayModConfig(SimpleRPCCore core) {
        super("simple-rpc-replaymod", "");
        this.core = core;
        registerAndSetup(this);
    }

    @Override
    public void configReloaded() {
        core.setReplayModConfig(this.readConfig(this));
    }

    @Override
    public int getConfigVersion() {
        return version;
    }

}
