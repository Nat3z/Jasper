package com.nat3z.mixins.jasper;

import com.nat3z.jasper.JasperMod;
import com.nat3z.jasper.impls.GrandmaWolf;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundHandler.class)
public class MixinSoundHandler {

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void playSound(ISound sound, CallbackInfo ci) {
        /* check if sound is when exp is grabbed */
        if (sound.getSoundLocation().toString().equals("minecraft:random.orb")) {
            JasperMod.LOGGER.info("Matched EXP Gained Sound.");
            GrandmaWolf.Companion.getINSTANCE().expSoundEvent();
        }
    }
}
