package com.example.sasha.okhear.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

public class DetectedRectangles extends View {

    public DetectedRectangles(Context context) {
        super(context);
    }

    public DetectedRectangles(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetectedRectangles(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint = new Paint();

    List<RectF> rects = new ArrayList<>();
    RectF croppedRect;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (croppedRect != null) {
            for (RectF rect : rects) {
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(rect, paint);
            }

            paint.setColor(Color.RED);
            canvas.drawRect(croppedRect, paint);
        }
    }

    public void setRectangles(Rect[] rects, Rect croppedRect) {
        if (this.rects != null) {
            this.rects.clear();
            for (Rect rect : rects) {
                this.rects.add(new RectF(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height));
            }
            this.croppedRect = new RectF(croppedRect.x, croppedRect.y,
                    croppedRect.x + croppedRect.width, croppedRect.y + croppedRect.height);
            invalidate();
        }
    }
}
