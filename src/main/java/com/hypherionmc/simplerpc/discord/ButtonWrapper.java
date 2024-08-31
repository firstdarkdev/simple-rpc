package com.hypherionmc.simplerpc.discord;

import com.hypherionmc.simplerpc.util.variables.PlaceholderEngine;
import dev.firstdark.rpc.models.DiscordRichPresence;
import lombok.RequiredArgsConstructor;

import static com.hypherionmc.simplerpc.util.APIUtils.parseAndLimit;

/**
 * @author HypherionSA
 *
 * Wrapper class to convert RPC buttons from Config Format to Discord RPC sdk format
 */
@RequiredArgsConstructor
public final class ButtonWrapper {

    private final String label;
    private final String url;

    /**
     * Convert the button to an RPC button, and parse any placeholders that might be present
     *
     * @return A ready to use {@link dev.firstdark.rpc.models.DiscordRichPresence.RPCButton}
     */
    public DiscordRichPresence.RPCButton rpcButton() {
        return DiscordRichPresence.RPCButton.of(parseAndLimit(label, 32), PlaceholderEngine.INSTANCE.resolvePlaceholders(url));
    }

}
