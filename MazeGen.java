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
    private MazeChunkFactory mazeChunkFactory;

    MazeGen(){
        mazeChunkFactory = new MazeChunkFactory(8);
    }


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
         if(world.provider.getDimension() == 0) {

             if(!(chunkX == 0 && chunkZ == 0)){
                 MazeChunk mazeChunk = mazeChunkFactory.getNewMazeChunk(random);
                 mazeChunk.recursiveBacktrackMaze(random);
                 if(zoomedChunk == null){
                     mazeChunk.createXDoor(random);
                     mazeChunk.createZDoor(random);
                 }else {
                     Cell chunkCell = zoomedChunk.getCell(Math.floorMod(chunkX,8),Math.floorMod(chunkZ,8));
                     if(!chunkCell.xWall){
                         mazeChunk.createXDoor(random);
                     }
                     if(!chunkCell.zWall){
                         mazeChunk.createZDoor(random);
                     }
                 }
                 mazeChunk.construct(world, chunkX, chunkZ);
             }

         }

    }

    void makeZoomedMazeChunks(Random zoomedOutRNG){
        zoomedChunk = mazeChunkFactory.getNewMazeChunk(zoomedOutRNG,200);
        zoomedChunk.recursiveBacktrackMaze(zoomedOutRNG);
        zoomedChunk.createZDoor(zoomedOutRNG);
        zoomedChunk.createXDoor(zoomedOutRNG);
    }


}
