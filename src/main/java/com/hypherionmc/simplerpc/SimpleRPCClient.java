package com.hypherionmc.simplerpc;

import com.hypherionmc.craterlib.api.events.client.*;
import com.hypherionmc.craterlib.core.event.CraterEventBus;
import com.hypherionmc.craterlib.core.event.annot.CraterEventListener;
import com.hypherionmc.craterlib.nojang.client.BridgedMinecraft;
import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.enums.GameType;
import com.hypherionmc.simplerpc.enums.RichPresenceState;
import com.hypherionmc.simplerpc.util.variables.RPCVariables;

/**
 * @author HypherionSA
 *
 * Main Mod Entrypoint for Modloaders
 */
public class SimpleRPCClient {

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
        if (event.getPlayer().getStringUUID().equals(BridgedMinecraft.getInstance().getPlayer().getStringUUID())) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.JOINING_GAME);
        }
    }

    @CraterEventListener
    public static void screenOpenEvent(ScreenEvent.Opening event) {
        if (event.getScreen().isTitleScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.MAIN_MENU);
        }

        if (event.getScreen().isRealmsScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.REALM_MENU);
        }

        if (event.getScreen().isServerBrowserScreen()) {
            SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.SERVER_MENU);
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
            if (BridgedMinecraft.getInstance().isRealmServer()) {
                SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.IN_GAME, GameType.REALM);
            } else {
                SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.IN_GAME, BridgedMinecraft.getInstance().isSinglePlayer() ? GameType.SINGLE : GameType.MULTIPLAYER);
            }
        }
    }
}
