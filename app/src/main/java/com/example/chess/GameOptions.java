package com.example.chess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class GameOptions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Dialog create_join, guest, gameCode, waitDialog, waitDialogOnline;
    private String username_sp, image_sp;
    private int score_val;
    private DatabaseReference available_codes, games;
    private String[] colors = new String[]{"white", "black"};
    private Random random = new Random();
    private SharedPreferences sharedPreferences;
    private LinearLayout loginButtons, guestButtons;
    private RelativeLayout scoreContainer;
    private TextView name, username, score;
    private ImageView img;
    private int REQUEST_CAMERA = 1, SELECT_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        loginButtons = findViewById(R.id.button_login);
        guestButtons = findViewById(R.id.button_guest);
        scoreContainer = findViewById(R.id.score_con);
        name = findViewById(R.id.fullname_label);
        username = findViewById(R.id.username_label);
        score = findViewById(R.id.score_val);
        img = findViewById(R.id.profile_image);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }

        });

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

        sharedPreferences = getSharedPreferences("User_Data", 0);
        username_sp = sharedPreferences.getString("username", "");
        String name_sp = sharedPreferences.getString("name", "");
        image_sp = sharedPreferences.getString("image", "");
        score_val = sharedPreferences.getInt("score", 0);

        if (username_sp.equals("") && name_sp.equals("")) {
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_signup).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            loginButtons.setVisibility(View.GONE);
            guestButtons.setVisibility(View.VISIBLE);
            scoreContainer.setVisibility(View.GONE);
            name.setText("Guest");
            username.setText("Login or Sign up to continue");

            setMargins(loginButtons, 0, 0, 0, 0);
            img.setImageResource(R.drawable.guest_icon);

        } else {
            menu.findItem(R.id.nav_profile).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_signup).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
            loginButtons.setVisibility(View.VISIBLE);
            guestButtons.setVisibility(View.GONE);
            scoreContainer.setVisibility(View.VISIBLE);
            name.setText(name_sp);
            username.setText(username_sp);
            score.setText(String.valueOf(score_val));
            if (!image_sp.equals(""))
                img.setImageBitmap(convertStringToBitmap(image_sp));
            else
                img.setImageResource(R.drawable.guest_icon);

            setMargins(loginButtons, 0, 25, 0, 0);

        }

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

    public String convertBitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap convertStringToBitmap(String encoded) {
        if (encoded == null)
            return null;
        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GameOptions.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(username_sp);
            if (requestCode == REQUEST_CAMERA) {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bitmap);
                user.child("image").setValue(convertBitmapToString(bitmap));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("image", convertBitmapToString(bitmap));
                editor.apply();
            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    img.setImageBitmap(bitmap);
                    user.child("image").setValue(convertBitmapToString(bitmap));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("image", convertBitmapToString(bitmap));
                    editor.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
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
                randomOnlineGame();
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
            case R.id.nav_settings:
                intent = new Intent(GameOptions.this, Settings.class);
                break;
            case R.id.nav_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "");
                editor.putString("username", "");
                editor.putString("email", "");
                editor.putString("phone", "");
                editor.putString("password", "");
                editor.putString("image", "");
                editor.putInt("score", 0);
                editor.commit();
                startActivity(new Intent(GameOptions.this, GameOptions.class));
                finish();
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

    public void randomOnlineGame(View view) {
        DatabaseReference rndOnline = FirebaseDatabase.getInstance().getReference("RandomOnline");
        rndOnline.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //login to an existing game
                    String code = snapshot.getValue().toString();
                    rndOnline.setValue(null);
                    games.child("ChessGame" + code).child("users").child("joiner").setValue(username_sp);
                    Intent game = new Intent(GameOptions.this, OnlineGame.class);
                    setMyColor(code, game);
                    game.putExtra("game_code", "ChessGame" + code);
                    game.putExtra("player_type", "joiner");
                    startActivity(game);
                    finish();

                } else {
                    //create new game
                    String gameId = generateRandomCode();
                    rndOnline.setValue(gameId);
                    //creates the game in the DB
                    games.child("ChessGame" + gameId).child("Chat").setValue("");
                    games.child("ChessGame" + gameId).child("users").child("creator").setValue(username_sp);
                    String myColor = randomColor();
                    games.child("ChessGame" + gameId).child("colors").child(username_sp).setValue(myColor);
                    games.child("ChessGame" + gameId).child("firstColor").setValue(myColor);
                    games.child("ChessGame" + gameId).child("Status");
                    games.child("ChessGame" + gameId).child("Online").setValue("Online");

                    openWaitDialogOnline();

                    rndOnline.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {

                                games.child("ChessGame" + gameId).child("users").child("joiner").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            waitDialogOnline.cancel();
                                            Intent game = new Intent(GameOptions.this, OnlineGame.class);
                                            game.putExtra("game_code", "ChessGame" + gameId);
                                            game.putExtra("player_type", "creator");
                                            game.putExtra("myColor", myColor);
                                            startActivity(game);
                                            finish();
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void randomOnlineGame() {
        DatabaseReference rndOnline = FirebaseDatabase.getInstance().getReference("RandomOnline");
        rndOnline.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //login to an existing game
                    String code = snapshot.getValue().toString();
                    rndOnline.setValue(null);
                    games.child("ChessGame" + code).child("users").child("joiner").setValue(username_sp);
                    Intent game = new Intent(GameOptions.this, OnlineGame.class);
                    setMyColor(code, game);
                    game.putExtra("game_code", "ChessGame" + code);
                    game.putExtra("player_type", "joiner");
                    startActivity(game);
                    finish();

                } else {
                    //create new game
                    String gameId = generateRandomCode();
                    rndOnline.setValue(gameId);
                    //creates the game in the DB
                    games.child("ChessGame" + gameId).child("Chat").setValue("");
                    games.child("ChessGame" + gameId).child("users").child("creator").setValue(username_sp);
                    String myColor = randomColor();
                    games.child("ChessGame" + gameId).child("colors").child(username_sp).setValue(myColor);
                    games.child("ChessGame" + gameId).child("firstColor").setValue(myColor);
                    games.child("ChessGame" + gameId).child("Status");
                    games.child("ChessGame" + gameId).child("Online").setValue("Online");

                    openWaitDialogOnline();

                    rndOnline.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {

                                games.child("ChessGame" + gameId).child("users").child("joiner").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            waitDialogOnline.cancel();
                                            Intent game = new Intent(GameOptions.this, OnlineGame.class);
                                            game.putExtra("game_code", "ChessGame" + gameId);
                                            game.putExtra("player_type", "creator");
                                            game.putExtra("myColor", myColor);
                                            startActivity(game);
                                            finish();
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void open_game_code_Dialog(View view) {
        create_join.cancel();
        gameCode = new Dialog(this);
        gameCode.setContentView(R.layout.join_online_game_dialog);
        gameCode.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameCode.setCancelable(true);
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
                                games.child("ChessGame" + code).child("users").child("joiner").setValue(username_sp);
                                Intent game = new Intent(GameOptions.this, OnlineGame.class);
                                setMyColor(code, game);
                                game.putExtra("game_code", "ChessGame" + code);
                                game.putExtra("player_type", "joiner");
                                startActivity(game);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        gameCode.cancel();
                        create_join.cancel();

                    } else {
                        Toast.makeText(GameOptions.this, "Game Code Does Not Exists", Toast.LENGTH_SHORT).show();
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
        games.child("ChessGame" + gameId).child("Chat").setValue("");
        games.child("ChessGame" + gameId).child("users").child("creator").setValue(username_sp);
        String myColor = randomColor();
        games.child("ChessGame" + gameId).child("colors").child(username_sp).setValue(myColor);
        games.child("ChessGame" + gameId).child("firstColor").setValue(myColor);
        games.child("ChessGame" + gameId).child("Status");

        create_join.cancel();
        openWaitDialog(gameId);

        DatabaseReference codeRef = FirebaseDatabase.getInstance().getReference("Available Codes");
        Query query = codeRef.orderByKey().equalTo(gameId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                    games.child("ChessGame" + gameId).child("users").child("joiner").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                waitDialog.cancel();

                                Intent game = new Intent(GameOptions.this, OnlineGame.class);
                                game.putExtra("game_code", "ChessGame" + gameId);
                                game.putExtra("player_type", "creator");
                                game.putExtra("myColor", myColor);
                                startActivity(game);
                                finish();
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


    }

    public String randomColor() {
        return colors[random.nextInt(2)];
    }

    public void openWaitDialog(String gameId) {
        waitDialog = new Dialog(this);
        waitDialog.setContentView(R.layout.wait_for_opponent_dialog);
        TextView gameCodeTxt = waitDialog.findViewById(R.id.gameCodeTxt);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        waitDialog.setCancelable(false);
        gameCodeTxt.setText("the Game Code is: " + gameId);
        waitDialog.show();
    }

    public void openWaitDialogOnline() {
        waitDialogOnline = new Dialog(this);
        waitDialogOnline.setContentView(R.layout.wait_for_opponent_dialog_online);
        waitDialogOnline.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        waitDialogOnline.setCancelable(false);
        waitDialogOnline.show();
    }

    public void toLoginFromGame(View view) {
        startActivity(new Intent(GameOptions.this, Login.class));
        finish();
    }

    public void toSignUpFromGame(View view) {
        startActivity(new Intent(GameOptions.this, SignUp.class));
        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (create_join != null && guest != null && gameCode != null && waitDialog != null) {
            create_join.dismiss();
            guest.dismiss();
            gameCode.dismiss();
            waitDialog.dismiss();
        }
    }

    public void setMyColor(String code, Intent i) {
        games.child("ChessGame" + code).child("firstColor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String c = snapshot.getValue().toString();
                    if (c.equals("white")) {
                        games.child("ChessGame" + code).child("colors").child(username_sp).setValue("black");
                        i.putExtra("myColor", "black");
                    } else {
                        games.child("ChessGame" + code).child("colors").child(username_sp).setValue("white");
                        i.putExtra("myColor", "black");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeGame(View view) {
        TextView codeHandler = waitDialog.findViewById(R.id.gameCodeTxt);
        String code = codeHandler.getText().toString().substring(18);
        available_codes.child(code).setValue(null);
        games.child("ChessGame" + code).setValue(null);
        waitDialog.cancel();
    }

    public void toOffline(View view) {
        startActivity(new Intent(GameOptions.this, OfflineGame.class));
    }

    public void removeGameOnline(View view) {
        DatabaseReference rndOnline = FirebaseDatabase.getInstance().getReference("RandomOnline");
        rndOnline.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String code = snapshot.getValue().toString();
                DatabaseReference game = FirebaseDatabase.getInstance().getReference("Games").child("ChessGame" + code);
                game.setValue(null);
                rndOnline.setValue(null);
                waitDialogOnline.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
