package com.example.chess;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Random;

public class GameOptions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Dialog create_join, guest, gameCode, waitDialog;
    private String username_sp, name_sp;
    private NavigationView navigationView;
    private DatabaseReference available_codes, games;
    private String opponentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);
        drawerLayout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        available_codes = FirebaseDatabase.getInstance().getReference("Available Codes");
        games = FirebaseDatabase.getInstance().getReference("Games");

        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_game).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        username_sp = sharedPreferences.getString("username", "");
        name_sp = sharedPreferences.getString("name", "");

        if (username_sp.equals("") && name_sp.equals("")) {
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_signup).setVisible(true);
        } else {
            menu.findItem(R.id.nav_profile).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_signup).setVisible(false);
        }
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
}