package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Bishop extends ChessPiece implements DiagonalSearch {

    public Bishop(Position position, Color color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> diagonalSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        for (int i = 1; i < board.getSIZE(); i++) {

            if (inBoard(x + i, y + i, board.getSIZE()) && board.getBoard()[x + i][y + i].getColor() != this.getColor()) {
                positions.add(new Position(x + i, y + i));
            }
            if (inBoard(x - i, y - i, board.getSIZE()) && board.getBoard()[x - i][y - i].getColor() != this.getColor()) {
                positions.add(new Position(x - i, y - i));
            }
            if (inBoard(x + i, y - i, board.getSIZE()) && board.getBoard()[x + i][y - i].getColor() != this.getColor()) {
                positions.add(new Position(x + i, y - i));
            }
            if (inBoard(x - i, y + i, board.getSIZE()) && board.getBoard()[x - i][y + i].getColor() != this.getColor()) {
                positions.add(new Position(x - i, y + i));
            }
        }
        return positions;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board) {
        return diagonalSearch(board);
    }
}
