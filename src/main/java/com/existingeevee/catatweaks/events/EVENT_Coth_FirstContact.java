package com.existingeevee.catatweaks.events;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.existingeevee.catatweaks.CatalystTweaksSounds;
import energon.nebulalib.event.EventSaveData;
import energon.nebulalib.event.events.EventBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

public class EVENT_Coth_FirstContact extends EventBase {
    private Collection<PotionEffect> effects;
    public static ResourceLocation TEXTURE = new ResourceLocation("catalyst_tweaks", "textures/gui/tentacle_base.png");
    public static ResourceLocation TEXTUREN = new ResourceLocation("catalyst_tweaks", "textures/gui/tentacle_mid.png");
    public static ResourceLocation TEXTURENN = new ResourceLocation("catalyst_tweaks", "textures/gui/tentacle_tip.png");
    public EVENT_Coth_FirstContact(@Nullable EntityPlayer p) {
        super(p, 330);
    }

    @Override
    public void getFromData(String data, boolean playerEvent) {
        this.effects = EventSaveData.getPotionsFromData(data);
    }

    @Override
    public void serverEventStart() {
        if (this.player != null) {
            if (!this.fromData) {
                this.effects = new ArrayList<>(this.player.getActivePotionEffects());
                this.saveToData(EventSaveData.createDataForPotions(this.effects, ""));
                this.player.clearActivePotions();
            }
            this.player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, this.eventTime - 20, 4, false, false));
            this.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, this.eventTime - 20, 1, false, false));
            this.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, this.eventTime - 20, 4, false, false));
            for (ItemStack stack : this.player.inventory.mainInventory) {
                if (!stack.isEmpty()) {
                    this.player.getCooldownTracker().setCooldown(stack.getItem(), this.eventTime - 10);
                }
            }
        }
    }

    @Override
    public void serverTick() {
        if (this.player != null) {
            int radius = 16;
            int height = 8;
            for (EntityLiving target : this.player.world.getEntitiesWithinAABB(EntityLiving.class,
                    new AxisAlignedBB(this.player.posX - radius, this.player.posY - height, this.player.posZ - radius,
                            this.player.posX + radius, this.player.posY + height, this.player.posZ + radius))) {
                target.setAttackTarget(null);
                target.setRevengeTarget(null);
                target.getNavigator().clearPath();
                if (this.eventProgress % 5 == 2) {
                    if (target instanceof EntityParasiteBase) {
                        ((EntityParasiteBase) target).setWait(60);
                    }
                    target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 255));
                    target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, 255));
                }
            }
        }
    }

    @Override
    public void serverEventEnd() {
        if (this.player != null && this.effects != null) {
            //this.player.clearActivePotions();
            for (PotionEffect potionEffect : this.effects) {
                this.player.addPotionEffect(potionEffect);
            }
            effects.clear();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientEventStart() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(CatalystTweaksSounds.presence, 1F));
        });
    }

    public byte phase = (byte) 0;

    @Override
    @SideOnly(Side.CLIENT)
    public void clientTick() {
        if (this.phase == (byte) 0 && this.eventProgress > 10) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.coth_phase0.title1"), "", 40, 100, 20);
            this.phase++;
        } else if (this.phase == (byte) 1 && this.eventProgress > 170) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(I18n.translateToLocal("catalyst_tweaks.event.coth_phase0.title2"), "", 20, 100, 60);
            this.phase++;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void overlayRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        //mc.player.sendMessage(new TextComponentString("FACTOR: " + res.getScaleFactor() + "  WEIGHT: " + res.getScaledWidth() + "  HEIGHT: " + res.getScaledHeight()));
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();
        float partialTicks = event.getPartialTicks();
        float progress;
        if (this.eventProgress < 60) {
            progress = 1F - MathHelper.sin(Math.min((this.eventProgress + partialTicks) * 0.02F, 1F) * 1.57F);
        } else if (this.eventProgress > (this.eventTime - 60)) {
            progress = 1F - MathHelper.sin(Math.min((this.eventTime - this.eventProgress - partialTicks) * 0.02F, 1F) * 1.57F);
        } else {
            progress = 0F;
        }

        float f3 = mc.player.ticksExisted + partialTicks;
        float resScale = (screenHeight / 540F) * 0.85F;

        //Upper Left
        //1
        this.tentacle(mc, -30 * resScale - progress * 300, 120 * resScale, 120, MathHelper.sin(f3 * 0.09F) * (1.0F - progress * 0.8F), false, 1F * resScale);
        //2
        this.tentacle(mc, 210 * resScale - progress * 300, -30 * resScale - progress * 150, 150, MathHelper.sin(f3 * 0.071F) * (1.0F - progress * 0.8F), false, 0.9F * resScale);

        //Lower Left
        //3
        this.tentacle(mc, -30 * resScale - progress * 300, screenHeight - 120 * resScale, 60, -MathHelper.sin(f3 * 0.043F) * (1.0F - progress * 0.9F), false, resScale);
        //4
        this.tentacle(mc, 210 * resScale - progress * 300, screenHeight + 30 * resScale + progress * 150, 30, -MathHelper.sin(f3 * 0.083F) * (1.0F - progress * 0.9F), false, 1F * resScale);

        //Upper Right
        //5
        this.tentacle(mc, screenWidth + 30 * resScale + progress * 300, 120 * resScale, -120, -MathHelper.sin(f3 * 0.091F) * (1.0F - progress * 0.8F), true, 0.9F * resScale);
        //6
        this.tentacle(mc, screenWidth - 210 * resScale + progress * 300, -30 * resScale - progress * 150, -150, -MathHelper.sin(f3 * 0.081F) * (1.0F - progress * 0.9F), true, 1F * resScale);

        //Lower Right
        //7
        this.tentacle(mc, screenWidth + 30 * resScale + progress * 300, screenHeight - 120 * resScale, -60, -MathHelper.sin(f3 * 0.065F) * (1.0F - progress * 0.7F), true, resScale);
        //8
        this.tentacle(mc, screenWidth - 210 * resScale + progress * 300, screenHeight + 30 * resScale + progress * 150, -30, MathHelper.sin(f3 * 0.082F) * (1.0F - progress * 0.8F), true, 0.9F * resScale);
    }

    public void tentacle(Minecraft mc, float x, float y, float angle, float sin, boolean re, float scale) {
        GlStateManager.pushMatrix();

        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(sin * 10 + angle, 0, 0, 1);
        draw((int) (-50 * scale), (int) (-80 * scale), (int) (100 * scale), (int) (100 * scale), re);

        mc.getTextureManager().bindTexture(TEXTUREN);
        GlStateManager.translate(0, -81.25 * scale, 0);
        GlStateManager.rotate(sin * 10 , 0, 0, 1);
        draw((int) (-50 * scale), (int) (-80 * scale), (int) (100 * scale), (int) (100 * scale), re);

        mc.getTextureManager().bindTexture(TEXTURENN);
        GlStateManager.translate(0, -81.25 * scale, 0);
        GlStateManager.rotate(sin * 10 , 0, 0, 1);
        draw((int) (-50 * scale), (int) (-80 * scale), (int) (100 * scale), (int) (100 * scale), re);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void draw(int x, int y, int width, int height, boolean rev) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        if (rev) {
            buffer.pos(x, y + height, 0)
                    .tex(0D, 1D).endVertex();
            buffer.pos(x + width, y + height, 0)
                    .tex(1F, 1F).endVertex();
            buffer.pos(x + width, y, 0)
                    .tex(1D, 0D).endVertex();
            buffer.pos(x, y, 0)
                    .tex(0D, 0D).endVertex();
        } else {
            buffer.pos(x, y + height, 0)
                    .tex(1D, 1D).endVertex();
            buffer.pos(x + width, y + height, 0)
                    .tex(0D, 1D).endVertex();
            buffer.pos(x + width, y, 0)
                    .tex(0D, 0D).endVertex();
            buffer.pos(x, y, 0)
                    .tex(1D, 0D).endVertex();
        }
        tessellator.draw();
    }

    @Override
    public boolean disableAttack(LivingAttackEvent event) {
        return true;
    }

    @Override
    public boolean disableGetDamage(LivingAttackEvent event) {
        return true;
    }

    @Override
    public boolean disableChangeDimension(EntityTravelToDimensionEvent event) {
        return true;
    }

    @Override
    public boolean disableBreakBlock(BlockEvent.BreakEvent event) {
        return true;
    }

    @Override
    public boolean disablePlaceBlock(BlockEvent.EntityPlaceEvent event) {
        return true;
    }

    @Override
    public boolean disableInteractBlock(PlayerInteractEvent.RightClickBlock event) {
        return true;
    }
}
