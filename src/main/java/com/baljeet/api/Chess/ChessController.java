package com.baljeet.api.Chess;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("api/projects/chess")
public class ChessController {
    Game game;

    @PutMapping("/start")
    public ResponseEntity<Map<String,String>> startGame(@RequestBody ChessRequests.StartGameRequest request){
        String fen = request.fen;
        boolean engine = request.engine;
        game = new Game(fen,engine);
		return ResponseEntity.ok(Map.of("Status","Game Started successfully!"));

    }
    @GetMapping("/getMoves")
    public ResponseEntity<ChessResponses.getMovesResponse> getMoves(@RequestBody ChessRequests.getAllMovesRequest request){
        if(game == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(game.getMoves(request.square));
    }

    @PostMapping("/makeMove")
    public ResponseEntity<ChessResponses.makeMoveResponse> makeMove(@RequestBody ChessRequests.makeMove request){
        if(game == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(game.makeMove(request.move));
    }

}
