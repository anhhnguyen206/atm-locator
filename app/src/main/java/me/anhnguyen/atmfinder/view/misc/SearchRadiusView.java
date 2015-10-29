package me.anhnguyen.atmfinder.view.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import me.anhnguyen.atmfinder.R;

/**
 * Created by nguyenhoanganh on 10/29/15.
 */
public class SearchRadiusView extends View {
    Paint paint;
    float radius;

    public SearchRadiusView(Context context) {
        super(context);
        setup();
    }

    public SearchRadiusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void setRadius(float radius) {
        if (this.radius != radius) {
            this.radius = radius;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius > 0) {
            int x = getWidth();
            int y = getHeight();
            canvas.drawCircle(x / 2, y / 2, radius, paint);
        }
    }

    private void setup() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }
}
