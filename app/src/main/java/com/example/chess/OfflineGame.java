package com.example.chess;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.BoardDesign.Tile;
import com.example.chess.game.Board;
import com.example.chess.pieces.ChessPiece;
import com.example.chess.pieces.King;
import com.example.chess.pieces.Pawn;
import com.example.chess.pieces.PieceColor;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class OfflineGame extends AppCompatActivity {

    private int width, height, startBoardHeight;
    private final Tile[][] tiles = new Tile[8][8];
    private final double CIRCLE_SIZE = 0.4;
    private final int[] WHITE = new int[]{232, 231, 208};
    private final int[] BLACK = new int[]{75, 115, 153};
    private Board board;
    private boolean pressed = false;
    private int currentPiece_x, currentPiece_y;
    private PieceColor turn = PieceColor.WHITE;
    private boolean flip;

    private ImageView blackPawn_01, blackPawn_02, blackPawn_03, blackPawn_04, blackPawn_05, blackPawn_06, blackPawn_07, blackPawn_08,
            blackRook_01, blackRook_02,
            blackKnight_01, blackKnight_02,
            blackBishop_01, blackBishop_02,
            blackKing, blackQueen;

    private ImageView whitePawn_01, whitePawn_02, whitePawn_03, whitePawn_04, whitePawn_05, whitePawn_06, whitePawn_07, whitePawn_08,
            whiteRook_01, whiteRook_02,
            whiteKnight_01, whiteKnight_02,
            whiteBishop_01, whiteBishop_02,
            whiteKing, whiteQueen;

    private ImageView[][] white, black;

    private ImageView[] possibleMoves;
    private Dialog quitD;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels - 2;
        height = displayMetrics.heightPixels;
        startBoardHeight = (height - width) / 2;

        flip = false;

        AbsoluteLayout border = findViewById(R.id.border);
        border.setMinimumHeight(width + 1);
        AbsoluteLayout pieces = findViewById(R.id.pieces);
        pieces.setMinimumHeight(width);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);

        WHITE[0] = sharedPreferences.getInt("whiteR", 232);
        WHITE[1] = sharedPreferences.getInt("whiteG", 231);
        WHITE[2] = sharedPreferences.getInt("whiteB", 208);

        BLACK[0] = sharedPreferences.getInt("blackR", 75);
        BLACK[1] = sharedPreferences.getInt("blackG", 115);
        BLACK[2] = sharedPreferences.getInt("blackB", 153);

        drawBoard_white();

        //black
        blackPawn_01 = findViewById(R.id.blackPawn_1);
        blackPawn_02 = findViewById(R.id.blackPawn_2);
        blackPawn_03 = findViewById(R.id.blackPawn_3);
        blackPawn_04 = findViewById(R.id.blackPawn_4);
        blackPawn_05 = findViewById(R.id.blackPawn_5);
        blackPawn_06 = findViewById(R.id.blackPawn_6);
        blackPawn_07 = findViewById(R.id.blackPawn_7);
        blackPawn_08 = findViewById(R.id.blackPawn_8);

        blackRook_01 = findViewById(R.id.blackRook_1);
        blackRook_02 = findViewById(R.id.blackRook_2);

        blackKnight_01 = findViewById(R.id.blackKnight_1);
        blackKnight_02 = findViewById(R.id.blackKnight_2);

        blackBishop_01 = findViewById(R.id.blackBishop_1);
        blackBishop_02 = findViewById(R.id.blackBishop_2);

        blackKing = findViewById(R.id.blackKing);
        blackQueen = findViewById(R.id.blackQueen);

        //white
        whitePawn_01 = findViewById(R.id.whitePawn_1);
        whitePawn_03 = findViewById(R.id.whitePawn_3);
        whitePawn_02 = findViewById(R.id.whitePawn_2);
        whitePawn_04 = findViewById(R.id.whitePawn_4);
        whitePawn_05 = findViewById(R.id.whitePawn_5);
        whitePawn_06 = findViewById(R.id.whitePawn_6);
        whitePawn_07 = findViewById(R.id.whitePawn_7);
        whitePawn_08 = findViewById(R.id.whitePawn_8);

        whiteRook_01 = findViewById(R.id.whiteRook_1);
        whiteRook_02 = findViewById(R.id.whiteRook_2);

        whiteKnight_01 = findViewById(R.id.whiteKnight_1);
        whiteKnight_02 = findViewById(R.id.whiteKnight_2);

        whiteBishop_01 = findViewById(R.id.whiteBishop_1);
        whiteBishop_02 = findViewById(R.id.whiteBishop_2);

        whiteKing = findViewById(R.id.whiteKing);
        whiteQueen = findViewById(R.id.whiteQueen);

        black = new ImageView[][]{{blackPawn_01, blackPawn_02, blackPawn_03, blackPawn_04, blackPawn_05, blackPawn_06, blackPawn_07, blackPawn_08},
                {blackRook_01, blackKnight_01, blackBishop_01, blackQueen, blackKing, blackBishop_02, blackKnight_02, blackRook_02}};

        white = new ImageView[][]{{whitePawn_01, whitePawn_02, whitePawn_03, whitePawn_04, whitePawn_05, whitePawn_06, whitePawn_07, whitePawn_08},
                {whiteRook_01, whiteKnight_01, whiteBishop_01, whiteQueen, whiteKing, whiteBishop_02, whiteKnight_02, whiteRook_02}};

        for (ImageView[] imageViews : black) {
            for (ImageView i : imageViews) {
                i.getLayoutParams().height = width / 8;
                i.getLayoutParams().width = width / 8;
            }
        }

        for (ImageView[] imageViews : white) {
            for (ImageView i : imageViews) {
                i.getLayoutParams().height = width / 8;
                i.getLayoutParams().width = width / 8;
            }
        }

        arrangeBoard(PieceColor.WHITE);

        board = new Board();

        ImageView possibleMove_01, possibleMove_02, possibleMove_03, possibleMove_04, possibleMove_05, possibleMove_06, possibleMove_07,
                possibleMove_08, possibleMove_09, possibleMove_10, possibleMove_11, possibleMove_12, possibleMove_13, possibleMove_14,
                possibleMove_15, possibleMove_16, possibleMove_17, possibleMove_18, possibleMove_19, possibleMove_20, possibleMove_21,
                possibleMove_22, possibleMove_23, possibleMove_24, possibleMove_25, possibleMove_26, possibleMove_27, possibleMove_28;

        possibleMove_01 = findViewById(R.id.legal_01);
        possibleMove_02 = findViewById(R.id.legal_02);
        possibleMove_03 = findViewById(R.id.legal_03);
        possibleMove_04 = findViewById(R.id.legal_04);
        possibleMove_05 = findViewById(R.id.legal_05);
        possibleMove_06 = findViewById(R.id.legal_06);
        possibleMove_07 = findViewById(R.id.legal_07);
        possibleMove_08 = findViewById(R.id.legal_08);
        possibleMove_09 = findViewById(R.id.legal_09);
        possibleMove_10 = findViewById(R.id.legal_10);
        possibleMove_11 = findViewById(R.id.legal_11);
        possibleMove_12 = findViewById(R.id.legal_12);
        possibleMove_13 = findViewById(R.id.legal_13);
        possibleMove_14 = findViewById(R.id.legal_14);
        possibleMove_15 = findViewById(R.id.legal_15);
        possibleMove_16 = findViewById(R.id.legal_16);
        possibleMove_17 = findViewById(R.id.legal_17);
        possibleMove_18 = findViewById(R.id.legal_18);
        possibleMove_19 = findViewById(R.id.legal_19);
        possibleMove_20 = findViewById(R.id.legal_20);
        possibleMove_21 = findViewById(R.id.legal_21);
        possibleMove_22 = findViewById(R.id.legal_22);
        possibleMove_23 = findViewById(R.id.legal_23);
        possibleMove_24 = findViewById(R.id.legal_24);
        possibleMove_25 = findViewById(R.id.legal_25);
        possibleMove_26 = findViewById(R.id.legal_26);
        possibleMove_27 = findViewById(R.id.legal_27);
        possibleMove_28 = findViewById(R.id.legal_28);

        possibleMoves = new ImageView[]{possibleMove_01, possibleMove_02, possibleMove_03, possibleMove_04, possibleMove_05, possibleMove_06, possibleMove_07,
                possibleMove_08, possibleMove_09, possibleMove_10, possibleMove_11, possibleMove_12, possibleMove_13, possibleMove_14,
                possibleMove_15, possibleMove_16, possibleMove_17, possibleMove_18, possibleMove_19, possibleMove_20, possibleMove_21,
                possibleMove_22, possibleMove_23, possibleMove_24, possibleMove_25, possibleMove_26, possibleMove_27, possibleMove_28};

        for (ImageView i : possibleMoves) {
            i.getLayoutParams().height = (int) ((width / 8) * CIRCLE_SIZE);
            i.getLayoutParams().width = (int) ((width / 8) * CIRCLE_SIZE);
            i.setVisibility(View.INVISIBLE);
            i.setAlpha(0.7f);
            i.setElevation(100);
        }

        pieces.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int tile_x = getTileId(x, y)[1];
                    int tile_y = getTileId(x, y)[0];

                    if (!pressed) {
                        if (board.isPiece(tile_x, tile_y, board.getBoard())) {
                            PieceColor pieceColor = board.getPiece(tile_x, tile_y, board.getBoard()).getColor();
                            if (turn.equals(pieceColor)) {
                                setInvisible();
                                showLegalMoves(board.getPiece(tile_x, tile_y, board.getBoard()));
                                pressed = true;
                                currentPiece_x = tile_x;
                                currentPiece_y = tile_y;
                            }
                        }
                    }
                    else {
                        if (board.canMove(currentPiece_x, currentPiece_y, tile_x, tile_y, board.getBoard())) {
                            if (board.shouldEatPiece(tile_x, tile_y, board.getPiece(currentPiece_x, currentPiece_y, board.getBoard()), board.getBoard())) {
                                board.eatPiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                removePieceImg(tile_x, tile_y);
                                movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                playEatSound();
                            } else {
                                if (board.getPiece(currentPiece_x, currentPiece_y, board.getBoard()) instanceof King) {
                                    //king size castle
                                    if (tile_y - currentPiece_y == 2) {
                                        board.kingSideCastle(turn);
                                        movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                        rookCastle(turn, -2);
                                    }
                                    //queen side castle
                                    else if (tile_y - currentPiece_y == -2) {
                                        board.queenSideCastle(turn);
                                        movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                        rookCastle(turn, 3);
                                    } else {
                                        board.movePiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                        movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                    }
                                } else {
                                    board.movePiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                    movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                }
                            }

                            if (board.getPiece(tile_x, tile_y, board.getBoard()) instanceof Pawn
                                    && board.checkTransformation(tile_x)) {
                                if (board.getPiece(tile_x, tile_y, board.getBoard()).getColor().equals(PieceColor.WHITE)) {
                                    getPieceFromCanvasPos(tile_x, tile_y).setImageResource(R.drawable.white_queen);
                                    board.setNewQueen(tile_x, tile_y, PieceColor.WHITE);
                                } else {
                                    getPieceFromCanvasPos(tile_x, tile_y).setImageResource(R.drawable.black_queen);
                                    board.setNewQueen(tile_x, tile_y, PieceColor.BLACK);
                                }
                            }

                            if (board.checkDraw(turn)) {
                                drawDialog();
                            } else {
                                switchTurn();

                                if (board.checkCheckToColor(turn)) {
                                    if (board.checkMateTOColor(turn)) {
                                        switchTurn();
                                        checkmateDialog(turn);
                                    } else
                                        checkDialog();
                                }
                            }
                            pressed = false;
                            setInvisible();

                        } else {
                            pressed = false;
                            if (board.isPiece(tile_x, tile_y, board.getBoard())) {
                                PieceColor pieceColor = board.getPiece(tile_x, tile_y, board.getBoard()).getColor();
                                if (turn.equals(pieceColor)) {
                                    setInvisible();
                                    showLegalMoves(board.getPiece(tile_x, tile_y, board.getBoard()));
                                    pressed = true;
                                    currentPiece_x = tile_x;
                                    currentPiece_y = tile_y;
                                } else
                                    setInvisible();
                            } else {
                                setInvisible();
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        quitD = new Dialog(this);
        quitD.setContentView(R.layout.quit_dialog);
        quitD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quitD.setCancelable(true);
        quitD.show();
    }

    public void checkDialog() {
        playCheckSound();
    }

    public void checkmateDialog(PieceColor color) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.checkmate_dialog);
        dialog.setCancelable(false);
        TextView winner = dialog.findViewById(R.id.winner);
        if (color.equals(PieceColor.WHITE)) {
            winner.setText("White Won");
        } else {
            winner.setText("Black Won");
        }
        dialog.setCancelable(true);
        playCheckmateSound();
        dialog.show();
    }

    public void drawDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.draw_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void drawBoard_white() {
        int ROWS = 8;
        int tileSize = width / ROWS;
        RelativeLayout boardLayout = findViewById(R.id.chessBoard);

        int[] color;
        for (int i = 0; i < ROWS; i++) {
            int COLS = 8;
            for (int j = 0; j < COLS; j++) {
                if ((i + j) % 2 == 0) {
                    color = WHITE;
                } else {
                    color = BLACK;
                }
                int x_pos = i * tileSize , y_pos = j * tileSize;
                Tile tile = new Tile(this, x_pos, y_pos, x_pos + tileSize, y_pos + tileSize, color);
                tiles[i][j] = tile;
                boardLayout.addView(tile);
            }
        }
    }

    public void arrangeBoard(PieceColor bottomColor) {
        int tileSize = width / 8;
        if (bottomColor == PieceColor.WHITE) {
            //black pawns top
            for (int i = 0; i < 8; i++) {
                black[0][i].setX(i * tileSize);
                black[0][i].setY(tileSize);
            }
            //black other
            for (int i = 0; i < 8; i++) {
                black[1][i].setX(i * tileSize);
                black[1][i].setY(0);
            }
            //white pawns bottom
            for (int i = 0; i < 8; i++) {
                white[0][i].setX(i * tileSize);
                white[0][i].setY(6 * tileSize);
            }
            //white other
            for (int i = 0; i < 8; i++) {
                white[1][i].setX(i * tileSize);
                white[1][i].setY(7 * tileSize);
            }
        } else {
            black[1][3] = blackKing;
            black[1][4] = blackQueen;
            white[1][3] = whiteKing;
            white[1][4] = whiteQueen;
            //white pawns bottom
            for (int i = 0; i < 8; i++) {
                white[0][i].setX(i * tileSize);
                white[0][i].setY(tileSize);
            }
            //white other
            for (int i = 0; i < 8; i++) {
                white[1][i].setX(i * tileSize);
                white[1][i].setY(0);
            }

            //black pawns top
            for (int i = 0; i < 8; i++) {
                black[0][i].setX(i * tileSize);
                black[0][i].setY(6 * tileSize);
            }
            //black other
            for (int i = 0; i < 8; i++) {
                black[1][i].setX(i * tileSize);
                black[1][i].setY(7 * tileSize);
            }
        }

    }

    public int[] getTileId(int x, int y) {
        int row = x / (width / 8);
        int col = y / (width / 8);
        return new int[]{row, col};
    }

    public void setPositionOfCircle(int x, int y, ImageView circle) {
        int y_pos = (int) (x * (width / 8) + ((1 - CIRCLE_SIZE) * (width / 8)) / 1.9);
        int x_pos = (int) (y * (width / 8) + ((1 - CIRCLE_SIZE) * (width / 8)) / 1.9);
        circle.setX(x_pos);
        circle.setY(y_pos);
        circle.setVisibility(View.VISIBLE);
    }

    public void setInvisible() {
        for (ImageView i : possibleMoves) {
            i.setX(-1000);
            i.setY(-1000);
        }
    }

    public void showLegalMoves(ChessPiece piece) {
        AlphaAnimation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(250);
        animation1.setFillAfter(true);
        ArrayList<Position> m_legalMoves = piece.legalMoves(board, board.getBoard());
        for (int i = 0; i < m_legalMoves.size(); i++) {
            int x = m_legalMoves.get(i).getPos_x();
            int y = m_legalMoves.get(i).getPos_y();
            setPositionOfCircle(x, y, possibleMoves[i]);
            possibleMoves[i].startAnimation(animation1);
        }
    }

    public void movePieceImg(int current_x, int current_y, int move_x, int move_y) {
        ImageView piece = getPieceFromCanvasPos(current_x, current_y);
        setPiecePosition(move_x, move_y, piece);

    }

    public void removePieceImg(int x, int y) {
        movePieceImg(x, y, -999, -999);
    }

    public ImageView getPieceFromCanvasPos(int x, int y) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                int img_x = (int) white[i][j].getX(), pos_x = toPixels(y), img_y = (int) white[i][j].getY(), pos_y = toPixels(x);
                if (inRange(img_x, pos_x) && inRange(img_y, pos_y)) {
                    return white[i][j];
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                int img_x = (int) black[i][j].getX(), pos_x = toPixels(y), img_y = (int) black[i][j].getY(), pos_y = toPixels(x);
                if (inRange(img_x, pos_x) && inRange(img_y, pos_y)) {
                    return black[i][j];
                }
            }
        }
        return null;
    }

    public void setPiecePosition(int x, int y, ImageView piece) {
        int y_pos = (int) (x * (width / 8));
        int x_pos = (int) (y * (width / 8));
        piece.setX(x_pos);
        piece.setY(y_pos);
    }

    public int toPixels(int x) {
        return (x * (width / 8));
    }

    public boolean inRange(int imgPos, int piecePos) {
        final int deadBand = 5;
        return Math.abs(imgPos - piecePos) < deadBand;
    }

    public void playEatSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.man_eating);
        mediaPlayer.start();
    }

    public void playCheckSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.check_sound);
        mediaPlayer.start();
    }

    public void playCheckmateSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.checkmate_sound);
        mediaPlayer.start();
    }

    public void switchTurn() {
        if (turn.equals(PieceColor.WHITE))
            turn = PieceColor.BLACK;
        else
            turn = PieceColor.WHITE;
    }

    public void reload(View view) {
        finish();
        startActivity(new Intent(OfflineGame.this, OfflineGame.class));
    }

    //king side x = -2, queen side x = 3
    public void rookCastle(PieceColor color, int x) {
        if (color.equals(PieceColor.WHITE)) {
            //king side
            if (x == -2) {
                movePieceImg(7, 7, 7, 5);
            }
            //queen
            else if (x == 3) {
                movePieceImg(7, 0, 7, 3);
            }
        } else {
            //king side
            if (x == -2) {
                movePieceImg(0, 7, 0, 5);
            }
            //queen side
            else if (x == 3) {
                movePieceImg(0, 0, 0, 3);
            }
        }
    }

    public void cancel(View view) {
        quitD.cancel();
    }

    public void quit(View view) {
        startActivity(new Intent(OfflineGame.this, GameOptions.class));
        finish();
    }

    private void rotateImage(ImageView imageView) {
        if(flip) {
            imageView.animate().rotation(0).start();
        }
        else {
            imageView.animate().rotation(180).start();
        }

    }

    public void flipAll(View view) {
        for (ImageView[] images: black) {
            for (ImageView image: images) {
                rotateImage(image);
            }
        }
        for (ImageView[] images: white) {
            for (ImageView image: images) {
                rotateImage(image);
            }
        }
        flip = !flip;
    }
    public void toMain(View view) {
        startActivity(new Intent(OfflineGame.this, GameOptions.class));
        finish();
    }
}