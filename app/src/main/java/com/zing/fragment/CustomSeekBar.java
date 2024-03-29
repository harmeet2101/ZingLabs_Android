package com.zing.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;
/**
 * Created by abhishek on 22/8/18.
 */
public class CustomSeekBar extends SeekBar {


    private Rect rect;
    private Paint paint ;
    private int seekbar_height;

    public CustomSeekBar(Context context) {
        super(context);

    }

    public CustomSeekBar(Context context, AttributeSet attrs) {

        super(context, attrs);
        rect = new Rect();
        paint = new Paint();
        seekbar_height = 6;
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        rect.set(0 + getThumbOffset(),
                (getHeight() / 2) - (seekbar_height/2),
                getWidth()- getThumbOffset(),
                (getHeight() / 2) + (seekbar_height/2));

        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);



        if (this.getProgress() > 50) {


            rect.set(getWidth() / 2,
                    (getHeight() / 2) - (seekbar_height/2),
                    getWidth() / 2 + (getWidth() / 100) * (getProgress() - 50),
                    getHeight() / 2 + (seekbar_height/2));

            paint.setColor(Color.WHITE);

            canvas.drawRect(rect, paint);

        }

        if (this.getProgress() < 50) {

            rect.set(getWidth() / 2 - ((getWidth() / 100) * (50 - getProgress())),
                    (getHeight() / 2) - (seekbar_height/2),
                    getWidth() / 2,
                    getHeight() / 2 + (seekbar_height/2));

            paint.setColor(Color.WHITE);

            canvas.drawRect(rect, paint);

        }

        super.onDraw(canvas);
    }
}