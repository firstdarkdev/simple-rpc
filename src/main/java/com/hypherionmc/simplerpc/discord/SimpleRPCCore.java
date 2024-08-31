package com.hypherionmc.simplerpc.discord;

import com.hypherionmc.simplerpc.RPCConstants;
import com.hypherionmc.simplerpc.config.impl.ClientConfig;
import com.hypherionmc.simplerpc.config.impl.ReplayModConfig;
import com.hypherionmc.simplerpc.config.impl.ServerEntriesConfig;
import com.hypherionmc.simplerpc.integrations.launchers.LauncherDetector;
import com.hypherionmc.simplerpc.util.CompatUtils;
import dev.firstdark.rpc.DiscordRpc;
import dev.firstdark.rpc.models.DiscordRichPresence;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author HypherionSA
 *
 * Ah yes, the heart and soul of the mod. Think of it like the CPU that controls everything
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class SimpleRPCCore {

    static final ScheduledExecutorService taskManager = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    // Publicly accessible instance of this class
    public static final SimpleRPCCore INSTANCE = new SimpleRPCCore();

    // Event Handlers
    private DiscordController discordController;

    private RichPresenceEvents events;

    // Variables
    private String langCode;
    private String lastDiscordID = "0";
    private ClientConfig clientConfig;
    private ServerEntriesConfig serverEntriesConfig;

    public static ReplayModConfig replayModConfig;

    /**
     * Create a new instance of this class
     */
    public void init() {
        // Set a fallback lang code
        this.langCode = "en_us";

        // Load the configs
        clientConfig = new ClientConfig(this);
        serverEntriesConfig = new ServerEntriesConfig(this);

        if (clientConfig != null) {
            // Client Config has loaded, so we initialize the entire mod
            discordController = new DiscordController(clientConfig);
            events = new RichPresenceEvents(this);
            lastDiscordID = clientConfig.general.discordid;

            if (clientConfig.general.launcherIntegration) {
                LauncherDetector.INSTANCE.loadLaunchers();
            }

            taskManager.scheduleAtFixedRate(() -> {
                if (events != null) {
                    events.updateRPC();
                }
            }, 1, 2, TimeUnit.SECONDS);
        }

        if (CompatUtils.hasReplay) {
            replayModConfig = new ReplayModConfig(this);
        }
    }

    /**
     * Disconnect from discord and create a new connection
     */
    private void reInit() {
        discordController.shutdownRichPresence();
        discordController = new DiscordController(clientConfig);
        lastDiscordID = clientConfig.general.discordid;
        events.updateRPC();
    }

    /**
     * Update the current game language code
     *
     * @param code The current language identifier in use by the game
     */
    public void setLangCode(String code) {
        if (code != null && !code.equalsIgnoreCase(langCode)) {
            try {
                langCode = code;
                clientConfig = new ClientConfig(this);
                reInit();
            } catch (Exception ignored) { /* Silently Fail */ }
        }
    }

    /**
     * Called when the config is loaded/reloaded
     * THIS IS INTERNAL. LEAVE THIS SHIT ALONE!
     *
     * @param clientConfig The new {@link ClientConfig} to use
     */
    @ApiStatus.Internal
    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        if (discordController != null && !lastDiscordID.equals(clientConfig.general.discordid)) {
            reInit();
        }

        if (discordController != null) {
            this.getEvents().updateRPC();
        }
    }

    /**
     * Called when the server entries config is loaded/reloaded
     * THIS IS INTERNAL. LEAVE THIS SHIT ALONE!
     *
     * @param serverEntriesConfig The new {@link ServerEntriesConfig} to use
     */
    @ApiStatus.Internal
    public void setServerEntriesConfig(ServerEntriesConfig serverEntriesConfig) {
        this.serverEntriesConfig = serverEntriesConfig;
        if (this.discordController != null) {
            this.getEvents().updateRPC();
        }
    }

    /**
     * Called when the ReplayMod config is loaded/reloaded
     * THIS IS INTERNAL. LEAVE THIS SHIT ALONE!
     *
     * @param replayModConfig The new {@link ReplayModConfig} to use
     */
    public void setReplayModConfig(ReplayModConfig replayModConfig) {
        SimpleRPCCore.replayModConfig = replayModConfig;
    }

    /**
     * @author HypherionSA
     *
     * This class controls all the discord related API calls.
     * This is responsible for communicating with the Discord SDK
     */
    static class DiscordController {

        private final DiscordRpc discordRPC;
        private final ClientConfig clientConfig;

        /**
         * Open a connection to discord to allow presence events to be sent
         *
         * @param clientConfig - The Client Config containing all the required info
         */
        DiscordController(ClientConfig clientConfig) {
            this.clientConfig = clientConfig;
            discordRPC = new DiscordRpc();

            try {
                discordRPC.init(clientConfig.general.discordid, new SimpleRpcDiscordEventHandler(), false);
                SimpleRPCCore.taskManager.scheduleAtFixedRate(discordRPC::runCallbacks, 0, 500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                if (clientConfig != null && clientConfig.general.debugging) {
                    RPCConstants.logger.error("Failed to connect to discord", e);
                }
            }
        }

        /**
         * Send a rich presence update
         *
         * @param presence - The finalized presence ready to be sent to discord
         */
        void updateRichPresence(@Nullable DiscordRichPresence presence) {
            try {
                if (discordRPC != null) {
                    this.discordRPC.updatePresence(presence);
                }
            } catch (Exception e) {
                if (clientConfig != null && clientConfig.general.debugging) {
                    RPCConstants.logger.error("Failed to update Rich Presence", e);
                }
            }
        }

        /**
         * Disconnect from discord and stop sending updates
         */
        void shutdownRichPresence() {
            if (discordRPC != null) {
                discordRPC.updatePresence(null);
                discordRPC.shutdown();
            }
        }

    }
}
