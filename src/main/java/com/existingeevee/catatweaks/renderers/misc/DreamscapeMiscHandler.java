package com.existingeevee.catatweaks.renderers.misc;

import com.existingeevee.catatweaks.CataConstants;
import com.existingeevee.catatweaks.CatalystTweaks;

import net.minecraft.client.Minecraft;
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
			event.setRed(0.474f);
			event.setRed(0.336f);
			event.setRed(1.000f);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onFogDensity(FogDensity event) {
		World world = Minecraft.getMinecraft().world;
		if (world != null && world.provider.getDimension() == CataConstants.DREAMSCAPE_ID) {
			event.setDensity(100f);
		}
	}
}
