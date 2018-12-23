package cn.sjj.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 去掉Padding的TextView，要配合android:includeFontPadding="false"使用
 *
 * @author 宋疆疆
 * @date 2016/5/10.
 */
public class NoPaddingTextView extends TextView {

    public NoPaddingTextView(Context context) {
        super(context);
    }

    public NoPaddingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoPaddingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint.FontMetrics fontMetricsInt;

    @Override
    protected void onDraw(Canvas canvas) {
        if (fontMetricsInt == null) {
            fontMetricsInt = new Paint.FontMetrics();
            getPaint().getFontMetrics(fontMetricsInt);
        }
        canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        super.onDraw(canvas);
    }
}
