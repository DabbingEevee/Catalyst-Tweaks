package com.existingeevee.catatweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import mcjty.lostcities.LostCities;
import mcjty.lostcities.config.LostCityProfile;
import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.LostWoodlandMansion;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

@Mixin(value = LostCityChunkGenerator.class, remap = false)
public abstract class MixinLostCityChunkGenerator implements IChunkGenerator {

	@Shadow private MapGenStronghold strongholdGenerator;
	@Shadow private StructureOceanMonument oceanMonumentGenerator;
	@Shadow private MapGenVillage villageGenerator;
	@Shadow private MapGenMineshaft mineshaftGenerator;
	@Shadow private MapGenScatteredFeature scatteredFeatureGenerator;
	@Shadow private LostWoodlandMansion woodlandMansionGenerator;
	@Shadow private Biome[] biomesForGeneration;
	@Shadow private MapGenBase caveGenerator;
	@Shadow private MapGenBase ravineGenerator;
	
	/*@Overwrite
	public Chunk generateChunk(int chunkX, int chunkZ) {
		return this.generateChunkCustom(chunkX, chunkZ);
	}*/
	
	/**
	 * @author Eevee
	 * @reason custom handling
	 */
	@Overwrite
	public Chunk func_185932_a(int chunkX, int chunkZ) {
		return this.generateChunkCustom(chunkX, chunkZ);
	}
 
	@Unique
	public Chunk generateChunkCustom(int chunkX, int chunkZ) {
		LostCityChunkGenerator $this = (LostCityChunkGenerator) (Object) this;

		LostCityProfile profile = BuildingInfo.getProfile(chunkX, chunkZ, $this);

		$this.terrainGenerator.setupChars(profile);
		boolean isCity = BuildingInfo.isCity(chunkX, chunkZ, $this);

		ChunkPrimer chunkprimer = $this.getChunkPrimer(chunkX, chunkZ, isCity);

		BiomeProvider biomeProvider = $this.worldObj.getBiomeProvider();
		this.biomesForGeneration = biomeProvider.getBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
		$this.terrainGenerator.replaceBlocksForBiome(chunkX, chunkZ, chunkprimer, this.biomesForGeneration);
		
		try {
			$this.terrainGenerator.generate(chunkX, chunkZ, chunkprimer);
		} catch (Exception e) {
			LostCities.setup.getLogger().error("An exception occured while generating chunk: " + chunkX + "," + chunkZ, e);
			BuildingInfo info = BuildingInfo.getBuildingInfo(chunkX, chunkZ, $this);
			LostCities.setup.getLogger().error("    Chunk profile: " + info.profile.getName());
			LostCities.setup.getLogger().error("    Is City: " + info.isCity());
			LostCities.setup.getLogger().error("    Building type: " + info.getBuildingType());
			LostCities.setup.getLogger().error("    City level: " + info.getCityLevel());
			LostCities.setup.getLogger().error("    City ground level: " + info.getCityGroundLevel());
			LostCities.setup.getLogger().error("    Num floors: " + info.getNumFloors());
			LostCities.setup.getLogger().error("    Num cellars: " + info.getNumCellars());
			throw (e);
		}

		if (profile.GENERATE_CAVES) {
			this.caveGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
		}
		if (profile.GENERATE_RAVINES) {
			if (!profile.PREVENT_LAKES_RAVINES_IN_CITIES || !isCity) {
				this.ravineGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
			}
		}

		if (profile.GENERATE_MINESHAFTS) {
			this.mineshaftGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
		}

		if (profile.GENERATE_VILLAGES) {
			if (profile.PREVENT_VILLAGES_IN_CITIES) {
				if (!isCity) {
					this.villageGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
				}
			} else {
				this.villageGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
			}
		}

		if (profile.GENERATE_STRONGHOLDS) {
			this.strongholdGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
		}

		if (profile.GENERATE_SCATTERED) {
			this.scatteredFeatureGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
		}

		if (profile.GENERATE_OCEANMONUMENTS) {
			this.oceanMonumentGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
		}

		if (profile.GENERATE_MANSIONS) {
			this.woodlandMansionGenerator.generate($this.worldObj, chunkX, chunkZ, chunkprimer);
		}

		Chunk chunk = new Chunk($this.worldObj, chunkprimer, chunkX, chunkZ);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i) {
			abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();

		return chunk;
	}
}
