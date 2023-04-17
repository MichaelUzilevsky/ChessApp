package com.example.chess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity /* implements View.OnClickListener*/ {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LottieAnimationView lottie = findViewById(R.id.animationView);
        ImageView circle = findViewById(R.id.circle);
        ImageView logo = findViewById(R.id.logo);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("playing", false);
        editor.putInt("whiteR", 232);
        editor.putInt("whiteG", 231);
        editor.putInt("whiteB", 208);
        editor.putInt("blackR", 75);
        editor.putInt("blackG", 115);
        editor.putInt("blackB", 153);
        editor.apply();

        splash();
//        root = FirebaseDatabase.getInstance();
//        users_reference = root.getReference("Users");
//        board_reference = root.getReference("Board");
//
//        users_Event_Listener = users_reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String _name = snapshot.child("name").getValue().toString();
//                String _phone = snapshot.child("phone").getValue().toString();
//
//                name.setText(_name);
//                phone.setText(_phone);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        name = findViewById(R.id.name);
//        phone = findViewById(R.id.phone);
//        button = findViewById(R.id.btn);
//
//        button.setOnClickListener(this);
    }

    private void splash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, GameOptions.class);
                startActivity(i);
                finish();
            }
        }, 3540);
    }

    @Override
    protected void onResume() {
        splash();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        splash();
        super.onRestart();
    }


//    @Override
//    public void onClick(View view) {
//        if (view == button){
//            User user = new User(phone.getText().toString(),name.getText().toString());
//            //users_reference.child(phone.getText().toString()).setValue(user);
//            users_reference.setValue(user);
//
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        users_reference.removeEventListener(users_Event_Listener);
//    }

}