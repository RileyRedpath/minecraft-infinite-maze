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

    MazeChunk(Random random) {
        cells = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            cells.add(new ArrayList<>());
            for (int z = 0; z < 8; z++) {
                cells.get(x).add(new Cell(x, z));
            }
        }

        chest = new MazeChest(random, random.nextInt(8), random.nextInt(8));
    }

    void setXDoor(Random random){
        int xDoor = random.nextInt(8);
        cells.get(7).get(xDoor).xWall = false;
    }

    void setZDoor(Random random){
        int zDoor = random.nextInt(8);
        cells.get(zDoor).get(7).zWall = false;
    }

    @Nullable
    Cell getCell(int x, int z) {
        if (x > -1 && x < 8 && z > -1 && z < 8) {
            return cells.get(x).get(z);
        } else {
            return null;
        }
    }

    private void removeWall(Cell cellA, Cell cellB) {
        int xDiff = cellA.x - cellB.x;
        int zDiff = cellA.z - cellB.z;
        if (xDiff == -1 && zDiff == 0) {
            cellA.xWall = false;
        } else if (xDiff == 1 && zDiff == 0) {
            cellB.xWall = false;
        } else if (xDiff == 0 && zDiff == -1) {
            cellA.zWall = false;
        } else if (xDiff == 0 && zDiff == 1) {
            cellB.zWall = false;
        }
    }

    @Nullable
    private Cell move(Cell cell, Random random) {
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
                removeWall(currentCell, nextCell);
                nextCell.visited = true;
                cellStack.push(currentCell);
                currentCell = nextCell;
            } else {
                if (cellStack.isEmpty()) {
                    break;
                } else {
                    currentCell = cellStack.pop();
                }
            }
        }
    }

    void construct(World world, int chunkX, int chunkZ) {
        int xOrg = chunkX * 16 + 8;
        int zOrg = chunkZ * 16 + 8;
        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                Cell cell = getCell(x, z);
                fillXZ(world, xOrg + x * 2 + 1, zOrg + z * 2 + 1);
                if (cell.xWall) {
                    fillXZ(world, xOrg + x * 2 + 1, zOrg + z * 2);
                }
                if (cell.zWall) {
                    fillXZ(world, xOrg + x * 2, zOrg + z * 2 + 1);
                }
            }
        }
        chest.createChest(world, xOrg, zOrg);
    }

    private void fillXZ(World world, int x, int z) {
        for (int y = 4; y < 19; y++) {
            world.setBlockState(new BlockPos(x, y, z), Blocks.OBSIDIAN.getDefaultState());
        }
    }
}
