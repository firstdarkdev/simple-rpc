package com.hypherionmc.simplerpc.mixin.compat;

import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.enums.RichPresenceState;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.RenderInfo;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import com.replaymod.replay.gui.overlay.GuiReplayOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author HypherionSA
 */
@Mixin(GuiReplayOverlay.class)
public class GuiReplayOverlayMixin {

    @Inject(method = "draw", at = @At("HEAD"), remap = false)
    private void injectDraw(GuiRenderer renderer, ReadableDimension size, RenderInfo renderInfo, CallbackInfo ci) {
        SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.REPLAY_EDITOR);
    }

}