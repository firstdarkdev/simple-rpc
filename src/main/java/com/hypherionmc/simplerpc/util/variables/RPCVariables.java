package com.hypherionmc.simplerpc.util.variables;

import com.hypherionmc.craterlib.api.game.client.CraterGame;
import com.hypherionmc.craterlib.api.game.core.CraterBlockPos;
import com.hypherionmc.craterlib.api.game.realmsclient.dto.CraterRealmsServer;
import com.hypherionmc.craterlib.api.game.resources.CraterIdentifier;
import com.hypherionmc.craterlib.api.game.text.Text;
import com.hypherionmc.craterlib.api.game.world.level.CraterGameType;
import com.hypherionmc.craterlib.api.loader.CraterLoader;
import com.hypherionmc.craterlib.core.event.CraterEventBus;
import com.hypherionmc.simplerpc.api.events.RPCEvents;
import com.hypherionmc.simplerpc.api.utils.APIUtils;
import com.hypherionmc.simplerpc.api.utils.MCTimeUtils;
import com.hypherionmc.simplerpc.api.variables.PlaceholderEngine;
import com.hypherionmc.simplerpc.api.variables.validation.NotNullValidator;
import com.hypherionmc.simplerpc.config.objects.CustomVariablesConfig;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.integrations.ReplayModCompat;
import com.hypherionmc.simplerpc.integrations.known.KnownBiomeHelper;
import com.hypherionmc.simplerpc.integrations.known.KnownDimensionHelper;
import com.hypherionmc.simplerpc.integrations.launchers.LauncherDetector;
import com.hypherionmc.simplerpc.util.CompatUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author HypherionSA
 *
 * RPC Placeholders. Values here do not need null checks, because the internal resolver takes care of that
 */
public final class RPCVariables {

    private static final CraterGame minecraft = CraterLoader.getClient();
    public static CraterRealmsServer realmsServer;

    /**
     * Register all valid Placeholders, including custom ones
     */
    public static void register() {
        PlaceholderEngine.INSTANCE.clear();

        // Global
        PlaceholderEngine.INSTANCE.registerPlaceholder("game.version", "1.21", minecraft::getGameVersion);
        PlaceholderEngine.INSTANCE.registerPlaceholder("game.mods", "0", () -> String.valueOf(CraterLoader.getModCount()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.name", "Unknown Player", minecraft::getUserName);
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.uuid", NotNullValidator.of(minecraft::getPlayer), UUID.randomUUID().toString(), () -> minecraft.getPlayerId().toString());

        // Images
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.player", "", () -> String.format("https://skinatar.firstdark.dev/avatar/%s", minecraft.getPlayerId().toString()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.player.head", "", () -> String.format("https://skinatar.firstdark.dev/head/%s", minecraft.getPlayerId().toString()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.realm", () -> realmsServer != null && minecraft.isRealmServer(), "none", () -> realmsServer.getMinigameImage());
        PlaceholderEngine.INSTANCE.registerPlaceholder("images.server", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "none", () -> String.format("https://api.mcsrvstat.us/icon/%s", minecraft.getCurrentServer().ip()));

        // World - These only resolve when the player is in a game
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.name", NotNullValidator.of(minecraft::getLevel), "Unknown World", RPCVariables::resolveWorldName);
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.difficulty", NotNullValidator.of(minecraft::getLevel), "Unknown World", () -> minecraft.getLevel().getDifficulty().asString());
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.savename", NotNullValidator.of(minecraft::getLevel), "World", () -> {
            if (minecraft.getSinglePlayerServer() != null)
                return minecraft.getSinglePlayerServer().getLevelName();

            return "Server World";
        });
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.time.12", NotNullValidator.of(minecraft::getLevel), "12:00 AM", () -> MCTimeUtils.format12(minecraft.getLevel().getDayTime()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.time.24", NotNullValidator.of(minecraft::getLevel), "12:00", () -> MCTimeUtils.format24(minecraft.getLevel().getDayTime()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.time.day", NotNullValidator.of(minecraft::getLevel), "1", () -> String.valueOf(minecraft.getLevel().dayTime() / 24000L));
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.weather", NotNullValidator.of(minecraft::getLevel), "Clear", () -> {
            if (minecraft.getLevel().isRaining())
                return "Raining/Snowing";

            if (minecraft.getLevel().isThundering())
                return "Thunderstorm";

            return "Clear";
        });
        PlaceholderEngine.INSTANCE.registerPlaceholder("world.biome", () -> minecraft.getPlayer() != null && minecraft.getLevel() != null, "Plains", RPCVariables::resolveBiomeName);

