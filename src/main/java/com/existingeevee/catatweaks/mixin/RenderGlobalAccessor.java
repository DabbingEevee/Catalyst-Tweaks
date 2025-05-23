package com.existingeevee.catatweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderGlobal.class)
public interface RenderGlobalAccessor {
    @Accessor(value = "world") WorldClient getWorld();
    @Accessor(value = "mc") Minecraft getMinecraft();
    @Accessor(value = "vboEnabled") boolean getVBOenabled();
    @Accessor(value = "skyVBO") VertexBuffer getSkyVBO();
    @Accessor(value = "starVBO") VertexBuffer getStarVBO();
    @Accessor(value = "sky2VBO") VertexBuffer getSky2VBO();
    @Accessor(value = "glSkyList") int getGlSkyList();
    @Accessor(value = "glSkyList2") int getGlSkyList2();
    @Accessor(value = "starGLCallList") int getStarGLCallList();

    @Accessor(value = "SUN_TEXTURES") static ResourceLocation getSUN_TEXTURES() {return new ResourceLocation("textures/environment/sun.png");}
    @Accessor(value = "MOON_PHASES_TEXTURES") static ResourceLocation getMOON_PHASES_TEXTURES() {return  new ResourceLocation("textures/environment/moon_phases.png");}
}
