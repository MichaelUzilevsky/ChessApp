package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Bishop extends ChessPiece implements DiagonalSearch {

    public Bishop(Position position, Color color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> diagonalSearch() {
        return null;
    }
}
