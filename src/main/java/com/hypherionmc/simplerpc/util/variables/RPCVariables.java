package com.hypherionmc.simplerpc.util.variables;

import com.hypherionmc.craterlib.core.platform.ModloaderEnvironment;
import com.hypherionmc.craterlib.nojang.client.BridgedMinecraft;
import com.hypherionmc.craterlib.nojang.core.BridgedBlockPos;
import com.hypherionmc.craterlib.nojang.realmsclient.dto.BridgedRealmsServer;
import com.hypherionmc.craterlib.nojang.resources.ResourceIdentifier;
import com.hypherionmc.craterlib.utils.ChatUtils;
import com.hypherionmc.simplerpc.config.objects.CustomVariablesConfig;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.integrations.ReplayModCompat;
import com.hypherionmc.simplerpc.integrations.known.KnownBiomeHelper;
import com.hypherionmc.simplerpc.integrations.known.KnownDimensionHelper;
import com.hypherionmc.simplerpc.integrations.launchers.LauncherDetector;
import com.hypherionmc.simplerpc.util.APIUtils;
import com.hypherionmc.simplerpc.util.CompatUtils;
import com.hypherionmc.simplerpc.util.MCTimeUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author HypherionSA
 *
 * RPC Placeholders. Values here do not need null checks, because the internal resolver takes care of that
 */
public class RPCVariables {

    private static final BridgedMinecraft minecraft = BridgedMinecraft.getInstance();
    public static BridgedRealmsServer realmsServer;

    /**
     * Register all valid Placeholders, including custom ones
     */
    public static void register() {
        PlaceholderEngine.INSTANCE.clear();

        // Global
        PlaceholderEngine.INSTANCE.registerPlaceholder("game.version", "1.21", minecraft::getGameVersion);
        PlaceholderEngine.INSTANCE.registerPlaceholder("game.mods", "0", () -> String.valueOf(ModloaderEnvironment.INSTANCE.getModCount()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.name", "Unknown Player", minecraft::getUserName);

        // Images
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.player", "", () -> String.format("https://mc-heads.net/avatar/%s/512", minecraft.getPlayerId().toString()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.realm", () -> realmsServer != null && minecraft.isRealmServer(), "none", () -> realmsServer.getMinigameImage());
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.server", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "none", () -> String.format("https://api.mcsrvstat.us/icon/%s", minecraft.getCurrentServer().ip()));

        // World - These only resolve when the player is in a game
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.name", () -> minecraft.getLevel() != null, "Unknown World", RPCVariables::resolveWorldName);
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.difficulty", () -> minecraft.getLevel() != null, "Unknown World", () -> ChatUtils.resolve(minecraft.getLevel().getDifficulty(), false));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.savename", () -> minecraft.getLevel() != null, "World", () -> {
            if (minecraft.getSinglePlayerServer() != null)
                return minecraft.getSinglePlayerServer().getLevelName();

            return "Server World";
        });
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.time.12", () -> minecraft.getLevel() != null, "12:00 AM", () -> MCTimeUtils.format12(minecraft.getLevel().getDayTime()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.time.24", () -> minecraft.getLevel() != null, "12:00", () -> MCTimeUtils.format24(minecraft.getLevel().getDayTime()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.time.day", () -> minecraft.getLevel() != null, "1", () -> String.valueOf(minecraft.getLevel().dayTime() / 24000L));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.weather", () -> minecraft.getLevel() != null, "Clear", () -> {
            if (minecraft.getLevel().isRaining())
                return "Raining/Snowing";

            if (minecraft.getLevel().isThundering())
                return "Thunderstorm";

            return "Clear";
        });
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.biome", () -> minecraft.getPlayer() != null && minecraft.getLevel() != null, "Plains", RPCVariables::resolveBiomeName);

        // Player - This will only resolve if the player is in game
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.position", () -> minecraft.getPlayer() != null, "x: 0, y: 0, z: 0", () -> {
            BridgedBlockPos pos = minecraft.getPlayer().getOnPos();
            return String.format("x: %s, y: %s, z: %s", pos.getX(), pos.getY(), pos.getZ());
        });

