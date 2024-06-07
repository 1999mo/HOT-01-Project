package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Knights {
    /**
     * Need the previous x, y to get it back - done
     * Change the rectangle for a better model - done, but if there is a better model, that can be great
     * Need another button for the change of drag and drop <-> click and point
     * Need to make a field - the most low row is done
     * Make settings
     * Make Backgrounds for the first and second layout
     */

    /**
     * Last line
     * x
     * 495, 610, 750, 890, 1030, 1175, 1315, 1450, 1600, 1740
     * y
     * 860 - 965
     */
    private float x, y;
    private float prevX, prevY;
    private boolean actionDown = false;
    private int currentIndex = -1;
    private final float[] xPosition = {550.0F, 685.0F, 820.0F, 960.0F, 1100.0F, 1240.0F, 1385.0F, 1525.0F, 1670.0F};
    private Drawable stand;

    public Knights(float x, float y, Context context) {
        this.x = x;
        this.y = y;
        prevX = this.x;
        prevY = this.y;
        stand = context.getResources().getDrawable(R.drawable.garen);
    }

    public void draw(Canvas canvas) {
        /*
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(255, 0, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(x, y, x + 100, y + 100, myPaint);
         */

        stand.setBounds((int)x, (int)y, (int)x + 100, (int)y + 100);
        stand.draw(canvas);
    }

    public void setActionDown(boolean[] availableList) {
        if(actionDown) {
            if (!positionValid(availableList)) {
                setPosition(prevX, prevY);
            } else {
                setPosition(xPosition[currentIndex], 910);
                setPreviousPosition();
            }
            actionDown = false;
        }
    }

    public boolean getActionDown() {
        return actionDown;
    }

    public void setPosition(float x, float y) {
        this.x = x - 50;
        this.y = y - 50;
    }

    public boolean isTouched(float x, float y) {
        boolean xIsInside = x > this.x && x < this.x + 100;
        boolean yIsInside = y > this.y && y < this.y + 100;
        return xIsInside && yIsInside;
    }

    public void takeActionDown() {
        actionDown = true;
    }

    private void setPreviousPosition() {
        prevX = x + 50;
        prevY = y + 50;
    }

    private boolean positionValid(boolean[] availableList) {
        if (y <= 965 && y >= 860) {
            int xIndex = getXIndex();
            if (xIndex >= 0 && xIndex <= 8) {
                if (!availableList[xIndex]) {
                    availableList[xIndex] = true;
                    if(currentIndex != -1) {
                        availableList[currentIndex] = false;
                    }
                    currentIndex = xIndex;
                    return true;
                }
            }
        }
        return false;
    }

    private int getXIndex() {
        //495, 610, 750, 890, 1030, 1175, 1315, 1400, 1600, 1740
        if(x >= 495 && x <= 610) {
            return 0;
        } else if (x >= 610 && x <= 750) {
            return 1;
        } else if (x >= 750 && x <= 890) {
            return 2;
        } else if (x >= 890 && x <= 1030) {
            return 3;
        } else if (x >= 1030 && x <= 1175) {
            return 4;
        } else if (x >= 1175 && x <= 1315) {
            return 5;
        } else if (x >= 1315 && x <= 1450) {
            return 6;
        } else if (x >= 1450 && x <= 1600) {
            return 7;
        } else if (x >= 1600 && x <= 1740) {
            return 8;
        }

        return -1;
    }
}
