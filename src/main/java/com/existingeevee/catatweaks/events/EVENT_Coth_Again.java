package com.existingeevee.catatweaks.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EVENT_Coth_Again extends EVENT_Coth_FirstContact {
    public EVENT_Coth_Again(@Nullable EntityPlayer p) {
        super(p);
        this.eventTime = 390;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientTick() {
        if (this.phase == (byte) 0 && this.eventProgress > 10) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.coth_phase1.title1"), "", 40, 80, 20);
            this.phase++;
        } else if (this.phase == (byte) 1 && this.eventProgress > 150) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.coth_phase1.title2"), "", 20, 60, 20);
            this.phase++;
        } else if (this.phase == (byte) 2 && this.eventProgress > 250) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.coth_phase1.title3"), "", 20, 80, 40);
            this.phase++;
        }
    }
}
