package com.example.admin.whiteout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Jialin Liu on 09/09/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class MainView extends View implements View.OnClickListener, View.OnTouchListener {
    // Main descriptions
    static String tag = "MainView: ";
    static String desp = "Time to sleep! Turn off the lights!";
    // Background and text
    static int bgColor = Color.GRAY;
    static int textColor = Color.YELLOW;
    static float textPosY = 100;
    static double textFrac = 0.05;
    // Grid
    static int gridColor = Color.YELLOW;
    static int offColor = Color.BLACK;
    static int onColor = Color.WHITE;
    static int dim = 10;


    //Text
    private float textSize;
    private float textPosX;
    // Grid
    private float gridPosX;
    private float gridPosY;
    private float gridSize;
    private float winSize;
    private WhiteOut model;

    // Touch event
    private float currentPosX;
    private float currentPosY;

    public MainView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        System.out.println(tag + "first constructor");
        setOnClickListener(this);
        setup(context, "first constructor without giving white out");
        if(model == null)
            System.err.println("ERROR: model not created!!!");
    }

    public MainView(Context context, WhiteOut model) {
        super(context);
        this.model = model;
        setup(context, "first constructor with white out");
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setup(Context context, String cons) {
        System.out.println(tag + cons);
        checkModel();
        setOnClickListener(this);
        setOnTouchListener(this);
    }

    private void checkModel() {
        if(model != null) {
            dim = model.getDim();
        } else {
            System.out.println(tag + "No model found. Now create one.");
            model = new WhiteOut(dim);
            if(model.getBits() == null) {
                System.err.println("ERROR: bits not initialised!!!");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Background
        Paint bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);

        // Text
        Paint textPaint = new Paint();
        textSize = (float) (textFrac * Math.min(getWidth(), getHeight()));
        textPaint.setTextSize(textSize);
        float textWidth = textPaint.measureText(desp);
        textPaint.setColor(textColor);
        // Align the text to the center
        textPaint.setTextSize(textSize);
        textPosX = (getWidth() - textWidth) / 2;
        System.out.println(getWidth() + " " + getHeight() + " " + textWidth);
        canvas.drawText(desp, textPosX, textPosY, textPaint);

        // Draw grid
        setGeometry();
        bgPaint.setColor(gridColor);
        canvas.drawRoundRect(new RectF(gridPosX, gridPosY, gridPosX+gridSize, gridPosY+gridSize), 5, 5, bgPaint);
        // Draw the squares
        for(int k=0; k<this.dim*this.dim; k++) {
            int thiscolor = model.getBits()[k]>0 ? offColor : onColor;
            bgPaint.setColor(thiscolor);
            int i = k%this.dim;
            int j = k/this.dim;
            drawTile(canvas, (int) (gridPosX + i*winSize + winSize/2), (int) (gridPosY + j*winSize + winSize/2), bgPaint);
        }
    }

    private void setGeometry() {
        float midGridX = getWidth() / 2;
        float midGridY = getHeight() / 2;
        gridSize = (float) (Math.min(getWidth(), getHeight()) * 0.9);
        winSize = (gridSize / dim);
        gridPosX = midGridX - gridSize / 2;
        gridPosY = midGridY - gridSize / 2;
    }

    // Draw individual tiles
    private void drawTile(Canvas canvas, int cx, int cy, Paint paint) {
        float length = (winSize*7)/8;
        float rad = winSize/6;
        RectF rect = new RectF(cx-length/2, cy-length/2, cx+length/2, cy+length/2);
        canvas.drawRoundRect(rect, rad, rad, paint);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.currentPosX = event.getX();
        this.currentPosY = event.getY();
        System.out.println(tag + " onTouch : current position (" + currentPosX + "," + currentPosY +") and window size=" + winSize);
        return false;
    }

    @Override
    public void onClick(View v) {
        if(currentPosX<=gridPosX+gridSize && currentPosX>=gridPosX && currentPosY<=gridPosY+gridSize && currentPosY>= gridPosY) {
            int i = (int) ((currentPosX - gridPosX) / winSize);
            System.out.println(tag + " current position (" + currentPosX + "," + currentPosY + ") and window size=" + winSize);
            int j = (int) ((currentPosY - gridPosY) / winSize);
            System.out.println(tag + "tile (" + i + "," + j + ") clicked");
            model.flip(i,j);
        }
        postInvalidate();
    }

    public WhiteOut getModel() {
        return this.model;
    }
}
