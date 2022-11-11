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
    public ArrayList<Position> diagonalSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        if ((kingSearch(x, y, 1, 1, board) != null)) {
            positions.add(kingSearch(x, y, 1, 1, board));
        }
        if ((kingSearch(x, y, -1, -1, board) != null)) {
            positions.add(kingSearch(x, y, -1, -1, board));
        }
        if ((kingSearch(x, y, 1, -1, board) != null)) {
            positions.add(kingSearch(x, y, 1, -1, board));
        }
        if ((kingSearch(x, y, -1, 1, board) != null)) {
            positions.add(kingSearch(x, y, -1, 1, board));
        }

        return positions;

    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        if ((kingSearch(x, y, 1, 0, board) != null)) {
            positions.add(kingSearch(x, y, 1, 0, board));
        }
        if ((kingSearch(x, y, 0, 1, board) != null)) {
            positions.add(kingSearch(x, y, 0, 1, board));
        }
        if ((kingSearch(x, y, -1, 0, board) != null)) {
            positions.add(kingSearch(x, y, -1, 0, board));
        }
        if ((kingSearch(x, y, 0, -1, board) != null)) {
            positions.add(kingSearch(x, y, 0, -1, board));
        }

        return positions;
    }

    private Position kingSearch(int pos_x, int pos_y, int x_move, int y_move, Board board) {
        int total_x = pos_x + x_move;
        int total_y = pos_y + y_move;
        if (inBoard(total_x, total_y, board.getSIZE()) && board.getBoard()[total_x][total_y].getColor() != this.getColor()) {
            return new Position(total_x, total_y);
        }
        return null;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board) {
        ArrayList<Position> d = diagonalSearch(board);
        ArrayList<Position> s = straightSearch(board);
        d.addAll(s);
        return d;
    }
}
