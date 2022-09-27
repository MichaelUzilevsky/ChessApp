package com.example.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity /* implements View.OnClickListener*/ {

    private TextView name, slogan;
    private ImageView img;
    private Animation top, bootom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);
        img = findViewById(R.id.logoImg);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bootom = AnimationUtils.loadAnimation(this, R.anim.bottom);

        img.setAnimation(top);
        name.setAnimation(bootom);
        slogan.setAnimation(bootom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0]= new Pair<View,String>(img, "logo_img");
                pairs[1]= new Pair<View,String>(name, "logo_txt");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(i, options.toBundle());
            }
        }, 2200);

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