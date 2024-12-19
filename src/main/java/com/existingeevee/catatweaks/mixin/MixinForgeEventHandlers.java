package com.existingeevee.catatweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mcjty.lostcities.ForgeEventHandlers;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

@Mixin(ForgeEventHandlers.class)
public class MixinForgeEventHandlers {

	@Inject(at = @At("HEAD"), remap = false, method = "onPlayerSleepInBedEvent", cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$onPlayerSleepInBedEvent(PlayerSleepInBedEvent event, CallbackInfo ci) {
    	ci.cancel();
    }
	
}
