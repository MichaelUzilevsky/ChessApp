package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class King extends ChessPiece implements StraightSearch, DiagonalSearch {

    public King(Position position, Color color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> diagonalSearch() {
        return null;
    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        return null;
    }
}
