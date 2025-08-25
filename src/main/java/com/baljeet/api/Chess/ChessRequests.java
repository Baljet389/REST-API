package com.baljeet.api.Chess;

public class ChessRequests {
    public static class StartGameRequest{
        public boolean engine;
        public String fen;
    }
    public static class makeMove{
        public int move;
    }
    public static class engineMakeMove{
        //Time in milliseconds
        public long timeLeft;
        public long increment;
    }
}
