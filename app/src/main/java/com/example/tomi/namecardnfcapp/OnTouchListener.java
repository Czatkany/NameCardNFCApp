package com.example.tomi.namecardnfcapp;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Tomi on 2017. 04. 25..
 */

public class OnTouchListener implements View.OnTouchListener, GestureDetector.OnDoubleTapListener {

    private float mPrevX;
    private float mPrevY;

    public EditorActivity mainActivity;

    public OnTouchListener(EditorActivity _mainActivity) {
        mainActivity = _mainActivity;
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float currX, currY;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                    view.findFocus();

                    mPrevX = event.getX();
                    mPrevY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                currX = event.getRawX();
                currY = event.getRawY() - 145;


                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
                marginParams.setMargins((int) (currX - mPrevX), (int) (currY - mPrevY), 0, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                view.setLayoutParams(layoutParams);


                break;
            }


            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}