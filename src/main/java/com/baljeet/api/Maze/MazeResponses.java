package com.baljeet.api.Maze;


import java.util.ArrayList;
import java.util.List;

public class MazeResponses {
     static class GenerateResponse{
        public int position;
        public boolean[] walls;
        GenerateResponse(int position,boolean[] walls){
            this.walls=walls;
            this.position=position;
        }
        public static List<GenerateResponse> respond(Cell[] cells, int width){
            List<GenerateResponse> response = new ArrayList<>();
            for(Cell i : cells){
              if ((i.position % width +i.position / width) % 2 == 0){
                response.add(new GenerateResponse(i.position,i.walls));
              }
          }
            return response;
        }
    }
     static class SolveResponse{
        public boolean fastestPath;
        public int position;
        SolveResponse(boolean fastestPath,int position){
            this.fastestPath=fastestPath;
            this.position=position;
        }
        public static SolveResponse[] respond(Cell[] cells, int height, int width){
            SolveResponse[] response = new SolveResponse[height*width];
            for(Cell i : cells){
                response[i.position] = new SolveResponse(i.fastestPath,i.position);
            }
            return response;
        }
    }

}