        // Server - This will only resolve when playing on a server
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.ip", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0.0.0.0", () -> minecraft.getCurrentServer().ip());
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.ip_underscore", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0_0_0_0", () -> minecraft.getCurrentServer().ip().replace(".", "_"));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.name", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "Minecraft Server", () -> minecraft.getCurrentServer().name());
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.motd", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "A Minecraft Server", () -> ChatUtils.resolve(minecraft.getCurrentServer().motd(), false));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.players.count", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0", () -> String.valueOf(minecraft.getServerPlayerCount()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.players.countexcl", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0", () -> String.valueOf(minecraft.getServerPlayerCount() - 1));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.players.max", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0", () -> String.valueOf(minecraft.getCurrentServer().getMaxPlayers()));

        // Realms - This will only resolve on a realm server
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.name", () -> realmsServer != null && minecraft.isRealmServer(), "A Realm", () -> realmsServer.getName());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.description", () -> realmsServer != null && minecraft.isRealmServer(), "A Minecraft Realm", () -> realmsServer.getDescription());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.world", () -> realmsServer != null && minecraft.isRealmServer(), "World", () -> realmsServer.getWorldType().toLowerCase());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.game", () -> realmsServer != null && minecraft.isRealmServer(), "A Realm Game", () -> realmsServer.getMinigameName());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.players.count", () -> realmsServer != null && minecraft.isRealmServer(), "0", () -> String.valueOf(realmsServer.getPlayerCount()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.players.max", () -> realmsServer != null && minecraft.isRealmServer(), "10", () -> "10");

        // Custom Placeholders
        CustomVariablesConfig customVariablesConfig = SimpleRPCCore.INSTANCE.getClientConfig().variablesConfig;
        if (customVariablesConfig.enabled) {
            customVariablesConfig.variables.forEach(v -> {
                PlaceholderEngine.INSTANCE.registerPlaceholder("custom." + v.name, v.value, () -> v.value);
            });
        }

        // Replay Mod Variables - Only available if replay mod is installed
        PlaceholderEngine.INSTANCE.registerPlaceholder("replaymod.time.elapsed", CompatUtils::checkReplayMod, "0 Seconds", ReplayModCompat.renderTimeTaken::get);
        PlaceholderEngine.INSTANCE.registerPlaceholder("replaymod.time.left", CompatUtils::checkReplayMod, "0 Seconds", ReplayModCompat.renderTimeLeft::get);
        PlaceholderEngine.INSTANCE.registerPlaceholder("replaymod.frames.current", CompatUtils::checkReplayMod, "0", () -> String.valueOf(ReplayModCompat.renderFramesDone.get()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("replaymod.frames.total", CompatUtils::checkReplayMod, "0", () -> String.valueOf(ReplayModCompat.renderFramesTotal.get()));

        // Launcher integration
        PlaceholderEngine.INSTANCE.registerPlaceholder("launcher.name", "Unknown Launcher", LauncherDetector.INSTANCE::getLauncherName);
        PlaceholderEngine.INSTANCE.registerPlaceholder("launcher.pack", "Unknown Pack", LauncherDetector.INSTANCE::getLauncherPackName);
        PlaceholderEngine.INSTANCE.registerPlaceholder("launcher.icon", "unknown", LauncherDetector.INSTANCE::getLauncherIcon);
    }

    /**
     * Helper method to resolve the display name of a dimension based on the registry key
     *
     * @return The resolved name or "Unknown world"
     */
    private static String resolveWorldName() {
        if (minecraft.getLevel().getDimensionKey() != null) {
            String worldResKey = minecraft.getLevel().getDimensionKey().getPath();
            return KnownDimensionHelper.tryKnownDimensions(worldResKey).equalsIgnoreCase(worldResKey) ? APIUtils.worldNameToReadable(worldResKey) : KnownDimensionHelper.tryKnownDimensions(worldResKey);
        }
        return "Unknown World";
    }

    /**
     * Helper method to resolve the display name of a biome based on the registry key
     *
     * @return The resolved name or "Unknown Biome"
     */
    private static String resolveBiomeName() {
        AtomicReference<String> biome = new AtomicReference<>("Unknown Biome");
        if (minecraft.getLevel() != null && minecraft.getLevel().getBiomeIdentifier(minecraft.getPlayer().getOnPos()) != null) {
            ResourceIdentifier location = minecraft.getLevel().getBiomeIdentifier(minecraft.getPlayer().getOnPos());
            if (KnownBiomeHelper.tryKnownBiomes(location.getPath()).equalsIgnoreCase(location.getPath())) {
                biome.set(ChatUtils.resolve(ChatUtils.getBiomeName(location), false));
            } else {
                biome.set(KnownBiomeHelper.tryKnownBiomes(location.getPath()));
            }
        }
        return biome.get();
    }

}
