package com.example.chess.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chess.R;

import org.w3c.dom.Text;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatList> messages;
    private final Context context;
    private String sender;

    public ChatAdapter(List<ChatList> messages, Context context, String sender) {
        this.messages = messages;
        this.context = context;
        this.sender = sender;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        ChatList list2 = messages.get(position);
        if(list2.getSender().equals(sender)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myMsg.setText(list2.getMessage());
        }
        else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppoMsg.setText(list2.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateChatList(List<ChatList> messages){
        this.messages = messages;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMsg, myMsg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMsg = itemView.findViewById(R.id.oppoMsg);
            myMsg = itemView.findViewById(R.id.myMsg);
        }
    }
}
