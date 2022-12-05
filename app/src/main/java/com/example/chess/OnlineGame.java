package com.example.chess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chess.chat.ChatAdapter;
import com.example.chess.chat.ChatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnlineGame extends AppCompatActivity {

    private String gameCode;
    private SharedPreferences preferences;
    private String myUsername, myPlayerType, opponentPlayerType;
    private EditText message;
    private RecyclerView chat;
    private List<ChatList> messages;
    private ChatAdapter chatAdapter;
    private DatabaseReference chatReference;
    private DatabaseReference myLastMsg_reference;
    private DatabaseReference opponentLastMsg_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_game);

        Intent gameCodeIntent = getIntent();
        gameCode = gameCodeIntent.getStringExtra("game_code");
        myPlayerType = gameCodeIntent.getStringExtra("player_type");
        if (myPlayerType.equals("joiner"))
            opponentPlayerType = "creator";
        else
            opponentPlayerType = "joiner";

        preferences = getSharedPreferences("User_Data", 0);
        myUsername = preferences.getString("username", null);

        chatReference = FirebaseDatabase.getInstance().getReference("Games").child(gameCode).child("Chat");
        myLastMsg_reference = chatReference.child(myPlayerType + " last message");
        opponentLastMsg_reference = chatReference.child(opponentPlayerType + " last message");

        myLastMsg_reference.setValue("");
        opponentLastMsg_reference.setValue("");

        messages = new ArrayList<ChatList>();

        message = findViewById(R.id.messageEditTxt);
        chat = findViewById(R.id.chatting);

        chat.setHasFixedSize(true);
        chat.setLayoutManager(new LinearLayoutManager(OnlineGame.this));
        chatAdapter = new ChatAdapter(messages, OnlineGame.this, myUsername);
        chat.setAdapter(chatAdapter);

        opponentLastMsg_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().equals("")) {
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


    }

    public void sendMessage(View view) {
        String msg = message.getText().toString();

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
}