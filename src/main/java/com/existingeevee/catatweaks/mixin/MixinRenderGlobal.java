package com.existingeevee.catatweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.catatweaks.renderers.sky.CatalystOverworldSkyRenderer;

import net.minecraft.client.renderer.RenderGlobal;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
	
	@Inject(method = "renderSky(FI)V", at = @At(value = "HEAD"))
	public void catalyst_tweaks$HEAD_Inject$renderSky(float partialTicks, int pass, CallbackInfo ci) {
		CatalystOverworldSkyRenderer.PASS.set(pass);
	}
}
