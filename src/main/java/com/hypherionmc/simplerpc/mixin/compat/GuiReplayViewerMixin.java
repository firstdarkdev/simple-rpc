package com.hypherionmc.simplerpc.mixin.compat;

import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.enums.RichPresenceState;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author HypherionSA
 */
@Mixin(GuiReplayViewer.class)
public class GuiReplayViewerMixin {

    @Inject(method = "updateButtons", at = @At("HEAD"), remap = false)
    private void injectInit(CallbackInfo ci) {
        SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.REPLAY_BROWSER);
    }

}