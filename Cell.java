package com.riley.maze;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Riley on 10/12/2017.
 */
class Cell {
    int x;
    int z;
    boolean visited;
    boolean xWall;
    boolean zWall;

    Cell(int x, int z) {
        this.x = x;
        this.z = z;
        this.visited = false;
        this.xWall = true;
        this.zWall = true;
    }

    void construct(World world, int size, int xWorld, int zWorld, IBlockState blockType){
        /*
        We want to make a cell like this, expanded so the cell is a size*size square:
                    z+
            W   zW ..

        +x  xW  S ..
            .   .
            .   .

        xWorld and zWorld are the world coordinates of the x-,z- corner
        size is the size of the cell in minecraft blocks
         */

        //x+,z+ corner
        fillXZ(world, xWorld + size, zWorld + size, blockType);

        //+x Wall
        if(xWall){
            for(int i = 1; i < size; i++){
                fillXZ(world, xWorld + size, zWorld + i, blockType);
            }
        }

        //+z Wall
        if(zWall){
            for(int i = 1; i < size; i++){
                fillXZ(world, xWorld + i, zWorld + size, blockType);
            }
        }
    }

    private void fillXZ(World world, int x, int z, IBlockState blockType) {
        for (int y = 4; y < 19; y++) {
            world.setBlockState(new BlockPos(x, y, z), blockType);
        }
    }
}
