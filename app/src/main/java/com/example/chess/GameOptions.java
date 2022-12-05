package com.example.chess;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.chess.BoardDesign.Tile;
import com.example.chess.game.Board;
import com.example.chess.pieces.ChessPiece;
import com.example.chess.pieces.PieceColor;
import com.example.chess.utils.Position;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class GameOptions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Dialog create_join, guest, gameCode, waitDialog;
    private String username_sp;
    private DatabaseReference available_codes, games;
    private int width;
    private final Tile[][] tiles = new Tile[8][8];
    private final double CIRCLE_SIZE = 0.4;
    private final int[] WHITE = new int[]{251, 253, 246};
    private final int[] BLACK = new int[]{59, 164, 190};
    private Board board;
    private boolean pressed = false;
    private int currentPiece_x, currentPiece_y;
    private int count = 1;

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

    private ImageView possibleMove_01, possibleMove_02, possibleMove_03, possibleMove_04, possibleMove_05, possibleMove_06, possibleMove_07,
            possibleMove_08, possibleMove_09, possibleMove_10, possibleMove_11, possibleMove_12, possibleMove_13, possibleMove_14,
            possibleMove_15, possibleMove_16, possibleMove_17, possibleMove_18, possibleMove_19, possibleMove_20, possibleMove_21,
            possibleMove_22, possibleMove_23, possibleMove_24, possibleMove_25, possibleMove_26, possibleMove_27, possibleMove_28;

    private ImageView[] possibleMoves;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels - 2;

        AbsoluteLayout border = findViewById(R.id.border);
        border.setMinimumHeight(width + 1);
        AbsoluteLayout pieces = findViewById(R.id.pieces);
        pieces.setMinimumHeight(width);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);
        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        findViewById(R.id.menuImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        available_codes = FirebaseDatabase.getInstance().getReference("Available Codes");
        games = FirebaseDatabase.getInstance().getReference("Games");


        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_game).setVisible(false);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


        username_sp = sharedPreferences.getString("username", "");
        String name_sp = sharedPreferences.getString("name", "");

        if (username_sp.equals("") && name_sp.equals("")) {
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_signup).setVisible(true);
        } else {
            menu.findItem(R.id.nav_profile).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_signup).setVisible(false);
        }
        drawBoard();

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

//                    Toast.makeText(GameOptions.this, tile_x + ", " + tile_y, Toast.LENGTH_SHORT).show();

                    if (!pressed) {
                        if (board.isPiece(tile_x, tile_y)) {
                            setInvisible();
                            showLegalMoves(board.getPiece(tile_x, tile_y));
                            pressed = true;
                            currentPiece_x = tile_x;
                            currentPiece_y = tile_y;
                        }
                    } else if (pressed) {
                        if (board.movePiece(currentPiece_x, currentPiece_y, tile_x, tile_y)) {
                            movePiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                            pressed = false;
                            setInvisible();
                        } else {
                            pressed = false;
                            if (board.isPiece(tile_x, tile_y)) {
                                setInvisible();
                                showLegalMoves(board.getPiece(tile_x, tile_y));
                                pressed = true;
                                currentPiece_x = tile_x;
                                currentPiece_y = tile_y;
                            } else {
                                setInvisible();
                            }
                        }
                    }
                }

                return true;
            }
        });

