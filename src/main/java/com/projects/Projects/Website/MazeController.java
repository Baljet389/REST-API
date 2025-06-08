package com.projects.Projects.Website;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/projects/maze")
public class MazeController {
    private Maze currentMaze = null;
    static class GenerateMazeRequest {
        public int width;
        public int height;
    }

    @PostMapping("/generate")
    public ResponseEntity<Cell[]> postMaze(@RequestBody GenerateMazeRequest request) {
        if (request.width <= 0 || request.height <= 0) {
            return ResponseEntity.badRequest().build();
        }
        int width = request.width;
        int height = request.height;
        currentMaze = new Maze(height,width);
        return ResponseEntity.ok(currentMaze.cell);
    }
    @PostMapping("/solve")
    public ResponseEntity<Cell[]> getMaze() {
        if (currentMaze == null) {
            throw new IllegalStateException("Maze not yet submitted.");
        }
        currentMaze.solveMaze();
        return ResponseEntity.ok(currentMaze.cell);
    }

}
