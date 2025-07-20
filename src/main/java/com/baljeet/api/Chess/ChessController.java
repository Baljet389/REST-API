package com.baljeet.api.Chess;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projects/chess")
public class ChessController {
    Game game;

    @PutMapping("/start")
    public void startGame(ChessRequests.StartGameRequest request){
        String fen = request.fen;
        boolean engine = request.engine;
        game = new Game(fen,engine);

    }
    @GetMapping("/getMoves")
    public ResponseEntity<ChessResponses.getMovesResponse> getMoves(ChessRequests.getAllMovesRequest request){
        if(game == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(game.getMoves(request.square));
    }

    @PostMapping("/makeMove")
    public ResponseEntity<ChessResponses.makeMoveResponse> makeMove(ChessRequests.makeMove request){
        if(game == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(game.makeMove(request.move));
    }

}
