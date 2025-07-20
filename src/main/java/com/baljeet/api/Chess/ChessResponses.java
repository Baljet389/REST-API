package com.baljeet.api.Chess;

import com.baljeet.api.Chess.Core.MoveList;

import java.util.ArrayList;

public class ChessResponses {
    public static class getMovesResponse{
        public ArrayList<Integer> moves;
    }
    public static class makeMoveResponse{
        public String fen;
        public boolean check;
        public boolean checkmate;
        public boolean draw;
    }
}
