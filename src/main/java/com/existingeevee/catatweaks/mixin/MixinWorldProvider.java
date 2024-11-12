package com.existingeevee.catatweaks.mixin;

import java.util.Iterator;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.catatweaks.CataConstants;
import com.existingeevee.catatweaks.renderers.EmptyRenderer;
import com.existingeevee.catatweaks.renderers.sky.CatalystDreamscapeSkyRenderer;
import com.existingeevee.catatweaks.renderers.sky.CatalystOverworldSkyRenderer;
import com.mushroom.midnight.common.config.MidnightConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.IRenderHandler;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider {

	@Shadow(remap = false)
	abstract int getDimension();

	@Shadow
	float[] lightBrightnessTable;

	@Shadow(remap = false)
    abstract void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful);
	
	@Shadow()
	World world;

	@Inject(at = @At(value = "HEAD"), method = "getSkyRenderer", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$getSkyRenderer(CallbackInfoReturnable<IRenderHandler> ci) {
		if (getDimension() == CataConstants.getDreamScapeID()) {
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
		if (getDimension() == CataConstants.getDreamScapeID()) {
			ci.setReturnValue(EmptyRenderer.INSTANCE);
			return;
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "generateLightBrightnessTable", cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$generateLightBrightnessTable(CallbackInfo ci) {
		if (getDimension() == CataConstants.getDreamScapeID()) {
			float baseLight = 0.06F;
			for (int i = 0; i <= 15; ++i) {
				float alpha = 1.0F - i / 15.0F;
				float brightness = (1.0F - alpha) / (alpha * 10.0F + 1.0F);
				this.lightBrightnessTable[i] = (float) (Math.pow(brightness, 2.5F) * 3.0F) + baseLight;
			}

			ci.cancel();
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getLightmapColors", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors, CallbackInfo ci) {
		if (getDimension() == CataConstants.getDreamScapeID()) {
			colors[0] = blockLight * 0.93F + 0.07F;
			colors[1] = blockLight * 0.96F + 0.03F;
			colors[2] = blockLight * 0.94F + 0.16F;
			if (this.world.getLastLightningBolt() > 0) {
				colors[0] = 0.95F;
				colors[1] = 0.3F;
				colors[2] = 0.3F;
			}

			ci.cancel();
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "calculateInitialWeather", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$calculateInitialWeather(CallbackInfo ci) {
		if (getDimension() == CataConstants.getDreamScapeID()) {
			ci.cancel();
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "canDoLightning", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$canDoLightning(Chunk chunk, CallbackInfoReturnable<Boolean> ci) {
		if (getDimension() == CataConstants.getDreamScapeID()) {
			ci.setReturnValue(false);
		}
	}
	
	@Inject(at = @At(value = "HEAD"), method = "updateWeather", remap = false, cancellable = true)
	public void catalyst_tweaks$HEAD_Inject$updateWeather(CallbackInfo ci) {
		if (getDimension() == CataConstants.getDreamScapeID()) {
	        setAllowedSpawnTypes(false, false);
	        if (this.world instanceof WorldServer) {
	            WorldServer worldServer = (WorldServer) this.world;
	            PlayerChunkMap chunkMap = worldServer.getPlayerChunkMap();
	            Random rand = this.world.rand;

	            Iterator<Chunk> iterator = this.world.getPersistentChunkIterable(chunkMap.getChunkIterator());
	            iterator.forEachRemaining(chunk -> {
	                int globalX = chunk.x << 4;
	                int globalZ = chunk.z << 4;
	                if (rand.nextInt(200000) == 0) {
	                    int lightningX = globalX + rand.nextInt(16);
	                    int lightningZ = globalZ + rand.nextInt(16);
	                    BlockPos pos = this.world.getPrecipitationHeight(new BlockPos(lightningX, 0, lightningZ));

	                    Entity lightning = new EntityLightningBolt(this.world, pos.getX(), pos.getY(), pos.getZ(), !MidnightConfig.general.allowLightningDamage);
	                    this.world.addWeatherEffect(lightning);
	                }
	            });
	        }
	        ci.cancel();
		}
	}
}
