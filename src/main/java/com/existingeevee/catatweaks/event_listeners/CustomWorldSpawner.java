package com.existingeevee.catatweaks.event_listeners;

import java.util.List;
import java.util.stream.Collectors;

import com.existingeevee.catatweaks.CataConstants;
import com.existingeevee.catatweaks.CatalystTweaks;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber(modid = CatalystTweaks.MODID)
public final class CustomWorldSpawner {

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.world.isRemote || !event.world.getGameRules().getBoolean("doMobSpawning") || event.world.provider.getDimension() != CataConstants.getDreamScapeID())
			return;

		World world = event.world;

		if (world.getTotalWorldTime() % 20 == 0 && world.rand.nextInt(4) == 0) {

			@SuppressWarnings("unchecked")
			Class<EntityLiving> weepingAngelClass = (Class<EntityLiving>) EntityList.getClassFromName("weeping-angels:weepingangel");

			for (EntityPlayer player : getPlayers(world)) {

				if (weepingAngelClass == null)
					continue;

				int amount = world.getEntities(weepingAngelClass, e -> true).size();

				if (amount < 120) {
					double r = Math.random() * 5 + 35;
					double theta = Math.random() * Math.PI * 2;

					double xOff = Math.cos(theta) * r;
					double zOff = Math.sin(theta) * r;

					int x = (int) Math.round(player.posX + xOff);
					int z = (int) Math.round(player.posZ + zOff);

					int surfaceY = 255;
					while (world.isAirBlock(new BlockPos(x, surfaceY, z)) && surfaceY >= 0) {
						surfaceY--;
					}

					surfaceY++;

					BlockPos pos = new BlockPos(x, surfaceY, z);
					if (Math.abs(surfaceY - player.posY) > 25 || world.getChunk(pos).getLightFor(EnumSkyBlock.BLOCK, pos) > 0 || !(world.getWorldTime() > 13500 && world.getWorldTime() < 22500)) {
						continue;
					} else {
						if (!world.getPlayers(EntityPlayer.class, p -> p.getDistanceSq(pos) <= 35 * 35).isEmpty()) {
							continue;
						}

						EntityLiving angel = createEntity(world, pos, weepingAngelClass);
						if (world.getBlockState(pos.down()).canEntitySpawn(angel)) {
							angel.onInitialSpawn(world.getDifficultyForLocation(pos), null);
							world.spawnEntity(angel);
						}
					}
				}
			}
		}

		if (world.getTotalWorldTime() % 20 == 0 && world.rand.nextInt(15) == 0) {

			@SuppressWarnings("unchecked")
			Class<EntityLiving> enigmothClass = (Class<EntityLiving>) EntityList.getClassFromName("mod_lavacow:enigmoth"); // 

			for (EntityPlayer player : getPlayers(world)) {

				if (enigmothClass == null || player.posY < 125)
					continue;

				int amount = world.getEntities(enigmothClass, e -> true).size();

				if (amount < 6) {
					double r = Math.random() * 5 + 20;
					double theta = Math.random() * Math.PI * 2;

					double xOff = Math.cos(theta) * r;
					double zOff = Math.sin(theta) * r;

					int x = (int) Math.round(player.posX + xOff);
					int z = (int) Math.round(player.posZ + zOff);

					int y = (int) Math.round(player.posY + Math.random() * 25);

					BlockPos pos = new BlockPos(x, y, z);

					if (!world.getPlayers(EntityPlayer.class, p -> p.getDistanceSq(pos) <= 20 * 20).isEmpty()) {
						continue;
					}

					EntityLiving moth = createEntity(world, pos, enigmothClass);

					boolean isSpawnValid = true;

					for (int i = -1; i <= 1 && isSpawnValid; i++) {
						for (int j = -1; j <= 1 && isSpawnValid; j++) {
							for (int k = -1; k <= 1 && isSpawnValid; k++) {
								if (!world.isAirBlock(pos.add(i, j, k))) {
									isSpawnValid = false;
								}
							}
						}
					}

					if (isSpawnValid) {
						int packSize = world.rand.nextInt(2) + 2;
						for (int i = 0; i < packSize; i++) {
							moth.onInitialSpawn(world.getDifficultyForLocation(pos), null);
							moth.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 100));
							world.spawnEntity(moth);
							
							moth = createEntity(world, pos, enigmothClass);
						}
					}
				}
			}
		}
		
		
		if (world.getTotalWorldTime() % 20 == 0 && world.rand.nextInt(30) == 0) {

			@SuppressWarnings("unchecked")
			Class<EntityLiving> ghostrayClass = (Class<EntityLiving>) EntityList.getClassFromName("mod_lavacow:ghostray");//

			for (EntityPlayer player : getPlayers(world)) {

				if (ghostrayClass == null)
					continue;

				int amount = world.getEntities(ghostrayClass, e -> true).size();

				if (amount < 9) {
					double r = Math.random() * 5 + 20;
					double theta = Math.random() * Math.PI * 2;

					double xOff = Math.cos(theta) * r;
					double zOff = Math.sin(theta) * r;

					int x = (int) Math.round(player.posX + xOff);
					int z = (int) Math.round(player.posZ + zOff);

					int y = (int) Math.round(player.posY + Math.random() * 25);

					BlockPos pos = new BlockPos(x, y, z);

					if (!world.getPlayers(EntityPlayer.class, p -> p.getDistanceSq(pos) <= 20 * 20).isEmpty()) {
						continue;
					}

					EntityLiving ray = createEntity(world, pos, ghostrayClass);

					boolean isSpawnValid = world.canSeeSky(pos);

					for (int i = -1; i <= 1 && isSpawnValid; i++) {
						for (int j = -1; j <= 1 && isSpawnValid; j++) {
							for (int k = -1; k <= 1 && isSpawnValid; k++) {
								if (!world.isAirBlock(pos.add(i, j, k))) {
									isSpawnValid = false;
								}
							}
						}
					}

					if (isSpawnValid) {
						int packSize = world.rand.nextInt(2) + 3;
						
						for (int i = 0; i < packSize; i++) {
							ray.onInitialSpawn(world.getDifficultyForLocation(pos), null);
							ray.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 100));
							world.spawnEntity(ray);
							
							ray = createEntity(world, pos, ghostrayClass);
						}
					}
				}
			}
		}
	}

	private static EntityLiving createEntity(World world, BlockPos pos, Class<EntityLiving> entity) {
		try {
			EntityLiving creature = entity.getConstructor(World.class).newInstance(world);
			float yaw = world.rand.nextFloat() * 360.0F;
			creature.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yaw, 0.0F);
			return creature;
		} catch (Exception e) {
			throw new RuntimeException("Failed to spawn entity " + entity.getClass(), e);
		}
	}

	private static List<EntityPlayer> getPlayers(World world) {
		return world.playerEntities.stream().filter(player -> !player.isSpectator()).collect(Collectors.toList());
	}
}
