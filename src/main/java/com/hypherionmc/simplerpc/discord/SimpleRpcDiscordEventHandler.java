package com.hypherionmc.simplerpc.discord;

import com.hypherionmc.simplerpc.RPCConstants;
import dev.firstdark.rpc.enums.ErrorCode;
import dev.firstdark.rpc.handlers.DiscordEventHandler;
import dev.firstdark.rpc.models.DiscordJoinRequest;
import dev.firstdark.rpc.models.User;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 *
 * Discord Event handler to handle errors/events from the Discord RPC SDK
 */
public class SimpleRpcDiscordEventHandler implements DiscordEventHandler {

    @Override
    public void ready(User user) {
        RPCConstants.logger.info("Successfully connected to discord as {}", user.getUsername());
    }

    @Override
    public void disconnected(ErrorCode errorCode, @Nullable String s) {
        RPCConstants.logger.error("Disconnected from discord with error: {}, {}", errorCode.name(), s);
    }

    @Override
    public void errored(ErrorCode errorCode, @Nullable String s) {
        RPCConstants.logger.error("Encountered an error communicating with discord: {}, {}", errorCode.name(), s);
    }

    @Override
    public void joinGame(String s) {
        // UNUSED
    }

    @Override
    public void spectateGame(String s) {
        // UNUSED
    }

    @Override
    public void joinRequest(DiscordJoinRequest discordJoinRequest) {
        // UNUSED
    }
}
