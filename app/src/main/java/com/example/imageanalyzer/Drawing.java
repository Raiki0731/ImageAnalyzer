package com.example.imageanalyzer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


@SuppressLint("ViewConstructor")
public class Drawing extends View {
    Paint paint;
    private Bitmap bitmap;
    private final ModelForGson coordinates;
    private float bmpX;
    private float bmpY;
    private float scale;
    private int num = -1;
    private boolean flag_touch = false;
    private boolean flag_once = false;

    public Drawing(Context context, Bitmap bitmap, ModelForGson coordinates) {
        super(context);
        DrawingActivity drawingActivity = new DrawingActivity();
        paint = new Paint();
        this.bitmap = bitmap;
        this.coordinates = coordinates;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRGB(0,0,0);

        if (!flag_once) {
            Matrix matrix = new Matrix();
            if (bitmap.getWidth() > bitmap.getHeight()) {
                matrix.postRotate(90);
            }
            scale = getScale(getWidth(),getHeight(),bitmap.getWidth(), bitmap.getHeight()) + 0.12f;
            matrix.postScale(scale,scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            bmpX = (getWidth()  - bitmap.getWidth()) /2.0f;
            bmpY = (getHeight() - bitmap.getHeight()) /2.0f;
            flag_once = true;
        }

        canvas.drawBitmap(bitmap, bmpX, bmpY ,paint);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        float textX;
        float textY;

        for (int i = 0; i < coordinates.getObjects().size(); i++) {
            canvas.drawRect(
                    bmpX + (float)coordinates.getObjects().get(i).getRectangle().getX() * scale,
                    bmpY + (float)coordinates.getObjects().get(i).getRectangle().getY() * scale,
                    bmpX + ((float)coordinates.getObjects().get(i).getRectangle().getX() + (float)coordinates.getObjects().get(i).getRectangle().getW()) * scale,
                    bmpY + ((float)coordinates.getObjects().get(i).getRectangle().getY() + (float)coordinates.getObjects().get(i).getRectangle().getH()) * scale,
                    paint
            );
        }

        if (flag_touch) {
            paint.setColor(Color.GREEN);
            paint.setTextSize(65);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            String obj = coordinates.getObjects().get(num).getObject();

            if (bmpX + (float)coordinates.getObjects().get(num).getRectangle().getX() * scale <= 0) {
                textX = 1f;
            } else {
                textX = bmpX + (float)coordinates.getObjects().get(num).getRectangle().getX() * scale;
            }

            if (bmpY + (float)coordinates.getObjects().get(num).getRectangle().getY() * scale - 20f <= 0) {
                textY = bmpY + (float)coordinates.getObjects().get(num).getRectangle().getY() * scale + 100f;
            } else {
                textY = bmpY + (float)coordinates.getObjects().get(num).getRectangle().getY() * scale - 20f;
            }
            canvas.drawText(obj, textX, textY, paint);
            flag_touch = false;
        }
    }

    private float getScale(int dest_width, int dest_height, int src_width, int src_height) {
        float ret;

        if (dest_width < dest_height) {
            if (src_width <src_height) {
                ret = (float)dest_height / (float)src_height;
                if ((src_width * ret) > dest_width) {
                    ret = (float)dest_width / (float)src_width;
                }
            }else {
                ret = (float)dest_width / (float)src_width;
            }
        }else {
            if (src_width < src_height) {
                ret = (float)dest_height / (float)src_height;
            }else {
                ret = (float)dest_width / (float)src_width;

                if ((src_height * ret) > dest_height) {
                    ret = (float)dest_height / (float)src_height;
                }
            }
        }
        return ret;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();

            Log.v("TouchEvent", "release");

            for (int i = 0; i < coordinates.getObjects().size(); i++) {
                if ((bmpX + (float)coordinates.getObjects().get(i).getRectangle().getX() * scale) <= x && (bmpX + ((float)coordinates.getObjects().get(i).getRectangle().getX() + (float)coordinates.getObjects().get(i).getRectangle().getW()) * scale) >= x) {
                    if ((bmpY + (float)coordinates.getObjects().get(i).getRectangle().getY() * scale) <= y && (bmpY + ((float)coordinates.getObjects().get(i).getRectangle().getY() + (float)coordinates.getObjects().get(i).getRectangle().getH()) * scale) >= y) {
                        num = i;
                        flag_touch = true;
                        invalidate();
                        break;
                    }
                }
            }
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Log.v("TouchEvent", "press");
            invalidate();

        }

        return true;
    }
}
