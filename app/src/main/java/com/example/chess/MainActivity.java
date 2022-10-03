package com.example.chess;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity /* implements View.OnClickListener*/ {

    private TextView name, slogan;
    private ImageView img;
    private Animation top, bootom;

    private LottieAnimationView lottie;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lottie = findViewById(R.id.animationView);
        logo= findViewById(R.id.circle);

//        name = findViewById(R.id.name);
//        slogan = findViewById(R.id.slogan);
//        img = findViewById(R.id.logoImg);
//
        //top = AnimationUtils.loadAnimation(this, R.anim.top);
//        bootom = AnimationUtils.loadAnimation(this, R.anim.bottom);
//
//        img.setAnimation(top);
//        name.setAnimation(bootom);
//        slogan.setAnimation(bootom);


        //logo.setAnimation(top);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, Login.class);
//                Pair[] pairs = new Pair[2];
//                pairs[0]= new Pair<View,String>(logo, "logo_img");
//               pairs[1]= new Pair<View,String>(name, "logo_txt");

                //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.logo), "logo_img");
                //startActivity(i, options.toBundle());
                startActivity(i);
            }
        }, 4100);

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