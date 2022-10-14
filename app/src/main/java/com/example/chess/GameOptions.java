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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameOptions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Dialog codeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);
        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_game);

        String username_sp = sharedPreferences.getString("username", "");
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
                intent = new Intent(GameOptions.this, OnlineGame.class);
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
            default:
                Toast.makeText(GameOptions.this, "", Toast.LENGTH_LONG).show();
                break;
        }
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDialog(View view) {
        codeDialog = new Dialog(this);
        codeDialog.setContentView(R.layout.online_game_dialog);
        codeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        codeDialog.setCancelable(true);
        codeDialog.show();
    }

    public void start_new_game(View view) {
        TextInputLayout input_code = codeDialog.findViewById(R.id.code);
        String code = input_code.getEditText().getText().toString();

        if (!code.equals("")) {
            DatabaseReference available_codes = FirebaseDatabase.getInstance().getReference("Available Codes");
            DatabaseReference games = FirebaseDatabase.getInstance().getReference("Games");

            String randomValue = generateRandomCode() ;

            available_codes.orderByKey().equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        //creates the game in the DB
                        games.child(randomValue).child("Board").setValue("");
                        games.child(randomValue).child("Chat").setValue("");

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

                        Intent game = new Intent(GameOptions.this, OnlineGame.class);
                        game.putExtra("game_code", randomValue);
                        startActivity(game);

                    }
                    else {
                        available_codes.child(code).setValue(code);

                        codeDialog = new Dialog(GameOptions.this);
                        openWaitDialog();


                        DatabaseReference codeRef = FirebaseDatabase.getInstance().getReference("Available Codes").child(code);
                        codeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() == null) {
                                    Intent game = new Intent(GameOptions.this, OnlineGame.class);
                                    game.putExtra("game_code", randomValue);
                                    startActivity(game);
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

    public String generateRandomCode() {
        Random random = new Random();
        String cap_letters = "ABCDEFGHIJKLMNOPKRSTOVWXYZ";
        String low_letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "1234567890";
        StringBuilder code = new StringBuilder().append("ChessGame");
        int length = 16;
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

    public void openWaitDialog() {
        Dialog waitDialog = new Dialog(this);
        waitDialog.setContentView(R.layout.waiting);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        waitDialog.setCancelable(true);
        waitDialog.show();
    }
}