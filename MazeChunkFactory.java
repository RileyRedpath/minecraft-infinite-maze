package com.riley.maze;

import java.util.Random;

/**
 * Created by Riley on 10/15/2017.
 */
public class MazeChunkFactory {

    //Main reason to have this is so we can pull numCells once from the config and to make it easy to implement pooling in the future

    private int numCells;

    MazeChunkFactory(int numCells){
        this.numCells = numCells;
    }

    MazeChunk getNewMazeChunk(Random random){
        return getNewMazeChunk(random, this.numCells);
    }

    MazeChunk getNewMazeChunk(Random random, int numCells){
        return new MazeChunk(random, numCells);
    }
}