        // Player - This will only resolve if the player is in game
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.position", NotNullValidator.of(minecraft::getPlayer), "x: 0, y: 0, z: 0", () -> {
            CraterBlockPos pos = minecraft.getPlayer().getOnPos();
            return String.format("x: %s, y: %s, z: %s", pos.getX(), pos.getY(), pos.getZ());
        });

        PlaceholderEngine.INSTANCE.registerPlaceholder("player.health.current", NotNullValidator.of(minecraft::getPlayer), "0", () -> String.valueOf(minecraft.getPlayer().getHealth()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.health.max", NotNullValidator.of(minecraft::getPlayer), "0", () -> String.valueOf(minecraft.getPlayer().getMaxHealth()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.health.percent", NotNullValidator.of(minecraft::getPlayer), "0%", () -> String.valueOf(Math.round((minecraft.getPlayer().getHealth() / minecraft.getPlayer().getMaxHealth()) * 100)));
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.item.off_hand", NotNullValidator.of(minecraft::getPlayer), "Air", () -> minecraft.getPlayer().getHeldItemOffHand());
        PlaceholderEngine.INSTANCE.registerPlaceholder("player.item.main_hand", NotNullValidator.of(minecraft::getPlayer), "Air", () -> minecraft.getPlayer().getHeldItemMainHand());

        PlaceholderEngine.INSTANCE.registerPlaceholder("player.gamemode", NotNullValidator.of(minecraft::getPlayer), "Survival", () -> {
            CraterGameType type = minecraft.getPlayer().getGameMode();

            if (minecraft.isSinglePlayer() && minecraft.getSinglePlayerServer().isHardcore() && type.isSurvival()) {
                return "Hardcore";
            }

            return StringUtils.capitalize(type.getName());
        });

        // Server - This will only resolve when playing on a server
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.ip", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0.0.0.0", () -> minecraft.getCurrentServer().ip());
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.ip_underscore", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0_0_0_0", () -> minecraft.getCurrentServer().ip().replace(".", "_"));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.name", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "Minecraft Server", () -> minecraft.getCurrentServer().name());
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.motd", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "A Minecraft Server", () -> minecraft.getCurrentServer().motd().asString());
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.players.count", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0", () -> String.valueOf(minecraft.getServerPlayerCount()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.players.countexcl", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0", () -> String.valueOf(minecraft.getServerPlayerCount() - 1));
        PlaceholderEngine.INSTANCE.registerPlaceholder("server.players.max", () -> minecraft.getCurrentServer() != null && !minecraft.isRealmServer(), "0", () -> String.valueOf(minecraft.getCurrentServer().getMaxPlayers()));

        // Realms - This will only resolve on a realm server
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.name", () -> realmsServer != null && minecraft.isRealmServer(), "A Realm", () -> realmsServer.getName());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.description", () -> realmsServer != null && minecraft.isRealmServer(), "A Minecraft Realm", () -> realmsServer.getDescription());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.world", () -> realmsServer != null && minecraft.isRealmServer(), "world", () -> realmsServer.getWorldType().toLowerCase());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.game", () -> realmsServer != null && minecraft.isRealmServer(), "A Realm Game", () -> realmsServer.getMinigameName());
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.players.count", () -> realmsServer != null && minecraft.isRealmServer(), "0", () -> String.valueOf(realmsServer.getPlayerCount()));
        PlaceholderEngine.INSTANCE.registerPlaceholder("realm.players.max", () -> realmsServer != null && minecraft.isRealmServer(), "10", () -> "10");

        // Custom Placeholders
        CustomVariablesConfig customVariablesConfig = SimpleRPCCore.INSTANCE.getClientConfig().variablesConfig;
        if (customVariablesConfig.enabled) {
            customVariablesConfig.variables.forEach(v -> PlaceholderEngine.INSTANCE.registerPlaceholder("custom." + v.name, v.value, () -> PlaceholderEngine.INSTANCE.resolvePlaceholders(v.value)));
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

        CraterEventBus.INSTANCE.postEvent(RPCEvents.RegisterPlaceholders.of());
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
            CraterIdentifier location = minecraft.getLevel().getBiomeIdentifier(minecraft.getPlayer().getOnPos());
            if (KnownBiomeHelper.tryKnownBiomes(location.getPath()).equalsIgnoreCase(location.getPath())) {
                biome.set(Text.getBiomeName(location).asString());
            } else {
                biome.set(KnownBiomeHelper.tryKnownBiomes(location.getPath()));
            }
        }
        return biome.get();
    }

}
