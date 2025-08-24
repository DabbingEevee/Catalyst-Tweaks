package com.existingeevee.catatweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPreeminent;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.existingeevee.catatweaks.events.*;
import energon.nebulalib.event.NLibEventHandler;
import energon.nebulalib.event.NLibEventTools;
import energon.nebulalib.event.test.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CatalystTweaks.MODID)
public class CatalystTweaks {
    public static final String MODID = "catalyst_tweaks";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        CatalystTweaksSounds.registerSounds();
        NLibEventTools.addEvent(1, "coth_0", "", NLibEventHandler.SIDE.PLAYER_TICK, NLibEventHandler.RARITY.COMMON, EVENT_Coth_FirstContact::new, new TEST_Delay(4), new TEST_EvoPhase(0, 0), new TEST_PlayerHasPotionEffect(SRPPotions.COTH_E));
        NLibEventTools.addEvent(2, "coth_1", "", NLibEventHandler.SIDE.PLAYER_TICK, NLibEventHandler.RARITY.COMMON, EVENT_Coth_Again::new, new TEST_Delay(4), new TEST_EvoPhase(1, 1), new TEST_PlayerHasPotionEffect(SRPPotions.COTH_E));
        NLibEventTools.addEvent(3, "coth_2", "", NLibEventHandler.SIDE.PLAYER_TICK, NLibEventHandler.RARITY.COMMON, EVENT_Coth_Another::new, new TEST_Delay(4), new TEST_EvoPhase(2, 2), new TEST_PlayerHasPotionEffect(SRPPotions.COTH_E));
        NLibEventTools.addEvent(4, "saw_beckon", "", NLibEventHandler.SIDE.PLAYER_TICK, NLibEventHandler.RARITY.COMMON, EVENT_Saw_Beckon::new, new TEST_PlayerLooksAtEntity(EntityPStationaryArchitect.class, 0.035));
        NLibEventTools.addEvent(5, "biome_enter", "", NLibEventHandler.SIDE.PLAYER_TICK, NLibEventHandler.RARITY.COMMON, EVENT_Biome_Enter::new, new TEST_Delay(4), new TEST_PlayerInBiome(SRPBiomes.biomeInfested));
        NLibEventTools.addEvent(6, "node_destroyed", "", NLibEventHandler.SIDE.PLAYER_INTERACT, NLibEventHandler.RARITY.RARE, EVENT_Node_Destroyed::new, new TEST_PlayerBreakBlock(SRPBlocks.BiomeHeart));
        NLibEventTools.addEvent(7, "city_exit", "", NLibEventHandler.SIDE.PLAYER_INTERACT, NLibEventHandler.RARITY.RARE, EVENT_City_Exit::new, new TEST_PlayerChangeDimension(0, 111));
        NLibEventTools.addEvent(8, "kill_preem", "", NLibEventHandler.SIDE.PLAYER_INTERACT, NLibEventHandler.RARITY.RARE, EVENT_Kill_Preem::new, new TEST_PlayerKillEntity(EntityPPreeminent.class));
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {

    }
}
