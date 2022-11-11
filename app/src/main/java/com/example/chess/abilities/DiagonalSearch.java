package com.example.chess.abilities;

import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public interface DiagonalSearch {
    public ArrayList<Position> diagonalSearch(Board board);
}
