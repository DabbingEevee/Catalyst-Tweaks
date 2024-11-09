package com.existingeevee.catatweaks.renderers.sky;

import com.existingeevee.catatweaks.CatalystTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;

public class CatalystDreamscapeSkyRenderer extends IRenderHandler {

	public static final CatalystDreamscapeSkyRenderer INSTANCE = new CatalystDreamscapeSkyRenderer();
	public static final ThreadLocal<Integer> PASS = ThreadLocal.withInitial(() -> 0);

	private CatalystDreamscapeSkyRenderer() {
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		RenderGlobal global = mc.renderGlobal;

		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.depthMask(false);
		GlStateManager.alphaFunc(516, 0.003921569F);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		float pct = world.getSunBrightnessFactor(partialTicks);
		float smoothPct = (float) (-0.5f * Math.cos(Math.PI * pct) + 0.5f);
		float skyBrightness = smoothPct / 8f;
		
		mc.renderEngine.bindTexture(new ResourceLocation(CatalystTweaks.MODID, "textures/dreamscape/blank.png"));
		for (int k1 = 0; k1 < 6; ++k1) {
			GlStateManager.pushMatrix();

			if (k1 == 1) {
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (k1 == 2) {
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (k1 == 3) {
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			}

			if (k1 == 4) {
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			}

			if (k1 == 5) {
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
			}

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(skyBrightness, skyBrightness, skyBrightness, 1f).endVertex();
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(skyBrightness, skyBrightness, skyBrightness, 1f).endVertex();
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(skyBrightness, skyBrightness, skyBrightness, 1f).endVertex();
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(skyBrightness, skyBrightness, skyBrightness, 1f).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
		float f16 = 1.0F - world.getRainStrength(partialTicks);		
		float f15 = world.getStarBrightness(partialTicks) * f16;

		if (f15 > 0.0F) {
			GlStateManager.color(f15, f15, f15, f15);

			if (global.vboEnabled) {
				global.starVBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				global.starVBO.drawArrays(7);
				global.starVBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			} else {
				GlStateManager.callList(global.starGLCallList);
			}
		}

		mc.renderEngine.bindTexture(new ResourceLocation(CatalystTweaks.MODID, "textures/dreamscape/horizon_glow_smooth.png"));
		double magnitude = 100;
		double height = 75;
		int amount = 32;

		for (int i = 0; i < amount; i++) {
			double x1 = Math.cos(Math.PI * 2 * i / 1d / amount) * magnitude;
			double y1 = Math.sin(Math.PI * 2 * i / 1d / amount) * magnitude;

			double x2 = Math.cos(Math.PI * 2 * (i + 1) / 1d / amount) * magnitude;
			double y2 = Math.sin(Math.PI * 2 * (i + 1) / 1d / amount) * magnitude;

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.pos(x1, -height, y1).tex(0.0D, 0.0D).color(1f, 1f, 1f, 0.25f).endVertex();
			bufferbuilder.pos(x2, -height, y2).tex(1.0D, 0.0D).color(1f, 1f, 1f, 0.25f).endVertex();
			bufferbuilder.pos(x2, height, y2).tex(1.0D, 1.0D).color(1f, 1f, 1f, 0.25f).endVertex();
			bufferbuilder.pos(x1, height, y1).tex(0.0D, 1.0D).color(1f, 1f, 1f, 0.25f).endVertex();
			tessellator.draw();
		}

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 720F, 0, 1F, 0);

		Vec3d colorCold = new Vec3d(1.000, 0.141, 0.000);
		Vec3d colorHot = new Vec3d(0.997, 0.546, 0.221);

		Vec3d color = new Vec3d(
				colorHot.x * smoothPct + colorCold.x * (1 - smoothPct),
				colorHot.y * smoothPct + colorCold.y * (1 - smoothPct),
				colorHot.z * smoothPct + colorCold.z * (1 - smoothPct));

		float red = (float) color.x;
		float green = (float) color.y;
		float blue = (float) color.z;

		magnitude = 175 + 50 * Math.cos(world.getCelestialAngle(partialTicks) * 2 * Math.PI * 32);
		mc.renderEngine.bindTexture(new ResourceLocation(CatalystTweaks.MODID, "textures/dreamscape/glow.png"));
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(-magnitude, -height, -magnitude).tex(0.0D, 0.0D).color(red, green, blue, smoothPct).endVertex();
		bufferbuilder.pos(-magnitude, -height, magnitude).tex(0.0D, 1.0D).color(red, green, blue, smoothPct).endVertex();
		bufferbuilder.pos(magnitude, -height, magnitude).tex(1.0D, 1.0D).color(red, green, blue, smoothPct).endVertex();
		bufferbuilder.pos(magnitude, -height, -magnitude).tex(1.0D, 0.0D).color(red, green, blue, smoothPct).endVertex();
		tessellator.draw();

		mc.renderEngine.bindTexture(new ResourceLocation(CatalystTweaks.MODID, "textures/dreamscape/glow.png"));
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(-magnitude, -height, -magnitude).tex(0.0D, 0.0D).color(0.675f, 0.691f, 0.846f, (1 - smoothPct * smoothPct) / 2).endVertex();
		bufferbuilder.pos(-magnitude, -height, magnitude).tex(0.0D, 1.0D).color(0.675f, 0.691f, 0.846f, (1 - smoothPct * smoothPct) / 2).endVertex();
		bufferbuilder.pos(magnitude, -height, magnitude).tex(1.0D, 1.0D).color(0.675f, 0.691f, 0.846f, (1 - smoothPct * smoothPct) / 2).endVertex();
		bufferbuilder.pos(magnitude, -height, -magnitude).tex(1.0D, 0.0D).color(0.675f, 0.691f, 0.846f, (1 - smoothPct * smoothPct) / 2).endVertex();
		tessellator.draw();
		
		// draw the body
		magnitude = 150;
		mc.renderEngine.bindTexture(new ResourceLocation(CatalystTweaks.MODID, "textures/dreamscape/nether_hot.png"));
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(-magnitude, -height, -magnitude).tex(0.0D, 0.0D).color(1f, 1f, 1f, 1).endVertex();
		bufferbuilder.pos(-magnitude, -height, magnitude).tex(0.0D, 1.0D).color(1f, 1f, 1f, 1).endVertex();
		bufferbuilder.pos(magnitude, -height, magnitude).tex(1.0D, 1.0D).color(1f, 1f, 1f, 1).endVertex();
		bufferbuilder.pos(magnitude, -height, -magnitude).tex(1.0D, 0.0D).color(1f, 1f, 1f, 1).endVertex();
		tessellator.draw();
		mc.renderEngine.bindTexture(new ResourceLocation(CatalystTweaks.MODID, "textures/dreamscape/nether_cold.png"));
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(-magnitude, -height, -magnitude).tex(0.0D, 0.0D).color(1f, 1f, 1f, 1 - pct).endVertex();
		bufferbuilder.pos(-magnitude, -height, magnitude).tex(0.0D, 1.0D).color(1f, 1f, 1f, 1 - pct).endVertex();
		bufferbuilder.pos(magnitude, -height, magnitude).tex(1.0D, 1.0D).color(1f, 1f, 1f, 1 - pct).endVertex();
		bufferbuilder.pos(magnitude, -height, -magnitude).tex(1.0D, 0.0D).color(1f, 1f, 1f, 1 - pct).endVertex();
		tessellator.draw();

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
	}
}