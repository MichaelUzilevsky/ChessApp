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
    public ArrayList<Position> diagonalSearch(Board board, ChessPiece[][] pieces) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        for (int i = 1; i < 2; i++) {
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
        for (int i = 1; i < 2; i++) {
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
        for (int i = 1; i < 2; i++) {
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
        for (int i = 1; i < 2; i++) {
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

        // horizontal search

        if (inBoard(x + 1, y, board.getSIZE())) {
            if (board.isPiece(x + 1, y, pieces) && board.getPiece(x + 1, y, pieces).getColor() != this.color) {
                positions.add(new Position(x + 1, y));
            } else if (!board.isPiece(x + 1, y, pieces)) {
                positions.add(new Position(x + 1, y));
            }
        }
        if (inBoard(x - 1, y, board.getSIZE())) {
            if (board.isPiece(x - 1, y, pieces) && board.getPiece(x - 1, y, pieces).getColor() != this.color) {
                positions.add(new Position(x - 1, y));
            } else if (!board.isPiece(x - 1, y, pieces)) {
                positions.add(new Position(x - 1, y));
            }
        }
        if (inBoard(x, y + 1, board.getSIZE())) {
            if (board.isPiece(x, y + 1, pieces) && board.getPiece(x, y + 1, pieces).getColor() != this.color) {
                positions.add(new Position(x, y + 1));
            } else if (!board.isPiece(x, y + 1, pieces)) {
                positions.add(new Position(x, y + 1));
            }
        }
        if (inBoard(x, y - 1, board.getSIZE())) {
            if (board.isPiece(x, y - 1, pieces) && board.getPiece(x, y - 1, pieces).getColor() != this.color) {
                positions.add(new Position(x, y - 1));
            } else if (!board.isPiece(x, y - 1, pieces)) {
                positions.add(new Position(x, y - 1));
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

    @Override
    public boolean movePiece(int x, int y) {
        boolean b = super.movePiece(x, y);
        if (b)
            moved = true;
        return b;
    }

    public boolean canKingSideCastle(Board board) {
        if (moved)
            return false;

        ChessPiece[][] pieces = board.getBoard();

        if (board.isThreateningPosition(getPos_x(), getPos_y(), getColor()))
            return false;

        if (getColor().equals(PieceColor.WHITE)) {
            if (board.isPiece(getPos_x(), 7, pieces) && board.getPiece(getPos_x(), 7, pieces) instanceof Rook &&
                    board.getPiece(getPos_x(), 7, pieces).getColor().equals(PieceColor.WHITE) &&
                    !((Rook) board.getPiece(getPos_x(), 7, pieces)).isMoved()) {

                if (board.isPiece(getPos_x() - 1, getPos_y() + 2, pieces) && board.getPiece(getPos_x() - 1, getPos_y() + 2, pieces) instanceof King)
                    return false;

                for (int i = 1; i < 3; i++) {
                    if (board.isPiece(getPos_x(), getPos_y() + i, pieces) || board.isThreateningPosition(getPos_x(), getPos_y() + i, getColor()))
                        return false;
                }
                return true;
            }
        } else {
            if (board.isPiece(getPos_x(), 7, pieces) && board.getPiece(getPos_x(), 7, pieces) instanceof Rook &&
                    board.getPiece(getPos_x(), 7, pieces).getColor().equals(PieceColor.BLACK) &&
                    !((Rook) board.getPiece(getPos_x(), 7, pieces)).isMoved()) {

                if (board.isPiece(getPos_x() + 1, getPos_y() + 2, pieces) && board.getPiece(getPos_x() + 1, getPos_y() + 2, pieces) instanceof King)
                    return false;

                for (int i = 1; i < 3; i++) {
                    if (board.isPiece(getPos_x(), getPos_y() + i, pieces) || board.isThreateningPosition(getPos_x(), getPos_y() + i, getColor()))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean canQueenSideCastle(Board board) {
        if (moved)
            return false;

        ChessPiece[][] pieces = board.getBoard();

        if (board.isThreateningPosition(getPos_x(), getPos_y(), getColor()))
            return false;

        if (getColor().equals(PieceColor.WHITE)) {
            if (board.isPiece(getPos_x(), 0, pieces) && board.getPiece(getPos_x(), 0, pieces) instanceof Rook
                    && board.getPiece(getPos_x(), 0, pieces).getColor().equals(PieceColor.WHITE) &&
                    !((Rook) board.getPiece(getPos_x(), 0, pieces)).isMoved()) {

                if (board.isPiece(getPos_x() - 1, getPos_y() - 2, pieces) && board.getPiece(getPos_x() - 1, getPos_y() - 2, pieces) instanceof King)
                    return false;

                for (int i = 1; i < 3; i++) {
                    if (board.isPiece(getPos_x(), getPos_y() - i, pieces) || board.isThreateningPosition(getPos_x(), getPos_y() - i, getColor()))
                        return false;
                }
                return true;
            }
        } else {
            if (board.isPiece(getPos_x(), 0, pieces) && board.getPiece(getPos_x(), 0, pieces) instanceof Rook &&
                    board.getPiece(getPos_x(), 0, pieces).getColor().equals(PieceColor.BLACK) &&
                    !((Rook) board.getPiece(getPos_x(), 0, pieces)).isMoved()) {

                if (board.isPiece(getPos_x() + 1, getPos_y() - 2, pieces) && board.getPiece(getPos_x() + 1, getPos_y() - 2, pieces) instanceof King)
                    return false;

                for (int i = 1; i < 3; i++) {
                    if (board.isPiece(getPos_x(), getPos_y() - i, pieces) || board.isThreateningPosition(getPos_x(), getPos_y() - i, getColor()))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board, ChessPiece[][] pieces) {

        ArrayList<Position> positions = super.legalMoves(board, pieces);
        if (canQueenSideCastle(board))
            positions.add(new Position(getPos_x(), getPos_y() - 2));
        if (canKingSideCastle(board))
            positions.add(new Position(getPos_x(), getPos_y() + 2));
        return positions;
    }
}
