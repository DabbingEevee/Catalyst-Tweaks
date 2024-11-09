package com.existingeevee.catatweaks.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class EmptyRenderer extends IRenderHandler {

	public static final EmptyRenderer INSTANCE = new EmptyRenderer();

	private EmptyRenderer() {
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {

	}

}
