package com.riley.maze;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Riley on 10/12/2017.
 */
class MazeChest {
    private List<ItemStack> contents;
    private int x;
    private int z;
    private int[] swordIDs = {268, 272, 267, 283, 276};

    MazeChest(Random random, int x, int z) {
        this.x = x;
        this.z = z;
        contents = new ArrayList<>();
        contents.add(new ItemStack(Item.getItemById(264), 1));// Diamond, score counter
        contents.add(new ItemStack(Item.getItemById(50), 16));// torches
        contents.add(new ItemStack(Item.getItemById(364), 1));// food

        int swordId = getSwordID(random);
        contents.add(new ItemStack(Item.getItemById(swordId), 1));

        if(getMap(random)){
            contents.add(new ItemStack(Item.getItemById(395)));// map
        }
    }

    MazeChest(int x, int z){
        this.x = x;
        this.z = z;
        contents = new ArrayList<>();

        contents.add(new ItemStack(Item.getItemById(50), 64));// torches
        contents.add(new ItemStack(Item.getItemById(364), 10));// food
        contents.add(new ItemStack(Item.getItemById(268), 1));// sword
        contents.add(new ItemStack(Item.getItemById(355), 1));// bed
        contents.add(new ItemStack(Item.getItemById(395), 1));// map
    }

    private int getSwordID(Random random) {
        int roll = random.nextInt(100);
        if (roll < 33) {
            return swordIDs[0];
        } else if (roll < 66) {
            return swordIDs[1];
        } else if (roll < 87) {
            return swordIDs[2];
        } else if (roll < 95) {
            return swordIDs[3];
        } else {
            return swordIDs[4];
        }
    }

    private boolean getMap(Random random){
        int roll = random.nextInt(100);
        if(roll == 0){
            return true;
        }else{
            return false;
        }
    }

    void createChest(World world, int xOrg, int zOrg) {
        BlockPos chestPos = new BlockPos(xOrg + 2 * x, 4, zOrg + 2 * z);
        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState());
        TileEntityChest chestEntity = (TileEntityChest) world.getTileEntity(chestPos);

        if (chestEntity == null) {
            return;
        }

        for (int i = 0; i < contents.size(); i++) {
            chestEntity.setInventorySlotContents(i, contents.get(i));
        }
    }
}
