package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Drawable mainMenu;
    private Drawable shop;
    private Drawable subMenuImage;
    //private Drawable settings;
    private float X = 0;
    private float Y = 0;

    private boolean toggleSwitch = true;
    private int selectKnight = -1;
    private boolean stageActivated = false;
    private boolean shopOpen = false;
    private boolean settingsMenu = false;
    private boolean subMenu = false;

    float cx;
    float cy;

    public Game(Context context) {
        super(context);

        //Get SurfaceHolder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);

        knights = new Knights[3];
        knights[0] = new Knights(1000, 800, "garen", new String[]{"Noble", "Knight"}, context);
        knights[1] = new Knights(1200, 800, "lux", new String[]{"Star Guardian", "Spellslinger"}, context);
        knights[2] = new Knights(1400, 800, "katarina", new String[]{"Sinister", "Assasin"},context);
        background = context.getResources().getDrawable(R.drawable.background);
        mainMenu = context.getResources().getDrawable(R.drawable.mainmenu);
        shop = context.getResources().getDrawable(R.drawable.shop);
        subMenuImage = context.getResources().getDrawable(R.drawable.submenu);
        //settings = context.getResources().getDrawable(R.drawable);

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

        cx = canvas.getWidth();
        cy = canvas.getHeight();

        if (stageActivated) {
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

            //draw shop button
            if (shopOpen) {
                drawShop(canvas);
            } else {
                if (!settingsMenu) {
                    drawShopButton(canvas);
                }
            }

            if (settingsMenu) {
                drawSettings(canvas);
                drawSwitchButton(canvas);
            } else {
                if(!shopOpen) {
                    drawSettingsButton(canvas);
                }
            }

        } else {
            if (!subMenu) {
                Rect imageBounds = canvas.getClipBounds();

                mainMenu.setBounds(imageBounds);
                mainMenu.draw(canvas);

                drawMainmenuButton(canvas);
            } else {
                Rect imageBounds = canvas.getClipBounds();

                subMenuImage.setBounds(imageBounds);
                subMenuImage.draw(canvas);

                drawMainmenuButton(canvas);
            }
        }
    }

    private void drawSettingsButton(Canvas canvas) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setStrokeWidth(10);

        canvas.drawCircle(2000 * cx / 2220, 150 * cy / 1014, 100, paint);
    }

    private void drawSettings(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(105, 105, 105));
        paint.setStrokeWidth(10);

        canvas.drawRoundRect(100 * cx / 2220, 100 * cy / 1014, (100 + 2000) * cx / 2220, (100 + 800) * cy / 1014, 10, 10, paint);
    }

    private void drawShop(Canvas canvas) {
        shop.setBounds((int) (100 * cx / 2220), (int) (50 * cy / 1014), (int) ((100 + 2000) * cx / 2220), (int) ((50 + 250) * cy / 1014));
        shop.draw(canvas);
    }

    private void drawShopButton(Canvas canvas) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setStrokeWidth(10);

        canvas.drawCircle(2000 * cx / 2220, 850 * cy / 1014, 100, paint);
    }

    private void drawMainmenuButton(Canvas canvas) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setStrokeWidth(10);

        canvas.drawCircle(1750 * cx / 2220, 800 * cy / 1014, 100, paint);
    }

    private void drawSwitchButton(Canvas canvas) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setStrokeWidth(10);

        canvas.drawCircle(300 * cx / 2220, 300 * cy / 1014, 100, paint);
        paint.setTextSize(50);
        if(toggleSwitch) {
            canvas.drawText("Click and point to move", 300 * cx / 2220, 500 * cy / 1014, paint);
        }
        else {
            canvas.drawText("Drag and drop to move", 300 * cx / 2220, 500 * cy / 1014, paint);
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
        X = event.getX();
        Y = event.getY();
        if(stageActivated) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Settings Menu
                    if (settingsMenu) {
                        //Toggle Switch
                        if (switchClicked()) {
                            toggleSwitch = !toggleSwitch;
                            break;
                        }
                        //Close Menu
                        //100, 100, 100 + 2000, 100 + 800
                        if ((X < (100 * cx / 2220) || X > (2100 * cx / 2220)) || (Y < (100 * cy / 1014) || Y > (900 * cy / 1014))) {
                            settingsMenu = false;
                            break;
                        }
                    }
                    else {
                        if (getDistance(2000 * cx / 2220, 150 * cy / 1014) <= 100 && !shopOpen) {
                            settingsMenu = true;
                            break;
                        }
                    }

                    //Shop button
                    if (shopOpen) {
                        shopOpen = false;
                        break;
                    } else {
                        if (getDistance(2000 * cx / 2220, 850 * cy / 1014) <= 100 && !settingsMenu) {
                            shopOpen = true;
                            break;
                        }
                    }

                    if (!toggleSwitch) {
                        for (Knights knight : knights) {
                            if (knight.isTouched(X, Y)) {
                                knight.takeActionDown();
                                knight.selectKnight();
                            }
                        }
                    } else {
                        if (selectKnight == -1) {
                            for (int i = 0; i < knights.length; i++) {
                                if (knights[i].isTouched(X, Y)) {
                                    knights[i].selectKnight();
                                    selectKnight = i;
                                }
                            }
                        } else {
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
                            knight.unselectKnight(X, Y);
                        }
                    }
                    break;
            }
        }
        else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (getDistance(1750 * cx / 2220, 800 * cy / 1014) <= 100) {
                        if (!subMenu) {
                            subMenu = true;
                        } else {
                            subMenu = false;
                            stageActivated = true;
                        }
                    }
                    break;
            }
        }
        return true;
    }

    private boolean switchClicked() {
        double distance = getDistance(300 * cx / 2220, 300 * cy / 1014);
        if(distance <= 100) {
            return true;
        }
        else {
            return false;
        }
    }

    public void update() {
        //Update game
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    private double getDistance(float a, float b)
    {
        double distance = Math.sqrt(Math.pow((X - a), 2) + Math.pow((Y - b), 2));
        return distance;
    }
}
