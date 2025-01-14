package com.hypherionmc.simplerpc.api.rpc;

import com.hypherionmc.simplerpc.api.variables.PlaceholderEngine;
import dev.firstdark.rpc.models.DiscordRichPresence;
import lombok.RequiredArgsConstructor;

import static com.hypherionmc.simplerpc.api.utils.APIUtils.parseAndLimit;

/**
 * @author HypherionSA
 *
 * Wrapper class to convert RPC buttons from Config Format to Discord RPC sdk format
 */
@RequiredArgsConstructor
public final class ButtonWrapper {

    private final String label;
    private final String url;

    public ButtonWrapper() {
        this("", "");
    }

    /**
     * Convert the button to an RPC button, and parse any placeholders that might be present
     *
     * @return A ready to use {@link dev.firstdark.rpc.models.DiscordRichPresence.RPCButton}
     */
    public DiscordRichPresence.RPCButton rpcButton() {
        return DiscordRichPresence.RPCButton.of(parseAndLimit(label, 32), PlaceholderEngine.INSTANCE.resolvePlaceholders(url));
    }

}
