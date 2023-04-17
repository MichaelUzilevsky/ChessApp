package com.example.chess.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

public class dialog {
    private Activity activity;
    private AlertDialog dialog;
    private int src;

    public dialog(Activity myActivity, int src) {
        activity = myActivity;
        this.src = src;
    }

    @SuppressLint("InflateParams")
    public void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(src, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    // dismiss method
    public void dismissDialog() {
        dialog.dismiss();
    }
}
