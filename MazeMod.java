package com.riley.maze;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Riley on 10/11/2017.
 */

@Mod(modid="maze",name = "maze",version = "1.0")
public class MazeMod {

    @Mod.EventHandler
    public void load(FMLInitializationEvent event){
        MazeGen generator = new MazeGen();
        GameRegistry.registerWorldGenerator(generator, 10);
        MinecraftForge.EVENT_BUS.register(new WorldLoadListener(generator));
        MinecraftForge.EVENT_BUS.register(new MobSpawnListener());
    }
}
