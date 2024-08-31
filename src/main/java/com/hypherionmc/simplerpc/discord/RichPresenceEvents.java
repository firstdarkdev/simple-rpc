package com.hypherionmc.simplerpc.discord;

import com.hypherionmc.simplerpc.config.base.RichPresenceOverrideHolder;
import com.hypherionmc.simplerpc.config.impl.ClientConfig;
import com.hypherionmc.simplerpc.config.impl.ReplayModConfig;
import com.hypherionmc.simplerpc.config.impl.ServerEntriesConfig;
import com.hypherionmc.simplerpc.config.objects.DimensionSection;
import com.hypherionmc.simplerpc.config.objects.ServerEntry;
import com.hypherionmc.simplerpc.enums.GameType;
import com.hypherionmc.simplerpc.enums.RichPresenceState;
import com.hypherionmc.simplerpc.util.APIUtils;
import com.hypherionmc.simplerpc.util.variables.PlaceholderEngine;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * @author HypherionSA
 *
 * Main Events Controller. This takes care of setting the correct RPC at the correct time
 */
public final class RichPresenceEvents {

    // Internal Values
    private final SimpleRPCCore core;
    private RichPresenceState rpcState = RichPresenceState.INIT;
    private GameType gameType = GameType.SINGLE;

    /**
     * Create a new instance of the RPC Event Handler
     *
     * @param core A Copy of {@link SimpleRPCCore}
     */
    @ApiStatus.Internal
    RichPresenceEvents(SimpleRPCCore core) {
        this.core = core;
        setRPCState(RichPresenceState.INIT);
    }

    /**
     * Update the current state of the RPC
     *
     * @param state What {@link dev.firstdark.rpc.enums.RPCState} should be shown
     */
    public void setRPCState(RichPresenceState state) {
        rpcState = state;
        updateRPC();
    }

    /**
     * Update the current RPC state, with additional game type
     *
     * @param state What {@link dev.firstdark.rpc.enums.RPCState} should be shown
     * @param type What kind of {@link GameType} is the player in
     */
    public void setRPCState(RichPresenceState state, GameType type) {
        this.gameType = type;
        setRPCState(state);
    }

    /**
     * RPC Update "tick loop"
     */
    @ApiStatus.Internal
    void updateRPC() {
        ClientConfig clientConfig = core.getClientConfig();
        SimpleRPCCore.DiscordController discordHandler = core.getDiscordController();
        ReplayModConfig replayModConfig = SimpleRPCCore.replayModConfig;

        if (clientConfig == null || !clientConfig.general.enabled || discordHandler == null)
            return;

        // Replay Mod Compat
        if (rpcState == RichPresenceState.REPLAY_BROWSER || rpcState == RichPresenceState.REPLAY_EDITOR || rpcState == RichPresenceState.REPLAY_RENDER) {
            if (replayModConfig != null && replayModConfig.enabled) {
                switch (rpcState) {
                    case REPLAY_BROWSER -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(replayModConfig.replayModMenuSection).get());
                    case REPLAY_EDITOR -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(replayModConfig.replayModEditorSection).get());
                    case REPLAY_RENDER -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(replayModConfig.replayModRenderSection).get());
                }
                return;
            }
        }

        // Standard RPC values
        switch (rpcState) {
            case INIT -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(clientConfig.init).get());
            case MAIN_MENU -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(clientConfig.main_menu).get());
            case REALM_MENU -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(clientConfig.realmsScreenSection).get());
            case SERVER_MENU -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(clientConfig.server_list).get());
            case JOINING_GAME -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(clientConfig.join_game).get());
            case IN_GAME -> {
                switch (gameType) {
                    case SINGLE -> updateSinglePlayerRpc(clientConfig, discordHandler);
                    case MULTIPLAYER -> updateMultiplayerRpc(clientConfig, discordHandler);
                    case REALM -> discordHandler.updateRichPresence(clientConfig.generic.overrideWith(clientConfig.realmsGameSection).get());
                }
            }
        }
    }

    /**
     * Build the Single Player RPC
     * This RPC is overridden by the Biome/Dimension entries
     *
     * @param config The currently loaded {@link ClientConfig}
     * @param discordHandler A copy of the {@link com.hypherionmc.simplerpc.discord.SimpleRPCCore.DiscordController}
     */
    private void updateSinglePlayerRpc(ClientConfig config, SimpleRPCCore.DiscordController discordHandler) {
        RichPresenceOverrideHolder main = config.generic.overrideWith(config.single_player);

        // Override with Dimensions/Biomes
        if (config.dimension_overrides.enabled && !config.dimension_overrides.dimensions.isEmpty()) {
            Optional<DimensionSection.Dimension> dimension = APIUtils.findDimension(config.dimension_overrides.dimensions);

            if (dimension.isPresent()) {
                main = main.overrideWith(dimension.get());
            }
        }

        discordHandler.updateRichPresence(main.get());
    }

    /**
     * Build the Multi Player RPC
     * This RPC is overridden by the Biome/Dimension entries as well as the Server Entries overrides
     *
     * @param config The currently loaded {@link ClientConfig}
     * @param discordHandler A copy of the {@link com.hypherionmc.simplerpc.discord.SimpleRPCCore.DiscordController}
     */
    private void updateMultiplayerRpc(ClientConfig config, SimpleRPCCore.DiscordController discordHandler) {
        RichPresenceOverrideHolder main = config.generic.overrideWith(config.multi_player);

        // Override with Dimensions/Biomes
        if (config.dimension_overrides.enabled && !config.dimension_overrides.dimensions.isEmpty()) {
            Optional<DimensionSection.Dimension> dimension = APIUtils.findDimension(config.dimension_overrides.dimensions);

            if (dimension.isPresent()) {
                main = main.overrideWith(dimension.get());
            }
        }

        // Override with server entries config
        ServerEntriesConfig entriesConfig = core.getServerEntriesConfig();
        if (entriesConfig != null && entriesConfig.enabled && !entriesConfig.serverEntries.isEmpty()) {
            String ip = PlaceholderEngine.INSTANCE.resolvePlaceholders("{{server.ip}}");
            Optional<ServerEntry> entry = entriesConfig.serverEntries.stream().filter(e -> e.ip.equalsIgnoreCase(ip)).findFirst();

            if (entry.isPresent()) {
                main = main.overrideWith(entry.get());
            }
        }

        discordHandler.updateRichPresence(main.get());
    }
}
