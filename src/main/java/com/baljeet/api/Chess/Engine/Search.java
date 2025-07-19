package com.baljeet.api.Chess.Engine;

import com.baljeet.api.Chess.Core.Board;
import com.baljeet.api.Chess.Core.MoveGeneration;
import com.baljeet.api.Chess.Core.MoveList;

public class Search {
    private final Board board;
    private final MoveGeneration moveGeneration;
    private int optimalMove;

    public int evaluation;

    Search(Board board){
        this.board = board;
        moveGeneration = new MoveGeneration(board);
    }
    public int getBestMove(){
        evaluation = negaMax(0,0,3);
        return  optimalMove;
    }

    private int negaMax(int alpha, int beta, int depth) {
        if (depth == 0) return 0;
        int max = Integer.MIN_VALUE;

        MoveList moveList = moveGeneration.getAllMoves();
        for (int i = 0; i < moveList.size(); i++) {
            int move = moveList.get(i);
            board.makeMove(move);
            int score = -negaMax(-beta, -alpha, depth - 1);
            board.undoMove(move);

            if (score > max) {
                optimalMove = move;
                max = score;
                if (score>alpha) alpha = score;
            }
            if (score >= beta){
                optimalMove = move;
                return  max;
            }
        }
        return max;
    }


}
