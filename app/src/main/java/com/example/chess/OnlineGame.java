package com.example.chess;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chess.BoardDesign.Tile;
import com.example.chess.chat.ChatAdapter;
import com.example.chess.chat.ChatList;
import com.example.chess.game.Board;
import com.example.chess.pieces.ChessPiece;
import com.example.chess.pieces.King;
import com.example.chess.pieces.Pawn;
import com.example.chess.pieces.PieceColor;
import com.example.chess.utils.Position;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlineGame extends AppCompatActivity {
    private String gameCode;
    Dialog checkmate_dialog, draw_dialog,quitD ;
    private SharedPreferences preferences;
    private String myUsername, myPlayerType, opponentPlayerType, opponentUsername, myColor;
    private EditText message;
    private RecyclerView chat;
    private List<ChatList> messages;
    private ChatAdapter chatAdapter;
    private DatabaseReference chatReference;
    private DatabaseReference myLastMsg_reference;
    private DatabaseReference opponentLastMsg_reference;
    private DatabaseReference opponentUsername_ref;
    private DatabaseReference myColor_ref;
    private DatabaseReference game_ref;
    private DatabaseReference lastMove_White_ref;
    private DatabaseReference lastMove_Black_ref;
    private TextView oppoUsername_textView, oppoName_textView, myUsername_textView, myName_textView, oppoScore_textView, myScore_textView;
    private int width, height;
    private int currentPiece_x, currentPiece_y;
    private Board board;
    private boolean pressed = false;
    private final int[] WHITE = new int[]{232, 231, 208};
    private final int[] BLACK = new int[]{75, 115, 153};
    private final Tile[][] tiles = new Tile[8][8];
    private RelativeLayout chatContainer, chessBoard;
    private ImageView[][] white, black;
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

    private ImageView[] possibleMoves;
    private final double CIRCLE_SIZE = 0.4;
    private AbsoluteLayout pieces;
    private PieceColor turn = PieceColor.WHITE;
    private PieceColor myColor_;
    private CircleImageView myImg, oppoImg;
    private boolean toScore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_game);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels - 2;
        height = displayMetrics.heightPixels;

        Intent gameCodeIntent = getIntent();
        gameCode = gameCodeIntent.getStringExtra("game_code");
        myPlayerType = gameCodeIntent.getStringExtra("player_type");
        myColor = gameCodeIntent.getStringExtra("myColor");

        oppoName_textView = findViewById(R.id.oppoName);
        oppoUsername_textView = findViewById(R.id.oppUsername);
        myUsername_textView = findViewById(R.id.myUsername);
        myName_textView = findViewById(R.id.myName);
        myScore_textView = findViewById(R.id.my_score);
        oppoScore_textView = findViewById(R.id.oppo_score);
        myImg = findViewById(R.id.my_profile_image);
        oppoImg = findViewById(R.id.oppo_profile_image);

        if (myPlayerType.equals("joiner")) {
            opponentPlayerType = "creator";
        } else {
            opponentPlayerType = "joiner";
            if (myColor.equals("white"))
                myColor_ = PieceColor.WHITE;
            else
                myColor_ = PieceColor.BLACK;

        }
        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);

        WHITE[0] = sharedPreferences.getInt("whiteR", 232);
        WHITE[1] = sharedPreferences.getInt("whiteG", 231);
        WHITE[2] = sharedPreferences.getInt("whiteB", 208);

        BLACK[0] = sharedPreferences.getInt("blackR", 75);
        BLACK[1] = sharedPreferences.getInt("blackG", 115);
        BLACK[2] = sharedPreferences.getInt("blackB", 153);

        preferences = getSharedPreferences("User_Data", 0);
        myUsername = preferences.getString("username", null);

        chatReference = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("Chat");
        myLastMsg_reference = chatReference.child(myPlayerType + " last message");
        opponentLastMsg_reference = chatReference.child(opponentPlayerType + " last message");
        opponentUsername_ref = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("users").child(opponentPlayerType);

        myLastMsg_reference.setValue("");
        opponentLastMsg_reference.setValue("");

        messages = new ArrayList<ChatList>();

        message = findViewById(R.id.messageEditTxt);
        chat = findViewById(R.id.chatting);
        chatContainer = findViewById(R.id.chatContainer);


        chat.setHasFixedSize(true);
        chat.setLayoutManager(new LinearLayoutManager(OnlineGame.this));
        chatAdapter = new ChatAdapter(messages, OnlineGame.this, myUsername);
        chat.setAdapter(chatAdapter);

        chessBoard = findViewById(R.id.chessBoard);
        game_ref = FirebaseDatabase.getInstance().getReference("Games").child(gameCode);
        lastMove_Black_ref = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("lastMove_black");
        lastMove_White_ref = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("lastMove_white");


        opponentLastMsg_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && !snapshot.getValue().equals("")) {
                    String msg = snapshot.child("message").getValue().toString();
                    String sender = snapshot.child("sender").getValue().toString();
                    if (!msg.equals("") && !sender.equals("")) {
                        messages.add(new ChatList(sender, msg));
                        chatAdapter.updateChatList(messages);
                        chat.scrollToPosition(messages.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        board = new Board();

        pieces = findViewById(R.id.pieces);
        ViewGroup.LayoutParams params = pieces.getLayoutParams();
        params.height = width;
        params.width = width;
        pieces.setLayoutParams(params);

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


        opponentUsername_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                opponentUsername = snapshot.getValue(String.class);

                DatabaseReference users_reference = FirebaseDatabase.getInstance().getReference("Users");
                Query checkUser = users_reference.orderByChild("username").equalTo(opponentUsername);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        oppoUsername_textView.setText(opponentUsername);
                        String name_DB = snapshot.child(opponentUsername).child("name").getValue().toString();
                        String image = snapshot.child(opponentUsername).child("image").getValue().toString();
                        int score = snapshot.child(opponentUsername).child("score").getValue(Integer.class);

                        oppoName_textView.setText(name_DB);
                        oppoScore_textView.setText(score+"");
                        oppoImg.setImageBitmap(convertStringToBitmap(image));
                        myUsername_textView.setText(myUsername);
                        myName_textView.setText(preferences.getString("name", "noName"));
                        myScore_textView.setText(String.valueOf(preferences.getInt("score", 0)));
                        myImg.setImageBitmap(convertStringToBitmap(preferences.getString("image", "")));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OnlineGame.this, "Failed to read value." + error.toException(), Toast.LENGTH_SHORT).show();
            }
        });

        myColor_ref = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("firstColor");
        myColor_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PieceColor color;
                if (myPlayerType.equals("joiner")) {
                    if (snapshot.exists() && snapshot.getValue(String.class).equals("white")) {
                        color = PieceColor.BLACK;
                    } else {
                        color = PieceColor.WHITE;
                    }
                }
                else {
                    color = myColor_;
                }

                if (color.equals(PieceColor.WHITE)) {
                    drawBoard_white();
                    arrangeBoard(PieceColor.WHITE);
                }
                else {
                    drawBoard_white();
                    arrangeBoard(PieceColor.BLACK);
                }

                pieces.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            int x = (int) event.getX();
                            int y = (int) event.getY();
                            int tile_x = getTileId(x, y, color)[1];
                            int tile_y = getTileId(x, y, color)[0];


                            String check = "";

                            //no piece is selected
                            if (!pressed) {
                                if (board.isPiece(tile_x, tile_y, board.getBoard())) {
                                    PieceColor pieceColor = board.getPiece(tile_x, tile_y, board.getBoard()).getColor();
                                    if (pieceColor.equals(color) && turn.equals(pieceColor)) {
                                        setInvisible();
                                        showLegalMoves(board.getPiece(tile_x, tile_y, board.getBoard()), color);
                                        pressed = true;
                                        currentPiece_x = tile_x;
                                        currentPiece_y = tile_y;
                                    }
                                }
                            }
                            //piece selected
                            else {
                                if (board.canMove(currentPiece_x, currentPiece_y, tile_x, tile_y, board.getBoard())) {
                                    if (board.shouldEatPiece(tile_x, tile_y, board.getPiece(currentPiece_x, currentPiece_y, board.getBoard()), board.getBoard())) {
                                        board.eatPiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                        removePieceImg(tile_x, tile_y, color);
                                        movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y, color);
                                        playEatSound();
                                    }
                                    else {
                                        if (board.getPiece(currentPiece_x, currentPiece_y, board.getBoard()) instanceof King) {
                                            //king size castle
                                            if (tile_y - currentPiece_y == 2) {
                                                board.kingSideCastle(turn);
                                                movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y, color);
                                                rookCastle(turn, -2);
                                                check = "K";
                                            }
                                            //queen side castle
                                            else if (tile_y - currentPiece_y == -2) {
                                                board.queenSideCastle(turn);
                                                movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y, color);
                                                rookCastle(turn, 3);
                                                check = "Q";
                                            } else {
                                                board.movePiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                                movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y, color);
                                            }
                                        } else {

                                            board.movePiece(currentPiece_x, currentPiece_y, tile_x, tile_y);
                                            movePieceImg(currentPiece_x, currentPiece_y, tile_x, tile_y, color);
                                        }
                                    }

                                    if (board.getPiece(tile_x, tile_y, board.getBoard()) instanceof Pawn
                                            && board.checkTransformation(tile_x)) {
                                        if (board.getPiece(tile_x, tile_y, board.getBoard()).getColor().equals(PieceColor.WHITE)) {
                                            getPieceFromCanvasPos(tile_x, tile_y).setImageResource(R.drawable.white_queen);
                                            board.setNewQueen(tile_x, tile_y, PieceColor.WHITE);
                                        } else {
                                            getPieceFromCanvasPos(7 - tile_x, 7 - tile_y).setImageResource(R.drawable.black_queen);
                                            board.setNewQueen(tile_x, tile_y, PieceColor.BLACK);
                                        }
                                        check = "T";
                                    }

                                    if (board.checkDraw(turn)) {
                                        drawDialog();
                                        check += "=";
                                    }
                                    else {
                                        switchTurn();

                                        if (board.checkCheckToColor(turn)) {
                                            if (board.checkMateTOColor(turn)) {
                                                switchTurn();
                                                checkmateDialog(turn);
                                                check += "*";
                                            } else {
                                                checkDialog();
                                                check += "+";
                                            }
                                        }
                                    }

                                    if (color.equals(PieceColor.WHITE)) {
                                        lastMove_White_ref.setValue(currentPiece_x + "" + currentPiece_y + "" + tile_x + "" + tile_y + check);
                                    }
                                    else {
                                        lastMove_Black_ref.setValue(currentPiece_x + "" + currentPiece_y + "" + tile_x + "" + tile_y + check);
                                    }

                                    pressed = false;
                                    setInvisible();

                                } else {
                                    pressed = false;
                                    if (board.isPiece(tile_x, tile_y, board.getBoard())) {
                                        PieceColor pieceColor = board.getPiece(tile_x, tile_y, board.getBoard()).getColor();
                                        if (turn.equals(pieceColor)) {
                                            setInvisible();
                                            showLegalMoves(board.getPiece(tile_x, tile_y, board.getBoard()), color);
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

                if (color.equals(PieceColor.WHITE)) {
                    lastMove_Black_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String key = snapshot.getValue().toString();
                                int current_x = Integer.parseInt(String.valueOf(key.toCharArray()[0]));
                                int current_y = Integer.parseInt(String.valueOf(key.toCharArray()[1]));
                                int move_x = Integer.parseInt(String.valueOf(key.toCharArray()[2]));
                                int move_y = Integer.parseInt(String.valueOf(key.toCharArray()[3]));

                                if (board.isPiece(move_x, move_y, board.getBoard())) {
                                    removePieceImg(move_x, move_y, PieceColor.WHITE);
                                }
                                board.getBoard()[move_x][move_y] = board.getBoard()[current_x][current_y];
                                board.getBoard()[move_x][move_y].setPosition(new Position(move_x, move_y));
                                //move the piece
                                board.getBoard()[current_x][current_y] = null;
                                movePieceImg(current_x, current_y, move_x, move_y, PieceColor.WHITE);
                                switchTurn();
                                //special cases
                                if (key.length() >= 5) {
                                    String opp1 = String.valueOf(key.toCharArray()[4]);
                                    String opp2 = "";
                                    if (key.length() > 5) {
                                        opp2 = String.valueOf(key.toCharArray()[5]);
                                    }
                                    switch (opp1) {
                                        //draw
                                        case "=":
                                            drawDialog();
                                            break;
                                        //check
                                        case "+":
                                            checkDialog();
                                            break;
                                        //mate
                                        case "*":
                                            checkmateDialog(PieceColor.BLACK);
                                            break;
                                        //king side castle
                                        case "K":
                                            board.getBoard()[0][5] = board.getBoard()[0][7];
                                            board.getBoard()[0][5].setPosition(new Position(0, 5));
                                            board.getBoard()[0][7] = null;
                                            movePieceImg(0, 7, 0, 5, PieceColor.WHITE);
                                            break;
                                        //queen side castle
                                        case "Q":
                                            board.getBoard()[0][3] = board.getBoard()[0][0];
                                            board.getBoard()[0][3].setPosition(new Position(0, 3));
                                            board.getBoard()[0][0] = null;
                                            movePieceImg(0, 0, 0, 3, PieceColor.WHITE);
                                            break;
                                        //transformation
                                        case "T":
                                            getPieceFromCanvasPos(move_x, move_y).setImageResource(R.drawable.black_queen);
                                            board.setNewQueen(move_x, move_y, PieceColor.BLACK);
                                            break;
                                    }
                                    switch (opp2) {
                                        //draw
                                        case "=":
                                            drawDialog();
                                            break;
                                        //check
                                        case "+":
                                            checkDialog();
                                            break;
                                        //mate
                                        case "*":
                                            checkmateDialog(PieceColor.BLACK);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    lastMove_White_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String key = snapshot.getValue().toString();
                                int current_x = Integer.parseInt(String.valueOf(key.toCharArray()[0]));
                                int current_y = Integer.parseInt(String.valueOf(key.toCharArray()[1]));
                                int move_x = Integer.parseInt(String.valueOf(key.toCharArray()[2]));
                                int move_y = Integer.parseInt(String.valueOf(key.toCharArray()[3]));
                                if (board.isPiece(move_x, move_y, board.getBoard())) {
                                    removePieceImg(move_x, move_y, PieceColor.BLACK);
                                }
                                board.getBoard()[move_x][move_y] = board.getBoard()[current_x][current_y];
                                board.getBoard()[move_x][move_y].setPosition(new Position(move_x, move_y));
                                board.getBoard()[current_x][current_y] = null;
                                movePieceImg(current_x, current_y, move_x, move_y, PieceColor.BLACK);
                                switchTurn();
                                if (key.length() >= 5) {
                                    String opp1 = String.valueOf(key.toCharArray()[4]);
                                    String opp2 = "";
                                    if (key.length() > 5) {
                                        opp2 = String.valueOf(key.toCharArray()[5]);
                                    }
                                    switch (opp1) {
                                        //draw
                                        case "=":
                                            drawDialog();
                                            break;
                                        //check
                                        case "+":
                                            checkDialog();
                                            break;
                                        //mate
                                        case "*":
                                            checkmateDialog(PieceColor.WHITE);
                                            break;
                                        //king side castle
                                        case "K":
                                            board.getBoard()[7][5] = board.getBoard()[7][7];
                                            board.getBoard()[7][5].setPosition(new Position(7, 5));
                                            board.getBoard()[7][0] = null;
                                            movePieceImg(7, 7, 7, 5, PieceColor.BLACK);
                                            break;
                                        //queen side castle
                                        case "Q":
                                            board.getBoard()[7][3] = board.getBoard()[7][0];
                                            board.getBoard()[7][3].setPosition(new Position(7, 3));
                                            board.getBoard()[7][0] = null;
                                            movePieceImg(7, 0, 7, 3, PieceColor.BLACK);
                                            break;
                                        //transformation
                                        case "T":
                                            getPieceFromCanvasPos(7 - move_x, 7 - move_y).setImageResource(R.drawable.white_queen);
                                            board.setNewQueen(7 - move_x, 7 - move_y, PieceColor.WHITE);
                                            break;
                                    }
                                    switch (opp2) {
                                        //draw
                                        case "=":
                                            drawDialog();
                                            break;
                                        //check
                                        case "+":
                                            checkDialog();
                                            break;
                                        //mate
                                        case "*":
                                            checkmateDialog(PieceColor.WHITE);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        game_ref.child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(Objects.equals(snapshot.getValue(), "rematch")){
                        Toast.makeText(OnlineGame.this, "REMATCH REQUESTED", Toast.LENGTH_LONG).show();
                        game_ref.child("Status").setValue("");
                    }
                    else if(snapshot.getValue().toString().length() == 5){
                        if(myPlayerType.equals("joiner")){
                            Toast.makeText(OnlineGame.this, "OPPONENT LEFT YOU WON!!!", Toast.LENGTH_LONG).show();
                            game_ref.child("Status").setValue("");
                            game_ref.setValue(null);
                            startActivity(new Intent(OnlineGame.this, GameOptions.class));
                            finish();
                            if(toScore) {
                                SharedPreferences.Editor editor = preferences.edit();
                                int score = (int) (preferences.getInt("score", 0));
                                editor.putInt("score",(int)(score  * 1.01));
                                editor.apply();
                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(myUsername);
                                user.child("score").setValue((int)(score  * 1.01));

                            }

                        }
                    }
                    else if(snapshot.getValue().equals("Left")){
                        if(myPlayerType.equals("creator")){
                            Toast.makeText(OnlineGame.this, "OPPONENT LEFT  YOU WON!!!", Toast.LENGTH_LONG).show();
                            game_ref.child("Status").setValue("");
                            game_ref.setValue(null);
                            startActivity(new Intent(OnlineGame.this, GameOptions.class));
                            finish();
                            if(toScore) {
                                SharedPreferences.Editor editor = preferences.edit();
                                int score = (int) (preferences.getInt("score", 0));
                                editor.putInt("score",(int)(score  * 1.01));
                                editor.apply();
                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(myUsername);
                                user.child("score").setValue((int)(score  * 1.01));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        game_ref.child("Online").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue().toString().equals("Online"))
                    toScore = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Bitmap convertStringToBitmap(String encoded) {
        if (encoded == null)
            return null;
        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    public void onBackPressed() {
        quitD = new Dialog(this);
        quitD.setContentView(R.layout.quit_dialog);
        quitD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quitD.setCancelable(true);
        quitD.show();
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

    public void sendMessage(View view) {
        String msg = message.getText().toString();
        if (!msg.equals("")) {
            //create chat message
            ChatList chatList = new ChatList(myUsername, msg);

            //add to last sent message
            myLastMsg_reference.setValue(chatList);

            //add to all messages
            DatabaseReference allMessages_reference = chatReference.child("all messages");
            String messageId = myUsername + generateMessageKey();
            allMessages_reference.child(messageId).setValue(chatList);

            //add to recycle view
            messages.add(chatList);
            chatAdapter.updateChatList(messages);
            chat.scrollToPosition(messages.size() - 1);

            message.setText("");
        }
    }

    public String generateMessageKey() {
        Random random = new Random();
        String cap_letters = "ABCDEFGHIJKLMNOPKRSTOVWXYZ";
        String low_letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "1234567890";
        StringBuilder code = new StringBuilder().append("Message");
        int length = 6;
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

    public int[] getTileId(int x, int y, PieceColor color) {
        int row = x / (width / 8);
        int col = y / (width / 8);
        if (color.equals(PieceColor.WHITE)) {
            return new int[]{row, col};
        } else {
            return new int[]{7 - row, 7 - col};
        }

    }

    public void setInvisible() {
        for (ImageView i : possibleMoves) {
            i.setX(-1000);
            i.setY(-1000);
        }
    }

    public void showLegalMoves(ChessPiece piece, PieceColor color) {
        AlphaAnimation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(250);
        animation1.setFillAfter(true);
        ArrayList<Position> m_legalMoves = piece.legalMoves(board, board.getBoard());
        for (int i = 0; i < m_legalMoves.size(); i++) {
            int x = m_legalMoves.get(i).getPos_x();
            int y = m_legalMoves.get(i).getPos_y();
            if (color.equals(PieceColor.WHITE)) {
                setPositionOfCircle(x, y, possibleMoves[i]);
            } else {
                setPositionOfCircle(7 - x, 7 - y, possibleMoves[i]);
            }
            possibleMoves[i].startAnimation(animation1);
        }
    }

    public void movePieceImg(int current_x, int current_y, int move_x, int move_y, PieceColor color) {
        ImageView piece;
        if (color.equals(PieceColor.BLACK)) {
            piece = getPieceFromCanvasPos(7 - current_x, 7 - current_y);
        } else {
            piece = getPieceFromCanvasPos(current_x, current_y);
        }
        setPiecePosition(move_x, move_y, piece, color);

    }

    public void removePieceImg(int x, int y, PieceColor color) {
        movePieceImg(x, y, -999, -999, color);
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

    public void setPiecePosition(int x, int y, ImageView piece, PieceColor color) {
        int y_pos, x_pos;
        if (color.equals(PieceColor.WHITE)) {
            y_pos = (int) (x * (width / 8));
            x_pos = (int) (y * (width / 8));
        } else {
            y_pos = (int) ((7 - x) * (width / 8));
            x_pos = (int) ((7 - y) * (width / 8));
        }
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
        MediaPlayer mediaPlayer = MediaPlayer.create(OnlineGame.this, R.raw.man_eating);
        mediaPlayer.start();
    }

    public void playCheckSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(OnlineGame.this, R.raw.check_sound);
        mediaPlayer.start();
    }

    public void playCheckmateSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(OnlineGame.this, R.raw.checkmate_sound);
        mediaPlayer.start();
    }

    public void switchTurn() {
        if (turn.equals(PieceColor.WHITE))
            turn = PieceColor.BLACK;
        else
            turn = PieceColor.WHITE;
    }

    //king side x = -2, queen side x = 3
    public void rookCastle(PieceColor color, int x) {
        if (color.equals(PieceColor.WHITE)) {
            //king side
            if (x == -2) {
                movePieceImg(7, 7, 7, 5, PieceColor.WHITE);
            }
            //queen
            else if (x == 3) {
                movePieceImg(7, 0, 7, 3, PieceColor.WHITE);
            }
        } else {
            //king side
            if (x == -2) {
                movePieceImg(0, 7, 0, 5, PieceColor.BLACK);
            }
            //queen side
            else if (x == 3) {
                movePieceImg(0, 0, 0, 3, PieceColor.BLACK);
            }
        }
    }

    public void setPositionOfCircle(int x, int y, ImageView circle) {
        int y_pos = (int) (x * (width / 8) + ((1 - CIRCLE_SIZE) * (width / 8)) / 1.9);
        int x_pos = (int) (y * (width / 8) + ((1 - CIRCLE_SIZE) * (width / 8)) / 1.9);
        circle.setX(x_pos);
        circle.setY(y_pos);
        circle.setVisibility(View.VISIBLE);
    }

    public void checkDialog() {
        playCheckSound();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.root));
        TextView txt = layout.findViewById(R.id.txt);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void checkmateDialog(PieceColor color) {

        checkmate_dialog = new Dialog(this);
        checkmate_dialog.setContentView(R.layout.checkmate_dialog);
        checkmate_dialog.setCancelable(true);
        TextView winner = checkmate_dialog.findViewById(R.id.winner);
        if (color.equals(PieceColor.WHITE)) {
            winner.setText("White Won");
        } else {
            winner.setText("Black Won");
        }
        checkmate_dialog.setCancelable(true);
        playCheckmateSound();
        checkmate_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkmate_dialog.show();
    }

    public void drawDialog() {
        draw_dialog = new Dialog(this);
        draw_dialog.setContentView(R.layout.draw_dialog);
        draw_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        draw_dialog.setCancelable(true);
        draw_dialog.show();
    }

    public void rematch(View view) {
        myColor_ref = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("firstColor");
        myColor_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PieceColor color;
                if (myPlayerType.equals("creator")) {
                    if (snapshot.exists() && snapshot.getValue(String.class).equals("white")) {
                        color = PieceColor.WHITE;
                    } else {
                        color = PieceColor.BLACK;
                    }
                } else {
                    if (snapshot.exists() && snapshot.getValue(String.class).equals("white")) {
                        color = PieceColor.BLACK;
                    } else {
                        color = PieceColor.WHITE;
                    }
                }

                board = new Board();
                setInvisible();
                arrangeBoard(color);
                turn = PieceColor.WHITE;

                if(draw_dialog!=null)
                    draw_dialog.dismiss();
                if(checkmate_dialog!=null)
                    checkmate_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        game_ref.child("Status").setValue("rematch");
    }

    public void toMain(View view) {
        if(myPlayerType.equals("creator")){
            game_ref.child("Status").setValue("LeftC");
            if(toScore) {
                SharedPreferences.Editor editor = preferences.edit();
                int score = (int) (preferences.getInt("score", 0));
                editor.putInt("score",(int)(score  * 0.99));
                editor.apply();
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(myUsername);
                user.child("score").setValue((int)(score  * 0.99));
            }
        }
        else{
            game_ref.child("Status").setValue("Left");
            if(toScore) {
                SharedPreferences.Editor editor = preferences.edit();
                int score = (int) (preferences.getInt("score", 0));
                editor.putInt("score",(int)(score  * 0.99));
                editor.apply();
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(myUsername);
                user.child("score").setValue((int)(score  * 0.99));
            }
        }
        game_ref.setValue(null);
        startActivity(new Intent(OnlineGame.this, GameOptions.class));
        finish();
    }

    public void cancel(View view) {
        quitD.cancel();
    }

    public void quit(View view) {
        toMain(view);
    }
}