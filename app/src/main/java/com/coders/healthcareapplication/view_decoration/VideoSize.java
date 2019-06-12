package com.coders.healthcareapplication.view_decoration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

public class VideoSize extends VideoView {
    public VideoSize(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Display dis =((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        setMeasuredDimension(dis.getWidth(), dis.getHeight() );

    }
}
