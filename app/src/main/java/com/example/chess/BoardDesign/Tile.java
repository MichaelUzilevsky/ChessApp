package com.example.chess.BoardDesign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;

public class Tile extends View {
    private Paint paint = new Paint();
    private int top_X, top_Y, bottom_X, bottom_Y;
    private int[] RGBColor;

    public Tile(Context context, int top_X, int top_Y, int bottom_X, int bottom_Y, int[]color) {
        super(context);
        this.top_X = top_X;
        this.top_Y = top_Y;
        this.bottom_X = bottom_X;
        this.bottom_Y = bottom_Y;
        this.RGBColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.rgb(RGBColor[0],RGBColor[1],RGBColor[2]));
        canvas.drawRect(top_X, top_Y, bottom_X, bottom_Y, paint);
    }
}
