package com.riley.maze;

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
}
