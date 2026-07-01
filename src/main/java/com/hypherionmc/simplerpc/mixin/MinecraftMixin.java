package com.hypherionmc.simplerpc.mixin;

import com.hypherionmc.simplerpc.discord.SimpleRPCCore;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Move this into craterlib
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "close", at = @At("HEAD"))
    public void close(CallbackInfo ci) {
        SimpleRPCCore.INSTANCE.shutdown();
    }

}
