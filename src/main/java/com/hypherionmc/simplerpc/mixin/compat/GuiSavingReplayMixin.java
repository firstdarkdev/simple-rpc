package com.hypherionmc.simplerpc.mixin.compat;

import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import com.hypherionmc.simplerpc.enums.RichPresenceState;
import com.hypherionmc.simplerpc.integrations.ReplayModCompat;
import com.replaymod.render.gui.GuiVideoRenderer;
import com.replaymod.render.rendering.VideoRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author HypherionSA
 */
@Mixin(GuiVideoRenderer.class)
public class GuiSavingReplayMixin {

    @Shadow(remap = false) private int renderTimeTaken;

    @Shadow(remap = false) private int renderTimeLeft;

    @Shadow(remap = false) @Final
    private VideoRenderer renderer;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/element/GuiLabel;setText(Ljava/lang/String;)Lcom/replaymod/lib/de/johni0702/minecraft/gui/element/AbstractGuiLabel;", ordinal = 0), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectDraw(CallbackInfo ci) {
        ReplayModCompat.renderTimeTaken.set(this.secToString(this.renderTimeTaken / 1000));
        ReplayModCompat.renderTimeLeft.set(this.secToString(this.renderTimeLeft));
        ReplayModCompat.renderFramesDone.set(this.renderer.getFramesDone());
        ReplayModCompat.renderFramesTotal.set(this.renderer.getTotalFrames());
        SimpleRPCCore.INSTANCE.getEvents().setRPCState(RichPresenceState.REPLAY_RENDER);
    }

    @Unique
    private String secToString(int seconds) {
        int hours = seconds / 3600;
        int min = seconds / 60 - hours * 60;
        int sec = seconds - (min * 60 + hours * 60 * 60);
        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append(hours).append("hour(s)");
        }

        if (min > 0 || hours > 0) {
            builder.append(min).append("minute(s)");
        }

        builder.append(sec).append("second(s)");
        return builder.toString();
    }

}
