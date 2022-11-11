package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Queen extends ChessPiece implements StraightSearch, DiagonalSearch {

    public Queen(Position position, Color color) {
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
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        // horizontal search
        for (int i = x + 1; i < board.getSIZE(); i++) {
            if (inBoard(i, y, board.getSIZE()) && board.getBoard()[i][y].getColor() != this.getColor()) {
                positions.add(new Position(i, y));
            }
        }

        for (int i = x - 1; i >= 0; i--) {
            if (inBoard(i, y, board.getSIZE()) && board.getBoard()[i][y].getColor() != this.getColor()) {
                positions.add(new Position(i, y));
            }
        }


        //vertical search
        for (int i = y + 1; i < board.getSIZE(); i++) {
            if (inBoard(x, i, board.getSIZE()) && board.getBoard()[x][i].getColor() != this.getColor()) {
                positions.add(new Position(x, i));
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (inBoard(x, i, board.getSIZE()) && board.getBoard()[x][i].getColor() != this.getColor()) {
                positions.add(new Position(x, i));
            }
        }

        return positions;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board) {
        ArrayList<Position> d = diagonalSearch(board);
        ArrayList<Position> s = straightSearch(board);
        d.addAll(s);
        return d;
    }
}
