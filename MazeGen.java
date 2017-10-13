package com.riley.maze;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.*;

/**
 * Created by Riley on 10/11/2017.
 */
public class MazeGen implements IWorldGenerator {

    private MazeChunk zoomedChunk;


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
         if(world.provider.getDimension() == 0) {

             if(!(chunkX == 0 && chunkZ == 0)){
                 MazeChunk mazeChunk = new MazeChunk(random);
                 mazeChunk.recursiveBacktrackMaze(random);
                 if(zoomedChunk == null){
                     mazeChunk.setXDoor(random);
                     mazeChunk.setZDoor(random);
                 }else {
                     Cell chunkCell = zoomedChunk.getCell(Math.floorMod(chunkX,8),Math.floorMod(chunkZ,8));
                     if(!chunkCell.xWall){
                         mazeChunk.setXDoor(random);
                     }
                     if(!chunkCell.zWall){
                         mazeChunk.setZDoor(random);
                     }
                 }
                 mazeChunk.construct(world, chunkX, chunkZ);
                 if(Math.abs(chunkX) < 3 && Math.abs(chunkZ) < 3) {
                     //mazeChunk.spawnMobs(world, random, chunkX * 16 + 8, chunkZ * 16 + 8);
                 }
             }

         }

    }

    void makeZoomedMazeChunks(Random zoomedOutRNG){
        zoomedChunk = new MazeChunk(zoomedOutRNG);
        zoomedChunk.recursiveBacktrackMaze(zoomedOutRNG);
        zoomedChunk.setZDoor(zoomedOutRNG);
        zoomedChunk.setXDoor(zoomedOutRNG);
    }


}
