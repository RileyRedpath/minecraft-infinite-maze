package com.riley.maze;

import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;


/**
 * Created by Riley on 10/12/2017.
 */
public class WorldLoadListener {

    private MazeGen mazeGen;

    WorldLoadListener(MazeGen mazeGen){
        this.mazeGen = mazeGen;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        World world = event.getWorld();
        if(world.provider.getDimension() == 0) {
            world.setSpawnPoint(new BlockPos(16,4,16));
            MazeChest initialChest = new MazeChest(3,3);
            initialChest.createChest(world,8,8);
            mazeGen.makeZoomedMazeChunks(new Random(world.getSeed()));
        }



    }
}
