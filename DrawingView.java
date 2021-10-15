package com.example.touchscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DrawingView extends View {

    // setup initial color
    private final int paintColor = Color.BLACK;
    private int[] colorList=new int[10];
    // defines paint and canvas
    private Paint drawPaint,touchPaint;
    private MotionEvent event;
    // Store circles to draw each time the user touches down
    private List<Point> circlePoints;
    float touchX=0;
    float touchY=0;
    protected DebugList  debugList;
    boolean SHOW_TOUCH_DEBUG =true;
    boolean SHOW_GENERAL_DEBUG =false;

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();

        circlePoints = new ArrayList<Point>();

        debugList = new DebugList();
    }
    private int getColorForPointerID(int pointerID)
    {
        //Siempre obtiene un numero entre 0 y 9
        //independiente de si el PointerCount devuelve un numero de 10, de 50 o de 1000
        float decimal=((float)pointerID)/10;
        int entero=(pointerID)/10;
        decimal=decimal-(float)entero;
        float color=decimal*10;

        return colorList[(int)color];
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        touchPaint = new Paint();
        touchPaint.setColor(paintColor);
        touchPaint.setAntiAlias(true);
        touchPaint.setStrokeWidth(5);
        touchPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        touchPaint.setStrokeJoin(Paint.Join.ROUND);
        touchPaint.setStrokeCap(Paint.Cap.ROUND);

        colorList[0]=Color.BLACK;
        colorList[1]=Color.RED;
        colorList[2]=Color.GREEN;
        colorList[3]=Color.BLUE;
        colorList[4]=Color.YELLOW;
        colorList[5]=Color.MAGENTA;
        colorList[6]=Color.CYAN;
        colorList[7]=Color.rgb(128,255,128);
        colorList[8]=Color.rgb(255,128,128);
        colorList[9]=Color.rgb(128,128,255);
    }
    // ...variables and setting up paint...
    // Let's draw three circles
    @Override
    protected void onDraw(Canvas canvas) {

        for (Point p : circlePoints) {
            canvas.drawCircle(p.x, p.y, 5, drawPaint);
        }
        if(event!=null) {
            int colorIndex=0;
            for (int pointer = 0; pointer < event.getPointerCount(); pointer++) {
                int pointerID=event.getPointerId(pointer);
                touchPaint.setColor(getColorForPointerID(pointerID));
                canvas.drawCircle(
                        event.getX(pointer),
                        event.getY(pointer),
                        100,
                        touchPaint);
            }
            colorIndex++;
            if (colorIndex>colorList.length)colorIndex=0;
        }
        drawDebugInfo(canvas);
    }
    // Append new circle each time user presses on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.event=MotionEvent.obtain(event);
         touchX = event.getX();
         touchY = event.getY();
        circlePoints.add(new Point(Math.round(touchX), Math.round(touchY)));
        int ActionIndex=event.getActionIndex();
        int ActionMask=event.getActionMasked();
        int ActionPointerCount=event.getPointerCount();

        debugList.a("ActionPointerCount",(int)ActionPointerCount);
        debugList.a("touchX",(int)touchX);
        debugList.a("touchY",(int)touchY);
        debugList.a("ActionIndex",(int)ActionIndex);
        debugList.a("ActionMask",(int)ActionMask);
        // indicate view should be redrawn
        postInvalidate();
        return true;
    }

    // Presenta en pantalla la informacion de depuracion
    protected void drawDebugInfo(Canvas canvas){

        final int DEBUG_TEXT_SIZE=50;
        final int DEBUG_TEXT_SPACE=20;
        final int DEBUG_TOUCH_SPACE=150;
        Paint paint=new Paint();

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);

        paint.setTextSize(DEBUG_TEXT_SIZE);
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(730, 430, 40, 20, paint);
        int py = 100;
        int px=0;
        String text;
        if(SHOW_TOUCH_DEBUG&&(event!=null))
        {

            int ActionPointerCount=event.getPointerCount();
            canvas.drawText(
                    "Pointers",
                    0,
                    0 + py,
                    paint);
            canvas.drawText(
                    Integer.toString(ActionPointerCount),
                    paint.measureText("Pointers") + DEBUG_TEXT_SPACE,
                    0 + py,
                    paint);
            py += DEBUG_TEXT_SIZE;
            for(int pointer=0;pointer<ActionPointerCount;pointer++)
            {

                px=0;
                paint.setColor(getColorForPointerID(event.getPointerId(pointer)));
                canvas.drawText(
                        Integer.toString(pointer),
                        px,
                        0 + py,
                        paint);
                px+=DEBUG_TOUCH_SPACE;
                canvas.drawText(
                        Integer.toString(event.getPointerId(pointer)),
                        px,
                        0 + py,
                        paint);
                px+=DEBUG_TOUCH_SPACE;
                canvas.drawText(
                        Integer.toString((int)event.getX(pointer)),
                        px,
                        0 + py,
                        paint);
                px+=DEBUG_TOUCH_SPACE;
                canvas.drawText(
                        Integer.toString((int)event.getY(pointer)),
                        px,
                        0 + py,
                        paint);
                px+=DEBUG_TOUCH_SPACE;
                py += DEBUG_TEXT_SIZE;
            }
        }
        if(SHOW_GENERAL_DEBUG) {
            for (DebugLine debugLine :
                    debugList.debugLines) {
                canvas.drawText(
                        debugLine.Name,
                        0,
                        0 + py,
                        paint);
                canvas.drawText(
                        Integer.toString(debugLine.Value),
                        paint.measureText(debugLine.Name) + DEBUG_TEXT_SPACE,
                        0 + py,
                        paint);
                py += DEBUG_TEXT_SIZE;
            }
        }
    }
    }

