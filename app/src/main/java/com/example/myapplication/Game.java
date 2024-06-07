package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Game manages all objects in the game and is responsible for updating all states
 * and render all objects to the screen
 */

class Game extends SurfaceView implements SurfaceHolder.Callback{
    /**
     * Make synergy table and display it
     * -> Have a list of knights on the field
     * Need to make a shop button
     */
    private GameLoop gameLoop;
    private Context context;
    private Knights[] knights;
    private Drawable background;
    private float X = 0;
    private float Y = 0;

    private boolean toggleSwitch = true;
    private int selectKnight = -1;

    public Game(Context context) {
        super(context);

        //Get SurfaceHolder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);

        knights = new Knights[3];
        knights[0] = new Knights(500, 500, context);
        knights[1] = new Knights(100, 500, context);
        knights[2] = new Knights(500, 100, context);
        background = context.getResources().getDrawable(R.drawable.background);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, holder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Rect imageBounds = canvas.getClipBounds();

        background.setBounds(imageBounds);
        background.draw(canvas);

        //drawUPS(canvas);
        //drawFPS(canvas);
        //drawCoordinate(canvas);

        //draw each object
        for (Knights knight : knights) {
            knight.draw(canvas);
        }
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void drawCoordinate(Canvas canvas) {
        String coordinate = X + "|" + Y;
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText(coordinate, 100, 100, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                X = event.getX();
                Y = event.getY();
                if (!toggleSwitch) {
                    for (Knights knight : knights) {
                        if (knight.isTouched(X, Y)) {
                            knight.takeActionDown();
                        }
                    }
                }
                else {
                    if (selectKnight == -1) {
                        for (int i = 0; i < knights.length; i++) {
                            if (knights[i].isTouched(X, Y)) {
                                knights[i].selectKnight();
                                selectKnight = i;
                            }
                        }
                    }
                    else {
                        knights[selectKnight].unselectKnight(X, Y);
                        selectKnight = -1;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!toggleSwitch) {
                    for (Knights knight : knights) {
                        if (knight.getActionDown()) {
                            knight.setPosition(event.getX(), event.getY());
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!toggleSwitch) {
                    for (Knights knight : knights) {
                        knight.setActionDown();
                    }
                }
                break;
        }
        return true;
    }

    public void update() {
        //Update game
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
