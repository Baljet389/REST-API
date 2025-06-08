package com.projects.Projects.Website;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cell {
    public int position;//0- width*height-1
    public boolean []walls = new boolean[4];//false = wall; true = no wall
    public boolean fastestPath = false;
    @JsonIgnore
    boolean visitedGenerate = false;
    @JsonIgnore
    boolean visitedSolve = false;
    @JsonIgnore
    int referenced;
    @JsonIgnore
    int cost;
    Cell(int position) {
        this.position = position;

    }
    boolean activeWalls(int i) {
        return walls[i];
    }
    int position() {
        return position;
    }
    void carvePassage(int x) {
        visitedGenerate = true;
        switch(x) {
            case(0):
                walls[0] = true;
                return;
            case(1):
                walls[1] = true;
                return;
            case(2):
                walls[2] = true;
                return;
            case(3):
                walls[3] = true;
        }


    }
    void cost(int y) {
        cost = cost + y;
    }
    int getcost() {
        return cost;
    }
    int GetReferenceCell() {
        return referenced;
    }
    void SetReferenceCell(int referenced) {
        this.referenced = referenced;
    }
}
