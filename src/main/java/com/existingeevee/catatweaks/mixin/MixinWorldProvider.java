package com.existingeevee.catatweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.catatweaks.CatalystOverworldSkyRenderer;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider {

	@Shadow()
	abstract int getDimension();
	
	@Inject(at = @At(value = "HEAD"), method = "getSkyRenderer", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$getSkyRenderer(CallbackInfoReturnable<IRenderHandler> ci) {
		if (getDimension() == 0) {
			ci.setReturnValue(CatalystOverworldSkyRenderer.INSTANCE); 
			return;
		}
	}

}
