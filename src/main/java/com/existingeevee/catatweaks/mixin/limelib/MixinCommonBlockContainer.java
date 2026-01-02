package com.existingeevee.catatweaks.mixin.limelib;

import mrriegel.limelib.block.CommonBlockContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommonBlockContainer.class)
public abstract class MixinCommonBlockContainer extends Block {
    public MixinCommonBlockContainer(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    @Inject(method = "getPickBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void catalyst_tweaks$HEAD_Inject$getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(super.getPickBlock(state, target, world, pos, player));
    }
}
