package com.existingeevee.catatweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.catatweaks.CataConstants;
import com.existingeevee.catatweaks.renderers.EmptyRenderer;
import com.existingeevee.catatweaks.renderers.sky.CatalystDreamscapeSkyRenderer;
import com.existingeevee.catatweaks.renderers.sky.CatalystOverworldSkyRenderer;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider {

	@Shadow(remap = false)
	abstract int getDimension();
	
	@Inject(at = @At(value = "HEAD"), method = "getSkyRenderer", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$getSkyRenderer(CallbackInfoReturnable<IRenderHandler> ci) {
		if (getDimension() == CataConstants.DREAMSCAPE_ID) {
			ci.setReturnValue(CatalystDreamscapeSkyRenderer.INSTANCE); 
			return;
		}
		
		if (getDimension() == CataConstants.OVERWORLD_ID) {
			ci.setReturnValue(CatalystOverworldSkyRenderer.INSTANCE); 
			return;
		}
	}
	
	@Inject(at = @At(value = "HEAD"), method = "getCloudRenderer", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$getCloudRenderer(CallbackInfoReturnable<IRenderHandler> ci) {
		if (getDimension() == CataConstants.DREAMSCAPE_ID) {
			ci.setReturnValue(EmptyRenderer.INSTANCE); 
			return;
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getWeatherRenderer", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$getWeatherRenderer(CallbackInfoReturnable<IRenderHandler> ci) {
		
	}
}
