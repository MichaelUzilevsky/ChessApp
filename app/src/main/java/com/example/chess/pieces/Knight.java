package com.example.chess.pieces;

import com.example.chess.abilities.KnightSearch;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Knight extends ChessPiece implements KnightSearch {

    public Knight(Position position, Color color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> knightSearch() {
        return null;
    }
}