//        //define black pieces
//        ChessPiece blackPawn_01 = new Pawn(new Position(1, 0), PieceColor.BLACK);
//        ChessPiece blackPawn_02 = new Pawn(new Position(1, 1), PieceColor.BLACK);
//        ChessPiece blackPawn_03 = new Pawn(new Position(1, 2), PieceColor.BLACK);
//        ChessPiece blackPawn_04 = new Pawn(new Position(1, 3), PieceColor.BLACK);
//        ChessPiece blackPawn_05 = new Pawn(new Position(1, 4), PieceColor.BLACK);
//        ChessPiece blackPawn_06 = new Pawn(new Position(1, 5), PieceColor.BLACK);
//        ChessPiece blackPawn_07 = new Pawn(new Position(1, 6), PieceColor.BLACK);
//        ChessPiece blackPawn_08 = new Pawn(new Position(1, 7), PieceColor.BLACK);
//
//        ChessPiece blackRook_01 = new Rook(new Position(0, 0), PieceColor.BLACK);
//        ChessPiece blackRook_02 = new Rook(new Position(0, 7), PieceColor.BLACK);
//
//        ChessPiece blackKnight_01 = new Knight(new Position(0, 1), PieceColor.BLACK);
//        ChessPiece blackKnight_02 = new Knight(new Position(0, 6), PieceColor.BLACK);
//
//        ChessPiece blackBishop_01 = new Bishop(new Position(0, 2), PieceColor.BLACK);
//        ChessPiece blackBishop_02 = new Bishop(new Position(0, 5), PieceColor.BLACK);
//
//        ChessPiece blackKing = new King(new Position(0, 4), PieceColor.BLACK);
//
//        ChessPiece blackQueen = new Queen(new Position(0, 3), PieceColor.BLACK);
//
//        //define white pieces
//        ChessPiece whitePawn_01 = new Pawn(new Position(6, 0), PieceColor.WHITE);
//        ChessPiece whitePawn_02 = new Pawn(new Position(6, 1), PieceColor.WHITE);
//        ChessPiece whitePawn_03 = new Pawn(new Position(6, 2), PieceColor.WHITE);
//        ChessPiece whitePawn_04 = new Pawn(new Position(6, 3), PieceColor.WHITE);
//        ChessPiece whitePawn_05 = new Pawn(new Position(6, 4), PieceColor.WHITE);
//        ChessPiece whitePawn_06 = new Pawn(new Position(6, 5), PieceColor.WHITE);
//        ChessPiece whitePawn_07 = new Pawn(new Position(6, 6), PieceColor.WHITE);
//        ChessPiece whitePawn_08 = new Pawn(new Position(6, 7), PieceColor.WHITE);
//
//        ChessPiece whiteRook_01 = new Rook(new Position(7, 0), PieceColor.WHITE);
//        ChessPiece whiteRook_02 = new Rook(new Position(7, 7), PieceColor.WHITE);
//
//        ChessPiece whiteKnight_01 = new Knight(new Position(7, 1), PieceColor.WHITE);
//        ChessPiece whiteKnight_02 = new Knight(new Position(7, 6), PieceColor.WHITE);
//
//        ChessPiece whiteBishop_01 = new Bishop(new Position(7, 2), PieceColor.WHITE);
//        ChessPiece whiteBishop_02 = new Bishop(new Position(7, 5), PieceColor.WHITE);
//
//        ChessPiece whiteKing = new King(new Position(7, 4), PieceColor.WHITE);
//
//        ChessPiece whiteQueen = new Queen(new Position(7, 3), PieceColor.WHITE);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_game:
                intent = new Intent(GameOptions.this, GameOptions.class);
                break;
            case R.id.nav_signup:
                intent = new Intent(GameOptions.this, SignUp.class);
                break;
            case R.id.nav_online:
                //TODO
                break;
            case R.id.nav_regular:
                intent = new Intent(GameOptions.this, OfflineGame.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(GameOptions.this, UserProfile.class);
                break;
            case R.id.nav_login:
                intent = new Intent(GameOptions.this, Login.class);
                break;
            case R.id.nav_vsFriendOnline:
                open_join_create_game_Dialog();
                break;
            default:
                Toast.makeText(GameOptions.this, "", Toast.LENGTH_LONG).show();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void open_join_create_game_Dialog(View view) {

        //user isn't connected
        if (username_sp.equals("")) {
            guest = new Dialog(this);
            guest.setContentView(R.layout.must_connect_dialog);
            guest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            guest.setCancelable(true);
            guest.show();
        } else {
            create_join = new Dialog(this);
            create_join.setContentView(R.layout.join_start_game_dialog);
            create_join.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            create_join.setCancelable(true);
            create_join.show();
        }
    }

    public void open_join_create_game_Dialog() {
        //user isn't connected
        if (username_sp.equals("")) {
            guest = new Dialog(this);
            guest.setContentView(R.layout.must_connect_dialog);
            guest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            guest.setCancelable(true);
            guest.show();
        } else {
            create_join = new Dialog(this);
            create_join.setContentView(R.layout.join_start_game_dialog);
            create_join.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            create_join.setCancelable(true);
            create_join.show();
        }
    }

    public void open_game_code_Dialog(View view) {
        create_join.cancel();
        gameCode = new Dialog(this);
        gameCode.setContentView(R.layout.join_online_game_dialog);
        gameCode.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameCode.setCancelable(false);
        gameCode.show();
    }

    public void join_game(View view) {
        TextInputLayout input_code = gameCode.findViewById(R.id.code);
        String code = input_code.getEditText().getText().toString();

        if (!code.equals("")) {
            available_codes.orderByKey().equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //removes the game code
                        available_codes.orderByKey().equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    data.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        gameCode.cancel();
                        create_join.cancel();

                        games.child("ChessGame" + code).child("users").child("joiner").setValue(username_sp);
                        Intent game = new Intent(GameOptions.this, OnlineGame.class);
                        game.putExtra("game_code", "ChessGame" + code);
                        game.putExtra("player_type", "joiner");
                        startActivity(game);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void create_new_game(View view) {
        String gameId = generateRandomCode();

        available_codes.child(gameId).setValue(gameId);

        //creates the game in the DB
        games.child("ChessGame" + gameId).child("Board").setValue("");
        games.child("ChessGame" + gameId).child("Chat").setValue("");
        games.child("ChessGame" + gameId).child("users").child("creator").setValue(username_sp);

        create_join.cancel();
        openWaitDialog(gameId);

        DatabaseReference codeRef = FirebaseDatabase.getInstance().getReference("Available Codes").child(gameId);
        codeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                    waitDialog.cancel();

                    Intent game = new Intent(GameOptions.this, OnlineGame.class);
                    game.putExtra("game_code", "ChessGame" + gameId);
                    game.putExtra("player_type", "creator");
                    startActivity(game);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void openWaitDialog(String gameId) {
        waitDialog = new Dialog(this);
        waitDialog.setContentView(R.layout.wait_for_opponent_dialog);
        TextView gameCodeTxt = waitDialog.findViewById(R.id.gameCodeTxt);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        waitDialog.setCancelable(false);
        gameCodeTxt.setText("the Game Code is: "+ gameId);
        waitDialog.show();
    }

    public void toLoginFromGame(View view) {
        startActivity(new Intent(GameOptions.this, Login.class));
    }

    public void toSignUpFromGame(View view) {
        startActivity(new Intent(GameOptions.this, SignUp.class));
    }

    public String generateRandomCode() {
        Random random = new Random();
        String cap_letters = "ABCDEFGHJKMNPKRSTVWXYZ";
        String low_letters = "abcdefghjkmnpqrstuvwxyz";
        String numbers = "123456789";
        StringBuilder code = new StringBuilder();
        int length = 7;
        for (int i = 0; i < length; i++) {
            switch (random.nextInt(3)) {
                case 0:
                    code.append(cap_letters.toCharArray()[new Random().nextInt(cap_letters.length())]);
                    break;
                case 1:
                    code.append(low_letters.toCharArray()[new Random().nextInt(low_letters.length())]);
                    break;
                case 2:
                    code.append(numbers.toCharArray()[new Random().nextInt(numbers.length())]);
                    break;
            }
        }
        return code.toString();
    }

    public void drawBoard() {
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
                int x_pos = i * tileSize, y_pos = j * tileSize;
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
        ArrayList<Position> m_legalMoves = piece.legalMoves(board);
        for (int i = 0; i < m_legalMoves.size(); i++) {
            int x = m_legalMoves.get(i).getPos_x();
            int y = m_legalMoves.get(i).getPos_y();
            setPositionOfCircle(x, y, possibleMoves[i]);
            possibleMoves[i].startAnimation(animation1);
        }
    }

    public void movePiece(int current_x, int current_y, int move_x, int move_y) {
        ImageView piece = getPieceFromCanvasPos(current_x, current_y);
        setPiecePosition(move_x, move_y, piece);

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
}