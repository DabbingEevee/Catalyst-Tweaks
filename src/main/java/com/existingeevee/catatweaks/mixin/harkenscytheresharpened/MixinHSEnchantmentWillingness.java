package com.existingeevee.catatweaks.mixin.harkenscytheresharpened;

import mod.emt.harkenscythe.enchantment.HSEnchantmentWillingness;
import mod.emt.harkenscythe.item.tool.HSToolGlaive;
import mod.emt.harkenscythe.item.tool.HSToolScythe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HSEnchantmentWillingness.class)
public abstract class MixinHSEnchantmentWillingness extends Enchantment {
    protected MixinHSEnchantmentWillingness(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Inject(
            method = "canApply",
            at = @At("HEAD"),
            cancellable = true
    )
    private void catalyst_tweaks$HEAD_Inject$canApply(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(super.canApply(stack));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof HSToolGlaive || stack.getItem() instanceof HSToolScythe;
    }
}
