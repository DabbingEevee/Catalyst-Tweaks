package com.existingeevee.catatweaks.renderers.misc;

import com.existingeevee.catatweaks.CataConstants;
import com.existingeevee.catatweaks.CatalystTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = CatalystTweaks.MODID)
public class DreamscapeMiscHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onFogColors(FogColors event) {
		World world = Minecraft.getMinecraft().world;
		if (world != null && world.provider.getDimension() == CataConstants.DREAMSCAPE_ID) {
			
			float pct = world.getSunBrightnessFactor(Minecraft.getMinecraft().getRenderPartialTicks());
			float smoothPct = (float) (-0.5f * Math.cos(Math.PI * pct) + 0.5f);
			
			Vec3d nightFog = new Vec3d(0.474f, 0.336f, 1.000f);
			Vec3d dayFog = new Vec3d(0.750f, 0.250f, 0.250f);
			
			event.setRed((float) (smoothPct * dayFog.x + (1 - smoothPct) * nightFog.x));
			event.setGreen((float) (smoothPct * dayFog.y + (1 - smoothPct) * nightFog.y));
			event.setBlue((float) (smoothPct * dayFog.z + (1 - smoothPct) * nightFog.z));
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onFogDensity(FogDensity event) {
		World world = Minecraft.getMinecraft().world;
		if (world != null && world.provider.getDimension() == CataConstants.DREAMSCAPE_ID) {
			event.setDensity(1f);
		}
	}
}
