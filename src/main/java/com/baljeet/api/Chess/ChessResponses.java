package com.baljeet.api.Chess;


import java.util.ArrayList;

public class ChessResponses {
    public static class getMovesResponse{
        public ArrayList<Integer> moves;
    }
    public static class gameState{
        public String fen;
        public boolean check;
        public boolean checkmate;
        public boolean draw;
    }
}
