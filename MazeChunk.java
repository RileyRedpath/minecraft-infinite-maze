package com.riley.maze;

import jline.internal.Nullable;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Riley on 10/12/2017.
 */
class MazeChunk {

    private List<List<Cell>> cells;
    private MazeChest chest;
    private final int size;

    MazeChunk(Random random, int size) {
        this.size = size;
        cells = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            cells.add(new ArrayList<>());
            for (int z = 0; z < size; z++) {
                cells.get(x).add(new Cell(x, z));
            }
        }

        chest = new MazeChest(random, random.nextInt(size), random.nextInt(size));
    }

    void createXDoor(Random random){
        int xDoor = random.nextInt(size);
        cells.get(size -1).get(xDoor).xWall = false;
    }

    void createZDoor(Random random){
        int zDoor = random.nextInt(size);
        cells.get(zDoor).get(size -1).zWall = false;
    }

    @Nullable
    Cell getCell(int x, int z) {
        if (x > -1 && x < size && z > -1 && z < size) {
            return cells.get(x).get(z);
        } else {
            return null;
        }
    }

    private void removeWall(Cell cellA, Cell cellB) {
        //removes the wall inbetween cellA and cellB
        //if cellA and cellB aren't adjacent, does nothing
        int xDiff = cellA.x - cellB.x;
        int zDiff = cellA.z - cellB.z;
        if (xDiff == -1 && zDiff == 0) {
            /*
                    +z
            +x  cellB cellA

             */
            cellA.xWall = false;
        } else if (xDiff == 1 && zDiff == 0) {
            /*
                    +z
            +x  cellA cellB

             */
            cellB.xWall = false;
        } else if (xDiff == 0 && zDiff == -1) {
            /*
                   +z
            +x   cellB
                 cellA

             */
            cellA.zWall = false;
        } else if (xDiff == 0 && zDiff == 1) {
            /*
                   +z
            +x   cellA
                 cellB

             */
            cellB.zWall = false;
        }
    }

    @Nullable
    private Cell move(Cell cell, Random random) {
        //move to a random unvisited adjacent cell
        List<Cell> neighbours = new ArrayList<>();
        List<Cell> unvisitedNeighbours = new ArrayList<>();
        neighbours.add(getCell(cell.x + 1, cell.z));
        neighbours.add(getCell(cell.x - 1, cell.z));
        neighbours.add(getCell(cell.x, cell.z + 1));
        neighbours.add(getCell(cell.x, cell.z - 1));

        for (Cell adjCell : neighbours) {
            if (adjCell != null && !adjCell.visited) {
                unvisitedNeighbours.add(adjCell);
            }
        }

        if (unvisitedNeighbours.size() == 0) {
            //calling random last is probably best as this case happens often
            //return null if no legal moves are available
            return null;
        } else {
            return unvisitedNeighbours.get(random.nextInt(unvisitedNeighbours.size()));
        }
    }

    void recursiveBacktrackMaze(Random random) {
        Stack<Cell> cellStack = new Stack<>();
        Cell currentCell = getCell(0, 0);
        currentCell.visited = true;
        Cell nextCell;
        while (true) {
            nextCell = move(currentCell, random);
            if (nextCell != null) {
                // legal move
                removeWall(currentCell, nextCell);
                nextCell.visited = true;
                cellStack.push(currentCell);
                currentCell = nextCell;
            } else {
                // deadend, go back
                if (cellStack.isEmpty()) {
                    break;
                } else {
                    currentCell = cellStack.pop();
                }
            }
        }
    }

    void construct(World world, int chunkX, int chunkZ) {
        // constructs the maze in the Minecraft world

        // +8 prevents chunk overflow
        int xOrg = chunkX * 16 + 8;
        int zOrg = chunkZ * 16 + 8;
        int cellSize = 16/size;

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                Cell cell = getCell(x, z);
                cell.construct(world, cellSize, cellSize * x + xOrg, cellSize *z + zOrg, Blocks.OBSIDIAN.getDefaultState());
            }
        }

        chest.createChest(world, xOrg, zOrg, cellSize);
    }
}
