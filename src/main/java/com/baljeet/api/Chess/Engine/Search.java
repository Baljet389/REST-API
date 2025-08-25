package com.baljeet.api.Chess.Engine;

import com.baljeet.api.Chess.Core.Board;
import com.baljeet.api.Chess.Core.MoveGeneration;
import com.baljeet.api.Chess.Core.MoveList;
import com.baljeet.api.Chess.Core.Piece;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Search {

    private final MoveGeneration moveGeneration;
    private final Board board;
    private final Evaluation evaluation;

    private int searchDepth;
    private long start;
    private long timeForMove;
    private long nodesSearched;
    private boolean searchCancelled;
    private int optimalMoveIteration;
    public int optimalMove;
    public int eval;
    private TranspositionTable.TTEntry entry;
    private final ArrayList<Long> gameHistory;
    private static final Logger logger = LoggerFactory.getLogger(Search.class);

    private final OpeningDatabase openingDatabase;

    TranspositionTable tt;
    private final int tableSize = 5000000;
    private static final byte EXACT = 0;
    private static final byte LOWER_BOUND = 1;
    private static final byte UPPER_BOUND = 2;

    public Search(Board board, MoveGeneration moveGeneration, ArrayList<Long> gameHistory) {
        this.board = board;
        this.moveGeneration = moveGeneration;
        this.gameHistory = gameHistory;
        evaluation = new Evaluation(board);
        tt = new TranspositionTable(tableSize);
        openingDatabase = new OpeningDatabase(board);
    }

    public int iterativeDeepening(long time, long increment) {
        if (board.fullMoveNumber < 10) {
            int move = openingDatabase.lookupPosition(moveGeneration);
            if (move != 0) {
                optimalMove = move;
                return 0;
            }
        }

        logger.debug("******* SEARCH STARTED ********");
        logger.debug("FEN: {}", board);
        boolean mateFound = false;
        searchCancelled = false;
        nodesSearched = 0;

        timeForMove = chooseTimeForMove(time, increment);
        start = System.currentTimeMillis();
        logger.debug("Allocated time: {}", timeForMove);

        for (int i = 1; i < 20; i++) {
            searchDepth = i;
            int evalIteration = negaMax(Integer.MIN_VALUE / 2, Integer.MAX_VALUE / 2, searchDepth);
            if (searchCancelled) {
                break;
            } else {
                optimalMove = optimalMoveIteration;
                eval = evalIteration;
            }
            logger.debug("Depth: {} Eval: {} Best Move: {}", i, eval, MoveList.moveToString(optimalMoveIteration));

            if (eval > 90000) {
                logger.debug("Mate in {} found", i - 1);
                break;
            }


        }
        logger.debug("Mate Found: {} Search cancelled: {}", mateFound, searchCancelled);
        logger.debug("Time used [ms]: {}", (System.currentTimeMillis() - start));
        logger.debug("Time remaining [ms]: {}", (timeForMove - System.currentTimeMillis() + start));
        logger.debug("Nodes searched: {}", nodesSearched);
        logger.debug("******* SEARCH ENDED ********");
        return eval;

    }

    private int negaMax(int alpha, int beta, int depth) {
        if (searchCancelled) return 0;
        nodesSearched++;
        // Check every 1024 nodes if time is up
        if ((nodesSearched & 0x3FF) == 0 && System.currentTimeMillis() - start > timeForMove) {
            searchCancelled = true;
            return 0;
        }
        // Check for fifty move rule
        if (board.halfMoveClock >= 50) return 0;
        //Add position to repetition history
        //Check for repetition if half move clock is at least 4 because else draw by repetition is not possible
        if (board.halfMoveClock >= 4 && checkForRepetition()) return 0;
        // Check if evaluated position is already in TT
        entry = tt.lookup(board.zobristHash);
        if (entry != null && entry.depth >= depth && depth != searchDepth) {
            if (entry.boundType == EXACT) return entry.score;
            else if (entry.boundType == LOWER_BOUND && entry.score >= beta) return entry.score;
            else if (entry.boundType == UPPER_BOUND && entry.score <= alpha) return entry.score;
        }
        // Static evaluation
        if (depth == 0) return evaluation.evaluate();
        int max = Integer.MIN_VALUE / 2;

        MoveList moveList = moveGeneration.getAllMoves();
        // Checkmate and stalemate
        if (moveList.isEmpty()) {
            if (moveGeneration.check) {
                return -100000 + 100 * (searchDepth - depth);
            }
            return 0;
        }
        sortMoves(moveList, depth);

        int initialAlpha = alpha;
        int bestMove = 0;
        for (int i = 0; i < moveList.size(); i++) {
            int move = moveList.get(i);
            board.makeMove(move);
            gameHistory.add(board.zobristHash);
            int score = -negaMax(-beta, -alpha, depth - 1);
            gameHistory.remove(gameHistory.size() - 1);
            board.undoMove(move);

            if (score > max) {
                max = score;
                bestMove = move;
                if (depth == searchDepth) {
                    optimalMoveIteration = move;
                }
                alpha = Math.max(max, alpha);
            }
            if (score >= beta) {
                return score;
            }

        }
        byte bound;
        if (max <= initialAlpha) {
            bound = UPPER_BOUND;
        } else if (max >= beta) {
            bound = LOWER_BOUND;
        } else {
            bound = EXACT;
        }
        if (!searchCancelled) tt.store(board.zobristHash, bestMove, max, depth, bound, board.fullMoveNumber);
        return max;
    }

    private long chooseTimeForMove(long timeLeft, long increment) {

        long buffer = 100;
        long base = (timeLeft - buffer) / 30;

        long timeForMove = base + increment / 2;

        //Never use more than 10 seconds
        return Math.min(timeForMove, 10000);
    }

    // Move ordering
    private void sortMoves(MoveList moveList, int depth) {
        // First: PV-Move from prev iteration
        // Second: Move from Transposition Table
        // Third: Captures
        // Fourth: Normal Moves
        int[] scores = new int[moveList.size()];
        entry = tt.lookup(board.zobristHash);

        for (int i = 0; i < moveList.size(); i++) {
            int move = moveList.get(i);
            if (depth == searchDepth && move == optimalMove) {
                scores[i] = 1000;
            } else if (entry != null && entry.bestMove == move) {
                scores[i] = 999;
            } else if (MoveList.getFlag(move) == Piece.CAPTURE) {
                int to = MoveList.getTo(move);
                scores[i] = EvaluationData.PieceWeights[board.currentPosition[to]];
            } else {
                scores[i] = 0;
            }

        }
        for (int i = 0; i < moveList.size() - 1; i++) {
            int maxIndex = i;

            for (int j = i + 1; j < moveList.size(); j++) {
                if (scores[j] > scores[maxIndex]) {
                    maxIndex = j;
                }
            }
            int tempScore = scores[i];
            scores[i] = scores[maxIndex];
            scores[maxIndex] = tempScore;

            int tempMove = moveList.get(i);
            moveList.set(i, moveList.get(maxIndex));
            moveList.set(maxIndex, tempMove);
        }

    }

    private boolean checkForRepetition() {
        long currentHash = board.zobristHash;
        int count = 0;

        // Only look back as far as the half-move clock allows
        int limit = Math.min(gameHistory.size(), board.halfMoveClock + 1);

        for (int i = gameHistory.size() - limit; i < gameHistory.size(); i++) {
            if (gameHistory.get(i) == currentHash) {
                count++;
                if (count >= 3) {
                    return true;
                }
            }
        }
        return false;
    }
}
