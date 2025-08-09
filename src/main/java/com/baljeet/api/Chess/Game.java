package com.baljeet.api.Chess;

import com.baljeet.api.Chess.Core.Board;
import com.baljeet.api.Chess.Core.MoveGeneration;
import com.baljeet.api.Chess.Core.MoveList;
import com.baljeet.api.Chess.Core.PrecomputedData;

import java.util.ArrayList;

public class Game {
    private final Board board;
    private final MoveGeneration moveGeneration;
    private boolean engine;

    public Game(String fen, boolean engine){
        new PrecomputedData();
        board = new Board(fen);
        moveGeneration = new MoveGeneration(board);
        this.engine = engine;

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
        return response;
    }

}
