package com.example.chess.game;

import com.example.chess.pieces.Bishop;
import com.example.chess.pieces.ChessPiece;
import com.example.chess.pieces.King;
import com.example.chess.pieces.Knight;
import com.example.chess.pieces.Pawn;
import com.example.chess.pieces.PieceColor;
import com.example.chess.pieces.Queen;
import com.example.chess.pieces.Rook;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Board {
    private final int SIZE = 8;
    private ChessPiece[][] pieces;

    public Board() {
        this.pieces = new ChessPiece[SIZE][SIZE];
        setAllNull();
        setStartingPos();
    }

    public void setAllNull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                pieces[i][j] = null;
            }
        }
    }

    public void setStartingPos() {

        //Black Row
        pieces[0][0] = new Rook(new Position(0, 0), PieceColor.BLACK);
        pieces[0][1] = new Knight(new Position(0, 1), PieceColor.BLACK);
        pieces[0][2] = new Bishop(new Position(0, 2), PieceColor.BLACK);
        pieces[0][3] = new Queen(new Position(0, 3), PieceColor.BLACK);
        pieces[0][4] = new King(new Position(0, 4), PieceColor.BLACK);
        pieces[0][5] = new Bishop(new Position(0, 5), PieceColor.BLACK);
        pieces[0][6] = new Knight(new Position(0, 6), PieceColor.BLACK);
        pieces[0][7] = new Rook(new Position(0, 7), PieceColor.BLACK);

        //Black Pawns
        for (int i = 0; i < SIZE; i++) {
            pieces[1][i] = new Pawn(new Position(1, i), PieceColor.BLACK);
        }

        //White Row
        pieces[7][0] = new Rook(new Position(7, 0), PieceColor.WHITE);
        pieces[7][1] = new Knight(new Position(7, 1), PieceColor.WHITE);
        pieces[7][2] = new Bishop(new Position(7, 2), PieceColor.WHITE);
        pieces[7][3] = new Queen(new Position(7, 3), PieceColor.WHITE);
        pieces[7][4] = new King(new Position(7, 4), PieceColor.WHITE);
        pieces[7][5] = new Bishop(new Position(7, 5), PieceColor.WHITE);
        pieces[7][6] = new Knight(new Position(7, 6), PieceColor.WHITE);
        pieces[7][7] = new Rook(new Position(7, 7), PieceColor.WHITE);

        //White Pawns
        for (int i = 0; i < SIZE; i++) {
            pieces[6][i] = new Pawn(new Position(6, i), PieceColor.WHITE);
        }
    }

    public int getSIZE() {
        return SIZE;
    }

    public ChessPiece[][] getBoard() {
        return pieces;
    }

    public ChessPiece[][] getClone() {
        return clone();
    }

    public void setBoard(ChessPiece[][] boa) {
        this.pieces = boa;
    }

    protected Boolean inBoard(int x, int y) {
        return (x >= 0 && x <= SIZE - 1) && (y >= 0 && y <= SIZE - 1);
    }

    public boolean isPiece(int x, int y, ChessPiece[][] chessPieces) {
        return inBoard(x, y) && chessPieces[x][y] != null;
    }

    public ChessPiece getPiece(int x, int y, ChessPiece[][] chessPieces) {
        return chessPieces[x][y];
    }

    public boolean movePiece(int current_x, int current_y, int move_x, int move_y) {
        if (canMove(current_x, current_y, move_x, move_y, getBoard())) {
            ChessPiece piece = getPiece(current_x, current_y, getBoard());
            if (piece.canMove(move_x, move_y, this, getBoard())) {
                piece.movePiece(move_x, move_y);
                pieces[move_x][move_y] = pieces[current_x][current_y];
                pieces[current_x][current_y] = null;
                return true;
            } else return false;
        }
        return false;
    }

    public boolean canMove(int current_x, int current_y, int move_x, int move_y, ChessPiece[][] chessPieces) {
        ChessPiece piece = getPiece(current_x, current_y, chessPieces);
        return piece.canMove(move_x, move_y, this, getBoard());
    }

    public boolean isOpponentPiece(int oppo_x, int oppo_y, ChessPiece piece, ChessPiece[][] chessPieces) {
        return this.isPiece(oppo_x, oppo_y, chessPieces) && !this.getPiece(oppo_x, oppo_y, chessPieces).getColor().equals(piece.getColor());

    }

    public boolean shouldEatPiece(int oppo_x, int oppo_y, ChessPiece piece, ChessPiece[][] chessPieces) {
        return (canMove(piece.getPos_x(), piece.getPos_y(), oppo_x, oppo_y, chessPieces) && isOpponentPiece(oppo_x, oppo_y, piece, chessPieces));
    }

    public boolean eatPiece(int current_x, int current_y, int move_x, int move_y) {
        if (shouldEatPiece(move_x, move_y, getPiece(current_x, current_y, getBoard()), getBoard())) {
            ChessPiece piece = getPiece(current_x, current_y, getBoard());
            if (piece.canMove(move_x, move_y, this, getBoard())) {
                piece.movePiece(move_x, move_y);
                pieces[move_x][move_y] = pieces[current_x][current_y];
                pieces[current_x][current_y] = null;
                return true;
            } else
                return false;
        }
        return false;
    }

    public Position findWhiteKing(ChessPiece[][] chessPieces) {
        for (int i = 0; i < getSIZE(); i++) {
            for (int j = 0; j < getSIZE(); j++) {
                if (isPiece(i, j, chessPieces) && getPiece(i, j, chessPieces).getColor().equals(PieceColor.WHITE) && getPiece(i, j, chessPieces) instanceof King) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public Position findBlackKing(ChessPiece[][] chessPieces) {
        for (int i = 0; i < getSIZE(); i++) {
            for (int j = 0; j < getSIZE(); j++) {
                if (isPiece(i, j, chessPieces) && getPiece(i, j, chessPieces).getColor().equals(PieceColor.BLACK) && getPiece(i, j, chessPieces) instanceof King) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public boolean checkCheckToColor(PieceColor color) {
        if (color.equals(PieceColor.WHITE)) {
            Position kingPos = findWhiteKing(getBoard());
            int king_x = kingPos.getPos_x(), king_y = kingPos.getPos_y();
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (isPiece(i, j, getBoard()) && getPiece(i, j, getBoard()).getColor().equals(PieceColor.BLACK)) {
                        if (getPiece(i, j, getBoard()).inPossibleMoves(king_x, king_y, this, getBoard())) {
                            return true;
                        }
                    }
                }
            }
        } else {
            Position kingPos = findBlackKing(getBoard());
            int king_x = kingPos.getPos_x(), king_y = kingPos.getPos_y();
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (isPiece(i, j, getBoard()) && getPiece(i, j, getBoard()).getColor().equals(PieceColor.WHITE)) {
                        if (getPiece(i, j, getBoard()).inPossibleMoves(king_x, king_y, this, getBoard())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canSaveMate(int x, int y) {
        ChessPiece piece = getPiece(x, y, getBoard());
        PieceColor color = piece.getColor();
        ArrayList<Position> moves = piece.legalMoves(this, getBoard());
        ChessPiece[][] clone = clone();
        for (Position pos : moves) {
            moveInClone(piece.getPos_x(), piece.getPos_y(), pos.getPos_x(), pos.getPos_y(), clone);
            if (!checkMateToColorNoSaves(color, clone))
                return true;
        }
        return false;
    }

    public boolean checkMateToColorNoSaves(PieceColor color, ChessPiece[][] clone) {
        if (color.equals(PieceColor.WHITE)) {
            Position kingPos = findWhiteKing(clone);
            int king_x = kingPos.getPos_x(), king_y = kingPos.getPos_y();
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (isPiece(i, j, clone) && getPiece(i, j, clone).getColor().equals(PieceColor.BLACK)) {
                        if (getPiece(i, j, clone).inPossibleMoves(king_x, king_y, this, clone)) {
                            return true;
                        }
                    }
                }
            }
        } else {
            Position kingPos = findBlackKing(clone);
            int king_x = kingPos.getPos_x(), king_y = kingPos.getPos_y();
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (clone[i][j] != null && clone[i][j].getColor().equals(PieceColor.WHITE)) {
                        if (clone[i][j].inPossibleMoves(king_x, king_y, this, clone)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //use after checkCheck!!!!
    public boolean checkMateTOColor(PieceColor color) {
        if (color.equals(PieceColor.WHITE)) {
            Position king_pos = findWhiteKing(getBoard());
            int king_x = king_pos.getPos_x(), king_y = king_pos.getPos_y();
            if (getPiece(king_x, king_y, getBoard()).legalMoves(this, getBoard()).size() > 0)
                return false;
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (isPiece(i, j, getBoard()) && getPiece(i, j, getBoard()).getColor().equals(PieceColor.WHITE)) {
                        if (canSaveMate(i, j))
                            return false;
                    }
                }
            }
        } else {
            Position king_pos = findBlackKing(getBoard());
            int king_x = king_pos.getPos_x(), king_y = king_pos.getPos_y();
            if (getPiece(king_x, king_y, getBoard()).legalMoves(this, getBoard()).size() > 0)
                return false;
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (isPiece(i, j, getBoard()) && getPiece(i, j, getBoard()).getColor().equals(PieceColor.BLACK)) {
                        if (canSaveMate(i, j))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkCheckToColor(int current_x, int current_y, int move_x, int move_y, PieceColor color) {
        ChessPiece[][] clone = clone();
        if (color.equals(PieceColor.WHITE)) {
            moveInClone(current_x, current_y, move_x, move_y, clone);
            Position kingPos = findWhiteKing(clone);
            int king_x = kingPos.getPos_x(), king_y = kingPos.getPos_y();
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (isPiece(i, j, clone) && getPiece(i, j, clone).getColor().equals(PieceColor.BLACK)) {
                        if (getPiece(i, j, clone).inPossibleMoves(king_x, king_y, this, clone)) {
                            return true;
                        }
                    }
                }
            }
        } else {
            moveInClone(current_x, current_y, move_x, move_y, clone);
            Position kingPos = findBlackKing(clone);
            int king_x = kingPos.getPos_x(), king_y = kingPos.getPos_y();
            for (int i = 0; i < getSIZE(); i++) {
                for (int j = 0; j < getSIZE(); j++) {
                    if (clone[i][j] != null && clone[i][j].getColor().equals(PieceColor.WHITE)) {
                        if (clone[i][j].inPossibleMoves(king_x, king_y, this, clone)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public ChessPiece[][] clone() {
        ChessPiece[][] clone = new ChessPiece[getSIZE()][getSIZE()];
        for (int i = 0; i < getSIZE(); i++) {
            for (int j = 0; j < getSIZE(); j++) {
                if (isPiece(i, j, getBoard())) {
                    if (getPiece(i, j, getBoard()) instanceof King) {
                        clone[i][j] = new King(new Position(i, j), getPiece(i, j, getBoard()).getColor());
                    } else if (getPiece(i, j, getBoard()) instanceof Queen) {
                        clone[i][j] = new Queen(new Position(i, j), getPiece(i, j, getBoard()).getColor());
                    } else if (getPiece(i, j, getBoard()) instanceof Bishop) {
                        clone[i][j] = new Bishop(new Position(i, j), getPiece(i, j, getBoard()).getColor());
                    } else if (getPiece(i, j, getBoard()) instanceof Knight) {
                        clone[i][j] = new Knight(new Position(i, j), getPiece(i, j, getBoard()).getColor());
                    } else if (getPiece(i, j, getBoard()) instanceof Rook) {
                        clone[i][j] = new Rook(new Position(i, j), getPiece(i, j, getBoard()).getColor());
                    } else if (getPiece(i, j, getBoard()) instanceof Pawn) {
                        clone[i][j] = new Pawn(new Position(i, j), getPiece(i, j, getBoard()).getColor());
                    }
                } else
                    clone[i][j] = null;
            }
        }
        return clone;
    }

    private void moveInClone(int current_x, int current_y, int move_x, int move_y, ChessPiece[][] clone) {
        if (clone[current_x][current_y] != null) {
            ChessPiece piece = clone[current_x][current_y];
            piece.movePiece(move_x, move_y);
            clone[move_x][move_y] = clone[current_x][current_y];
            clone[current_x][current_y] = null;
        }
    }

    public boolean isThreateningPosition(int x, int y, PieceColor beingThreatened) {
        PieceColor enemy;
        if (beingThreatened.equals(PieceColor.WHITE))
            enemy = PieceColor.BLACK;
        else
            enemy = PieceColor.WHITE;

        for (int i = 0; i < getSIZE(); i++) {
            for (int j = 0; j < getSIZE(); j++) {
                if (isPiece(i, j, pieces) && getPiece(i, j, pieces).getColor().equals(enemy) && !(getPiece(i, j, pieces) instanceof King)) {
                    if (getPiece(i, j, pieces).inLegalMove(x, y, this, pieces)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void kingSideCastle(PieceColor color) {
        if (color.equals(PieceColor.WHITE)) {
            movePiece(7, 4, 7, 6);
            moveInClone(7, 7, 7, 5, getBoard());
        } else {
            movePiece(0, 4, 0, 6);
            moveInClone(0, 7, 0, 5, getBoard());
        }
    }

    public void queenSideCastle(PieceColor color) {
        if (color.equals(PieceColor.WHITE)) {
            movePiece(7, 4, 7, 2);
            moveInClone(7, 0, 7, 3, getBoard());
        } else {
            movePiece(0, 4, 0, 2);
            moveInClone(0, 0, 0, 3, getBoard());
        }
    }

    public boolean checkTransformation(int move_x) {
        return move_x == 0 || move_x == 7;
    }

    public void setNewQueen(int x, int y, PieceColor color) {
        getBoard()[x][y] = new Queen(new Position(x, y), color);
    }

    public boolean checkDraw(PieceColor lastMove) {

        if (lastMove.equals(PieceColor.WHITE)) {
            if (!checkCheckToColor(PieceColor.BLACK) && !canOneMove(PieceColor.BLACK))
                return true;
        } else {
            if (!checkCheckToColor(PieceColor.WHITE) && !canOneMove(PieceColor.WHITE))
                return true;
        }
        return kingKingDraw();

    }

    public boolean hasMoves(ChessPiece piece) {
        return piece.legalMoves(this, getBoard()).size() > 0;
    }

    public boolean canOneMove(PieceColor color) {
        for (int i = 0; i < getSIZE(); i++) {
            for (int j = 0; j < getSIZE(); j++) {
                if (isPiece(i, j, getBoard()) && getPiece(i, j, getBoard()).getColor().equals(color) && hasMoves(getPiece(i, j, getBoard())))
                    return true;
            }
        }
        return false;
    }

    public boolean kingKingDraw() {
        ArrayList<ChessPiece> chessPieces = allPieces(getBoard());
        if (chessPieces.size() == 2)
            return true;
        if (chessPieces.size() == 3) {
            for (ChessPiece piece : chessPieces) {
                if (piece instanceof Knight || piece instanceof Bishop)
                    return true;
            }
        }
        return false;
    }

    public ArrayList<ChessPiece> allPieces(ChessPiece[][] array) {
        ArrayList<ChessPiece> nonNullObjects = new ArrayList<>();
        for (ChessPiece[] chessPieces : array) {
            for (ChessPiece chessPiece : chessPieces) {
                if (chessPiece != null) {
                    nonNullObjects.add(chessPiece);
                }
            }
        }
        return nonNullObjects;
    }
}
