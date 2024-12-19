package com.existingeevee.catatweaks.event_listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.existingeevee.catatweaks.CatalystTweaks;

import mcjty.lostcities.ForgeEventHandlers;
import mcjty.lostcities.config.LostCityConfiguration;
import mcjty.lostcities.varia.CustomTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@EventBusSubscriber(modid = CatalystTweaks.MODID)
public class MiscHandler {

	@SubscribeEvent
	public static void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event) {		
		if (LostCityConfiguration.DIMENSION_ID == -1) {
			return;
		}

		World world = event.getEntityPlayer().getEntityWorld();
		if (world.isRemote) {
			return;
		}
		BlockPos bedLocation = event.getPos();
		if (!isValidSpawnBed(world, bedLocation)) {
			return;
		}

		if (world.provider.getDimension() == LostCityConfiguration.DIMENSION_ID) {
			event.setResult(Event.Result.DENY);
			BlockPos location;

			if (event.getEntityPlayer().getEntityData().hasKey("CataTweaks.SleepPos", NBT.TAG_LONG)) {
				long val = event.getEntityPlayer().getEntityData().getLong("CataTweaks.SleepPos");
				location = BlockPos.fromLong(val);
			} else {
				WorldServer destWorld = DimensionManager.getWorld(0);
				location = findLocation(bedLocation, destWorld);
			}

			CustomTeleporter.teleportToDimension(event.getEntityPlayer(), 0, location);
		} else {
			event.setResult(Event.Result.DENY);
			WorldServer destWorld = event.getEntity().getEntityWorld().getMinecraftServer().getWorld(LostCityConfiguration.DIMENSION_ID);
			BlockPos location = findLocation(bedLocation, destWorld);

			event.getEntityPlayer().getEntityData().setLong("CataTweaks.SleepPos", event.getEntityPlayer().getPosition().toLong());

			CustomTeleporter.teleportToDimension(event.getEntityPlayer(), LostCityConfiguration.DIMENSION_ID, location);
		}
	}

	@ObjectHolder("simplenightmares:dreamcatcher")
	public static final Block DREAMCATCHER = null;
	
	private static boolean isValidSpawnBed(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockBed)) {
			return false;
		}
		EnumFacing direction = Blocks.BED.getBedDirection(state, world, pos);
		Block b1 = world.getBlockState(pos.down()).getBlock();
		Block b2 = world.getBlockState(pos.offset(direction.getOpposite()).down()).getBlock();
		Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(LostCityConfiguration.SPECIAL_BED_BLOCK));
		if (b1 != b || b2 != b) {
			return false;
		}

		if (world.getBlockState(pos.offset(direction)).getBlock() != DREAMCATCHER) {
			return false;
		}


		if (world.getBlockState(pos.offset(direction.getOpposite(), 2)).getBlock() != DREAMCATCHER) {
			return false;
		}

		return true;
	}

	private static BlockPos findLocation(BlockPos bedLocation, WorldServer destWorld) {
		BlockPos top = destWorld.getTopSolidOrLiquidBlock(bedLocation);
		BlockPos location = findValidTeleportLocation(destWorld, top);
		if (location == null) {
			location = top;
			if (destWorld.isAirBlock(top.down())) {
				// No place to teleport
				destWorld.setBlockState(bedLocation, Blocks.COBBLESTONE.getDefaultState());
				location = bedLocation.up();
			}
		}
		return location;
	}

	@SubscribeEvent
	public static void onClone(Clone ev) {
		if (ev.getOriginal().getEntityData().hasKey("CataTweaks.SleepPos", NBT.TAG_LONG)) {
			ev.getEntityPlayer().getEntityData().setLong("CataTweaks.SleepPos", 
					ev.getOriginal().getEntityData().getLong("CataTweaks.SleepPos"));
		}

	}
	
	protected static final Method findValidTeleportLocation$ForgeEventHandlers = ObfuscationReflectionHelper.findMethod(ForgeEventHandlers.class, "findValidTeleportLocation", BlockPos.class, World.class, BlockPos.class);

	private static BlockPos findValidTeleportLocation(World world, BlockPos start) {
		try {
			return (BlockPos) findValidTeleportLocation$ForgeEventHandlers.invoke(new ForgeEventHandlers(), world, start);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
