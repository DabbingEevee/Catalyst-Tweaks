package com.existingeevee.catatweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.catatweaks.CataConstants;
import com.mushroom.midnight.client.ClientEventHandler;
import com.mushroom.midnight.common.helper.Helper;

import net.minecraft.world.World;

@Mixin(Helper.class)
public class MixinHelper {

	@Inject(method = "isMidnightDimension", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void catalyst_tweaks$HEAD_Inject$isMidnightDimension(World world, CallbackInfoReturnable<Boolean> ci) {
		if (world != null && world.provider.getDimension() == CataConstants.getDreamScapeID()) {
			StackTraceElement element = Thread.currentThread().getStackTrace()[3];
			if (element.getClassName().equals(ClientEventHandler.class.getName()) && element.getMethodName().equals("onSetupFogDensity")) {
				ci.setReturnValue(false);
			} else {
				ci.setReturnValue(true);
			}
		}
	}
}
