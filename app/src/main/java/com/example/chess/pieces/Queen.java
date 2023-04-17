package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Queen extends ChessPiece implements StraightSearch, DiagonalSearch {

    public Queen(Position position, PieceColor color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> diagonalSearch(Board board, ChessPiece[][] pieces) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x + i, y + i, board.getSIZE())) {
                if (board.isPiece(x + i, y + i, pieces) && board.getPiece(x + i, y + i, pieces).getColor() != this.color) {
                    positions.add(new Position(x + i, y + i));
                    break;
                } else if (!board.isPiece(x + i, y + i, pieces))
                    positions.add(new Position(x + i, y + i));
                else if (board.isPiece(x + i, y + i, pieces) && board.getPiece(x + i, y + i, pieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x + i, y - i, board.getSIZE())) {
                if (board.isPiece(x + i, y - i, pieces) && board.getPiece(x + i, y - i, pieces).getColor() != this.color) {
                    positions.add(new Position(x + i, y - i));
                    break;
                } else if (!board.isPiece(x + i, y - i, pieces))
                    positions.add(new Position(x + i, y - i));
                else if (board.isPiece(x + i, y - i, pieces) && board.getPiece(x + i, y - i, pieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x - i, y + i, board.getSIZE())) {
                if (board.isPiece(x - i, y + i, pieces) && board.getPiece(x - i, y + i, pieces).getColor() != this.color) {
                    positions.add(new Position(x - i, y + i));
                    break;
                } else if (!board.isPiece(x - i, y + i, pieces))
                    positions.add(new Position(x - i, y + i));
                else if (board.isPiece(x - i, y + i, pieces) && board.getPiece(x - i, y + i, pieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x - i, y - i, board.getSIZE())) {
                if (board.isPiece(x - i, y - i, pieces) && board.getPiece(x - i, y - i, pieces).getColor() != this.color) {
                    positions.add(new Position(x - i, y - i));
                    break;
                } else if (!board.isPiece(x - i, y - i, pieces))
                    positions.add(new Position(x - i, y - i));
                else if (board.isPiece(x - i, y - i, pieces) && board.getPiece(x - i, y - i, pieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        return positions;
    }

    @Override
    public ArrayList<Position> straightSearch(Board board, ChessPiece[][] pieces) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        // vertical search
        for (int i = x + 1; i < board.getSIZE(); i++) {
            if (inBoard(i, y, board.getSIZE())) {
                if (board.isPiece(i, y, pieces) && board.getPiece(i, y, pieces).getColor() == this.color)
                    i = board.getSIZE() + 1;
                else if (board.isPiece(i, y, pieces) && board.getPiece(i, y, pieces).getColor() != this.color) {
                    positions.add(new Position(i, y));
                    i = board.getSIZE() + 1;
                } else if (!board.isPiece(i, y, pieces)) {
                    positions.add(new Position(i, y));
                }
            }
        }

        for (int i = x - 1; i >= 0; i--) {
            if (inBoard(i, y, board.getSIZE())) {
                if (board.isPiece(i, y, pieces) && board.getPiece(i, y, pieces).getColor() == this.color)
                    i = -1;
                else if (board.isPiece(i, y, pieces) && board.getPiece(i, y, pieces).getColor() != this.color) {
                    positions.add(new Position(i, y));
                    i = -1;
                } else if (!board.isPiece(i, y, pieces)) {
                    positions.add(new Position(i, y));
                }
            }
        }


        //horizontal search
        for (int i = y + 1; i < board.getSIZE(); i++) {
            if (inBoard(x, i, board.getSIZE())) {
                if (board.isPiece(x, i, pieces) && board.getPiece(x, i, pieces).getColor() == this.color)
                    i = board.getSIZE() + 1;
                else if (board.isPiece(x, i, pieces) && board.getPiece(x, i, pieces).getColor() != this.color) {
                    positions.add(new Position(x, i));
                    i = board.getSIZE() + 1;
                } else if (!board.isPiece(x, i, pieces)) {
                    positions.add(new Position(x, i));
                }
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (inBoard(x, i, board.getSIZE())) {
                if (board.isPiece(x, i, pieces) && board.getPiece(x, i, pieces).getColor() == this.color)
                    i = -1;
                else if (board.isPiece(x, i, pieces) && board.getPiece(x, i, pieces).getColor() != this.color) {
                    positions.add(new Position(x, i));
                    i = -1;
                } else if (!board.isPiece(x, i, pieces)) {
                    positions.add(new Position(x, i));
                }
            }
        }

        return positions;
    }

    @Override
    public ArrayList<Position> possibleMoves(Board board, ChessPiece[][] pieces) {
        ArrayList<Position> d = diagonalSearch(board, pieces);
        ArrayList<Position> s = straightSearch(board, pieces);
        d.addAll(s);
        return d;
    }
}
