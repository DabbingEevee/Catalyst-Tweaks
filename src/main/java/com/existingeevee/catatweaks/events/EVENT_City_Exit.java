package com.existingeevee.catatweaks.events;

import com.existingeevee.catatweaks.CatalystTweaksSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EVENT_City_Exit extends EVENT_Coth_FirstContact {
    public EVENT_City_Exit(@Nullable EntityPlayer p) {
        super(p);
        this.eventTime = 550;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientEventStart() {}

    public byte soundPhase = (byte) 0;

    @Override
    @SideOnly(Side.CLIENT)
    public void clientTick() {
        if (this.soundPhase == (byte) 0 && this.eventProgress >= 0) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(CatalystTweaksSounds.presence_start, 1F));//0,400
            });
            this.soundPhase++;
        } else if (this.soundPhase == (byte) 1 && this.eventProgress >= 340) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(CatalystTweaksSounds.presence_end, 1F));//-60,360
            });
            this.soundPhase++;
        }


        if (this.phase == (byte) 0 && this.eventProgress > 10) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.city_exit.title1"), "", 40, 60, 20);
            this.phase++;
        } else if (this.phase == (byte) 1 && this.eventProgress > 130) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.city_exit.title2"), "", 20, 60, 20);
            this.phase++;
        } else if (this.phase == (byte) 2 && this.eventProgress > 230) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.city_exit.title3"), "", 20, 60, 20);
            this.phase++;
        } else if (this.phase == (byte) 3 && this.eventProgress > 330) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.city_exit.title4"), "", 20, 60, 20);
            this.phase++;
        } else if (this.phase == (byte) 4 && this.eventProgress > 430) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.city_exit.title5"), "", 20, 60, 40);
            this.phase++;
        }
    }
}
