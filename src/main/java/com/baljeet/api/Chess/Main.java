package com.baljeet.api.Chess;

import java.util.ArrayList;

public class Main {
    public Board board;
    public ArrayList<Move> moves;

    public Main(){
        //board = new Board();
        moves = new ArrayList<>();
    }
    public void getPossibleMoves(){

            //getMovesPawn(moves,board);

    }
    public void printMoves(){
        for(Move move : moves){
            System.out.println(move.toString());
        }
    }

}
