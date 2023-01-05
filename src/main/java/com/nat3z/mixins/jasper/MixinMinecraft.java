package com.nat3z.mixins.jasper;

import com.nat3z.jasper.JasperMod;
import com.nat3z.jasper.impls.hooks.MinecraftHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(GameConfiguration gameConfig, CallbackInfo ci) {
        MinecraftHook.INSTANCE.checkUpdates(ci);
    }
    @Inject(method = "shutdown", at = @At("RETURN"))
    public void shutdown(CallbackInfo ci) {
        JasperMod.Companion.getHudPlacement().saveConfig();
        MinecraftHook.INSTANCE.startUpdate();
    }

}