package com.hypherionmc.simplerpc;

import com.hypherionmc.craterlib.api.events.client.*;
import com.hypherionmc.craterlib.api.game.text.Text;
import com.hypherionmc.craterlib.api.loader.CraterLoader;
import com.hypherionmc.craterlib.core.event.CraterEventBus;
import com.hypherionmc.craterlib.core.event.annot.CraterEventListener;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.enums.GameType;
import com.hypherionmc.simplerpc.enums.RichPresenceState;
import com.hypherionmc.simplerpc.util.variables.RPCVariables;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * @author HypherionSA
 *
 * Main Mod Entrypoint for Modloaders
 */
public final class SimpleRPCClient {

    private static boolean hasShownWarning = false;

    public static void setupEvents() {
        CraterEventBus.INSTANCE.registerEventListener(SimpleRPCClient.class);
    }

    @CraterEventListener
    public static void init(LateInitEvent event) {
        SimpleRPCCore.INSTANCE.init();
        SimpleRPCCore.INSTANCE.setLangCode(event.getOptions().getLanguage() == null ? "en_us" : event.getOptions().getLanguage());
    }

    @CraterEventListener
    public static void playerJoinGame(CraterSinglePlayerEvent.PlayerLogin event) {
        if (event.getPlayer().getStringUUID().equals(CraterLoader.getClient().getPlayer().getStringUUID())) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.JOINING_GAME);
        }
    }

    @CraterEventListener
    public static void screenOpenEvent(ScreenEvent.Opening event) {
        if (event.getScreen().isTitleScreen()) {
            if (CraterLoader.isModLoaded("craftpresence") && !hasShownWarning) {
                CraterLoader.getClient().showWarningScreen(
                        Text.literal("Warning").style(Style.style(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)),
                        Text.literal("You have both Simple RPC and CraftPresence installed. This will cause your RPC to break and behave badly. Please remove either mod and restart the game")
                );
                hasShownWarning = true;
            }

            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.MAIN_MENU);
        }

        if (event.getScreen().isRealmsScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.REALM_MENU);
        }

        if (event.getScreen().isServerBrowserScreen() || event.getScreen().isDisconnectedScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.SERVER_MENU);
        }

        if (event.getScreen().isPauseScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.PAUSED);
        }

        if (event.getScreen().isLoadingScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.JOINING_GAME);
        }
    }

    @CraterEventListener
    public static void playerJoinRealm(PlayerJoinRealmEvent event) {
        RPCVariables.realmsServer = event.getServer();
    }

    @CraterEventListener
    public static void clientTick(CraterClientTickEvent event) {
        if (event.getLevel() == null || !event.getLevel().isClientSide())
            return;

        if (event.getLevel().getGameTime() % 40L == 0L) {
            if (CraterLoader.getClient().isRealmServer()) {
                SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.IN_GAME, GameType.REALM);
            } else {
                SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.IN_GAME, CraterLoader.getClient().isSinglePlayer() ? GameType.SINGLE : GameType.MULTIPLAYER);
            }
        }
    }
}
