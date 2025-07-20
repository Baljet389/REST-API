package com.baljeet.api.Chess;

public class ChessRequests {
    public static class StartGameRequest{
        public boolean engine;
        public String fen;
    }
    public static class getAllMovesRequest{
        public int square;
    }
    public static class makeMove{
        public int move;
    }
}
