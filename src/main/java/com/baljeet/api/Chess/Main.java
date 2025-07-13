package com.baljeet.api.Chess;

import com.baljeet.api.Chess.Core.Board;
import com.baljeet.api.Chess.Core.MoveList;

import java.util.ArrayList;

public class Main {
    public Board board;
    public ArrayList<MoveList> moves;

    public Main(){
        //board = new Board();
        moves = new ArrayList<>();
    }
    public void getPossibleMoves(){

            //getMovesPawn(moves,board);

    }
    public void printMoves(){
        for(MoveList move : moves){
            System.out.println(move.toString());
        }
    }

}
