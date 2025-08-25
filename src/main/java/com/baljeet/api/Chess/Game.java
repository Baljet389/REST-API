package com.baljeet.api.Chess;

import com.baljeet.api.Chess.Core.*;
import com.baljeet.api.Chess.Engine.*;


import java.util.ArrayList;

public class Game {
    private final Board board;
    private final MoveGeneration moveGeneration;
    private final Engine engine;
    ArrayList<Long> repetitionTable;

    public Game(String fen){
        new PrecomputedData();
        board = new Board(fen);
        moveGeneration = new MoveGeneration(board);
        repetitionTable = new ArrayList<>();
        engine = new BaljeetEngine(board, repetitionTable);
    }
    public ChessResponses.gameState startGame(){
        ChessResponses.gameState response = new ChessResponses.gameState();
        MoveList moveList = moveGeneration.getAllMoves();
        if (moveList.isEmpty()){
            if (moveGeneration.check) response.checkmate = true;
            else response.draw = true;
        }
        if (moveGeneration.check) response.check = true;
        response.fen = board.toString();
        repetitionTable.add(board.zobristHash);
        return response;
    }
    public ChessResponses.getMovesResponse getMoves(int square){
        ChessResponses.getMovesResponse response = new ChessResponses.getMovesResponse();
        MoveList moveList = moveGeneration.getAllMoves();
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < moveList.size(); i++) {
            if (MoveList.getFrom(moveList.get(i)) == square) {
                list.add(moveList.get(i));
            }
        }
        response.moves = list;
        return response;
    }
    public ChessResponses.gameState makeMove(int move){
        ChessResponses.gameState response = new ChessResponses.gameState();
        board.makeMove(move);
        MoveList moveList = moveGeneration.getAllMoves();
        if (moveList.isEmpty()){
            if (moveGeneration.check) response.checkmate = true;
            else response.draw = true;
        }
        if (moveGeneration.check) response.check = true;
        response.fen = board.toString();
        repetitionTable.add(board.zobristHash);
        if(isRepetition(board.zobristHash) || board.halfMoveClock >= 50) response.draw = true;
        return response;
    }
    public ChessResponses.gameState makeEngineMove(long timeLeft,long increment){

        int move = engine.getBestMove(timeLeft,increment);
        return makeMove(move);
    }
    private boolean isRepetition(long currentKey){
        int count = 0;
        for (long key: repetitionTable) {
            if (key == currentKey) {
                count++;
                if (count == 3) return true;
            }
        }
        return false;
    }

}
