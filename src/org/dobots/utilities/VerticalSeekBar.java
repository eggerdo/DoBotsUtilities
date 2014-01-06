// https://github.com/AndroSelva/Vertical-SeekBar-Android
package org.dobots.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

	public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(),0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	if (mOnSeekBarChangeListener != null) {
            		mOnSeekBarChangeListener.onStartTrackingTouch(this);
            	}
            	trackTouchEvent(event);
            	break;
            case MotionEvent.ACTION_MOVE:
            	trackTouchEvent(event);
            	break;
            case MotionEvent.ACTION_UP:
            	trackTouchEvent(event);
            	if (mOnSeekBarChangeListener != null) {
            		mOnSeekBarChangeListener.onStopTrackingTouch(this);
            	}
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    
    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
    	super.setOnSeekBarChangeListener(l);
    	mOnSeekBarChangeListener = l;
    }
    
    public void setNewProgress(int progress) {
    	setProgress(progress);
    	onSizeChanged(getWidth(), getHeight(), 0, 0);
    }
    
    private void trackTouchEvent(MotionEvent event) {
    	int i=0;
    	i=getMax() - (int) (getMax() * event.getY() / getHeight());
        setProgress(i);
        Log.i("Progress",getProgress()+"");
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }
    
}