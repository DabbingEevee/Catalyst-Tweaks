package com.existingeevee.catatweaks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CatalystTweaksSounds {
    public static SoundEvent presence;
    public static SoundEvent presence_start;
    public static SoundEvent presence_middle;
    public static SoundEvent presence_end;

    public static void registerSounds() {
        presence = registerSound("presence");
        presence_start = registerSound("presence_start");
        presence_middle = registerSound("presence_middle");
        presence_end = registerSound("presence_end");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(CatalystTweaks.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
