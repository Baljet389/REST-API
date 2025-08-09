package com.baljeet.api.Chess;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projects/chess")
public class ChessController {
    Game game;

    @PutMapping("/start")
    public ResponseEntity<ChessResponses.gameState> startGame(@RequestBody ChessRequests.StartGameRequest request){
        String fen = request.fen;
        boolean engine = request.engine;
        game = new Game(fen,engine);
		return ResponseEntity.ok(game.startGame());
    }
    @GetMapping("/getMoves")
    public ResponseEntity<ChessResponses.getMovesResponse> getMoves(@RequestParam String square){
        if(game == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(game.getMoves(Integer.parseInt(square)));
    }

    @PostMapping("/makeMove")
    public ResponseEntity<ChessResponses.gameState> makeMove(@RequestBody ChessRequests.makeMove request){
        if(game == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(game.makeMove(request.move));
    }

}
