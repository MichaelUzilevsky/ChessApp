package com.example.chess;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private Button login, toSignUp;
    private TextView main, slogan;
    private TextInputLayout username, password;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("User_Data", 0);

        main = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.go);
        toSignUp = findViewById(R.id.toOther);
        Intent intent_signUp = getIntent();
        if (intent_signUp != null) {
            String intent_username = intent_signUp.getStringExtra("username");
            String intent_password = intent_signUp.getStringExtra("password");

            username.getEditText().setText(intent_username);
            password.getEditText().setText(intent_password);
        }
    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            username.setError("Filed cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Filed cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void toSignup(View view) {

        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(main, "logo_txt");
        pairs[1] = new Pair<View, String>(slogan, "slogan");
        pairs[2] = new Pair<View, String>(username, "username");
        pairs[3] = new Pair<View, String>(password, "password");
        pairs[4] = new Pair<View, String>(login, "go_btn");
        pairs[5] = new Pair<View, String>(toSignUp, "toOther");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(new Intent(Login.this, SignUp.class), options.toBundle());

    }

    public void loginUser(View view) {
        if (!validatePassword() | !validateUsername())
            return;
        else {
            isUser();
        }
    }

    private void isUser() {
        String userEnterd_username = username.getEditText().getText().toString().trim();
        String userEntered_password = password.getEditText().getText().toString().trim();

        DatabaseReference users_reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = users_reference.orderByChild("username").equalTo(userEnterd_username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String password_DB = snapshot.child(userEnterd_username).child("password").getValue().toString();

                    if (password_DB.equals(userEntered_password)) {

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String name_DB = snapshot.child(userEnterd_username).child("name").getValue().toString();
                        String username_DB = snapshot.child(userEnterd_username).child("username").getValue().toString();
                        String email_DB = snapshot.child(userEnterd_username).child("email").getValue().toString();
                        String phone_DB = snapshot.child(userEnterd_username).child("phone").getValue().toString();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name_DB);
                        editor.putString("username", username_DB);
                        editor.putString("email", email_DB);
                        editor.putString("phone", phone_DB);
                        editor.putString("password", password_DB);
                        editor.apply();

                        Intent intent = new Intent(Login.this, GameOptions.class);
                        startActivity(intent);
                    } else {
                        password.setError("Wrong PassWord");
                        password.requestFocus();
                    }
                } else {
                    username.setError("Username doesn't exists");
                    password.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}