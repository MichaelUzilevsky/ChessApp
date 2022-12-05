package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class King extends ChessPiece implements StraightSearch, DiagonalSearch {

    private boolean moved;
    public King(Position position, PieceColor color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> diagonalSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        for (int i = 1; i < 2; i++) {
            if (inBoard(x + i, y + i, board.getSIZE())) {
                if (board.isPiece(x + i, y + i) && board.getPiece(x + i, y + i).getColor() != this.color) {
                    positions.add(new Position(x + i, y + i));
                    break;
                } else if (!board.isPiece(x + i, y + i))
                    positions.add(new Position(x + i, y + i));
                else if (board.isPiece(x + i, y + i) && board.getPiece(x + i, y + i).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < 2; i++) {
            if (inBoard(x + i, y - i, board.getSIZE())) {
                if (board.isPiece(x + i, y - i) && board.getPiece(x + i, y - i).getColor() != this.color) {
                    positions.add(new Position(x - i, y - i));
                    break;
                } else if (!board.isPiece(x + i, y - i))
                    positions.add(new Position(x + i, y - i));
                else if (board.isPiece(x + i, y - i) && board.getPiece(x + i, y - i).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < 2; i++) {
            if (inBoard(x - i, y + i, board.getSIZE())) {
                if (board.isPiece(x - i, y + i) && board.getPiece(x - i, y + i).getColor() != this.color) {
                    positions.add(new Position(x - i, y + i));
                    break;
                } else if (!board.isPiece(x - i, y + i))
                    positions.add(new Position(x - i, y + i));
                else if (board.isPiece(x - i, y + i) && board.getPiece(x - i, y + i).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < 2; i++) {
            if (inBoard(x - i, y - i, board.getSIZE())) {
                if (board.isPiece(x - i, y - i) && board.getPiece(x - i, y - i).getColor() != this.color) {
                    positions.add(new Position(x - i, y - i));
                    break;
                } else if (!board.isPiece(x - i, y - i))
                    positions.add(new Position(x - i, y - i));
                else if (board.isPiece(x - i, y - i) && board.getPiece(x - i, y - i).getColor() == this.color)
                    break;
            } else
                break;
        }
        return positions;

    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        // horizontal search

        if (inBoard(x + 1, y, board.getSIZE())) {
            if (board.isPiece(x + 1, y) && board.getPiece(x + 1, y).getColor() != this.color) {
                positions.add(new Position(x + 1, y));
            } else if (!board.isPiece(x + 1, y)) {
                positions.add(new Position(x + 1, y));
            }
        }
        if (inBoard(x - 1, y, board.getSIZE())) {
            if (board.isPiece(x - 1, y) && board.getPiece(x - 1, y).getColor() != this.color) {
                positions.add(new Position(x - 1, y));
            } else if (!board.isPiece(x - 1, y)) {
                positions.add(new Position(x - 1, y));
            }
        }
        if (inBoard(x, y + 1, board.getSIZE())) {
            if (board.isPiece(x, y + 1) && board.getPiece(x, y + 1).getColor() != this.color) {
                positions.add(new Position(x, y + 1));
            } else if (!board.isPiece(x, y + 1)) {
                positions.add(new Position(x, y + 1));
            }
        }
        if (inBoard(x, y - 1, board.getSIZE())) {
            if (board.isPiece(x, y - 1) && board.getPiece(x, y - 1).getColor() != this.color) {
                positions.add(new Position(x, y - 1));
            } else if (!board.isPiece(x, y - 1)) {
                positions.add(new Position(x, y - 1));
            }
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

    @Override
    public boolean movePiece(int x, int y, Board board) {
        boolean b = super.movePiece(x, y, board);
        if(b)
            moved = true;
        return b;
    }
}
