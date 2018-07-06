package functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tony on 2017-05-02.
 */

public class ColorPickerView extends View {

    private Paint mPaint;
    private int mCurrentColor;
    private int[] mHueBarColors;
    private int width;
    private int height;
    private final String log = "icozy";
    private OnColorPickedListener mListener;

    public ColorPickerView(Context context) {
        super(context);
        initView();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public interface OnColorPickedListener{
        void onColorPicked(float x, float y, int color);
    }

    public void setOnColorPickedListener(OnColorPickedListener l){
        mListener=l;
    }

    private void initView() {
        //뷰 초기화
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHueBarColors = new int[258];
        mCurrentColor = 0;

        // 슬라이드바 색 초기화 red-pink-blue-light blue-green-yellow-red(mHueBarColors)
        int index = 0;

        // Red (#f00) to pink(#f0f)
        for (float i = 0; i < 256; i += 256 / 42) {
            mHueBarColors[index] = Color.rgb(255, 0, (int) i);
            index++;
        }

        // Pink (#f0f) to blue(#00f)
        for (float i = 0; i < 256; i += 256 / 42) {
            mHueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
            index++;
        }
        // Blue (#00f) to light blue (#0ff)
        for (float i = 0; i < 256; i += 256 / 42) {
            mHueBarColors[index] = Color.rgb(0, (int) i, 255);
            index++;
        }

        // Light blue (#0ff) to green (#0f0)
        for (float i = 0; i < 256; i += 256 / 42) {
            mHueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
            index++;
        }

        // Green (#0f0) to yellow (#ff0)
        for (float i = 0; i < 256; i += 256 / 42) {
            mHueBarColors[index] = Color.rgb((int) i, 255, 0);
            index++;
        }

        // Yellow (#ff0) back to red (#f00)
        for (float i = 0; i < 256; i += 256 / 42) {
            mHueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
            index++;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // mHueBarColors 모두 띄워주기
        for (int x = 0; x < 257; x++) {

            mPaint.setColor(mHueBarColors[x]);
            mPaint.setStrokeWidth(width/240.5F);

            canvas.drawLine(x * (width/240.5F), 0, x * (width/240.5F), height, mPaint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;
        float x = event.getX();
        float y = event.getY();
        Log.d(log, "x - " + x + ", y - " + y);

        // 컬러 스팩트럼 터치했을 경우
        if (x > 0 && x < width && y > 0 && y < height) {
            mCurrentColor = mHueBarColors[(int)(x)*241/width];
            Log.d(log, "mCurrentColor - " + mCurrentColor);
            //x, y 좌표, alpha 바뀐 color 보내기
            mListener.onColorPicked(x, y, getAlphaSetColor());
        }
        return true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        //뷰 높이와 너비 dp 측정
        width = View.MeasureSpec.getSize(widthMeasureSpec)-68;
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        Log.d(log, "width - "+width);

    }

    public int getAlphaSetColor() {
        // 화면색은 약한 alpha로 설정
        int red = Color.red(mCurrentColor);
        int green = Color.green(mCurrentColor);
        int blue = Color.blue(mCurrentColor);
        int color_temp = Color.argb(130, red, green, blue);
        return color_temp;
    }


}
