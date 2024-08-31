package com.hypherionmc.simplerpc.config.impl;

import com.hypherionmc.craterlib.core.config.annotations.SubConfig;
import com.hypherionmc.simplerpc.config.base.BaseRPCConfig;
import com.hypherionmc.simplerpc.config.objects.CustomVariablesConfig;
import com.hypherionmc.simplerpc.config.objects.DimensionSection;
import com.hypherionmc.simplerpc.config.objects.GeneralConfig;
import com.hypherionmc.simplerpc.config.presence.*;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.util.variables.RPCVariables;
import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;

import java.util.LinkedList;

/**
 * @author HypherionSA
 *
 * Main Client Side Config
 */
public final class ClientConfig extends BaseRPCConfig<ClientConfig> {

    // Keep this transient, or the config library will write this to the config file!!!
    private transient final SimpleRPCCore core;

    @Path("general")
    @SpecComment("General Config Section. See https://srpc.fdd-docs.com/introduction/")
    @SubConfig
    public GeneralConfig general = new GeneralConfig();

    @Path("init")
    @SpecComment("The Game Loading event")
    @SubConfig
    public InitSection init = new InitSection();

    @Path("main_menu")
    @SpecComment("The Main Menu event")
    @SubConfig
    public MainMenuSection main_menu = new MainMenuSection();

    @Path("server_list")
    @SpecComment("The Server List event")
    @SubConfig
    public ServerListSection server_list = new ServerListSection();

    @Path("realms_list")
    @SpecComment("The Realms Screen event")
    @SubConfig
    public RealmsScreenSection realmsScreenSection = new RealmsScreenSection();

    @Path("join_game")
    @SpecComment("The Join Game Event")
    @SubConfig
    public JoinGameSection join_game = new JoinGameSection();

    @Path("single_player")
    @SpecComment("The Single Player Event")
    @SubConfig
    public SinglePlayerSection single_player = new SinglePlayerSection();

    @Path("multi_player")
    @SpecComment("The Multi Player Event")
    @SubConfig
    public MultiPlayerSection multi_player = new MultiPlayerSection();

    @Path("realms")
    @SpecComment("The Realms Game Event")
    @SubConfig
    public RealmsGameSection realmsGameSection = new RealmsGameSection();

    @Path("generic")
    @SpecComment("Fallback event for disabled events")
    @SubConfig
    public GenericSection generic = new GenericSection();

    @Path("custom")
    @SpecComment("Custom Config Variables that you can use")
    @SubConfig
    public CustomVariablesConfig variablesConfig = new CustomVariablesConfig();

    @Path("dimension_overrides")
    @SpecComment("Dimension Information Overrides")
    @SubConfig
    public DimensionSection dimension_overrides = new DimensionSection();

    /**
     * Create a new copy of the RPC client config
     *
     * @param core A link to {@link SimpleRPCCore}
     */
    public ClientConfig(SimpleRPCCore core) {
        super("simple-rpc", core.getLangCode());
        this.core = core;
        registerAndSetup(this);
    }

    @Override
    public void configReloaded() {
        core.setClientConfig(this.readConfig(this));
        RPCVariables.register();
    }

    @Override
    public void appendAdditional() {
        this.dimension_overrides.dimensions.add(new DimensionSection.Dimension("overworld", "{{player.name}} is in The Overworld", "", RandomArrayList.of("overworld"), "In the Overworld", RandomArrayList.of("mclogo"), "{{game.mods}} mods installed", new LinkedList<>()));
        this.dimension_overrides.dimensions.add(new DimensionSection.Dimension("the_nether", "{{player.name}} is in The Nether", "", RandomArrayList.of("nether"), "In the Nether", RandomArrayList.of("mclogo"), "{{game.mods}} mods installed", new LinkedList<>()));
        this.dimension_overrides.dimensions.add(new DimensionSection.Dimension("the_end", "{{player.name}} is in The End", "", RandomArrayList.of("end"), "In the End", RandomArrayList.of("mclogo"), "{{game.mods}} mods installed", new LinkedList<>()));
    }

    @Override
    public int getConfigVersion() {
        return GeneralConfig.version;
    }
}
