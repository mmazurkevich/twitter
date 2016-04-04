package com.example.infinity.twitter.util;

import android.view.MotionEvent;
import android.view.View;

import com.example.infinity.twitter.ui.fragments.SingleTweetFragment;

/**
 * Created by infinity on 24.09.15.
 */
public class SwipeDetectorUtils implements View.OnTouchListener {

    public interface SwipeDownDetector{
        void onSwipeDownDetector();
    }

    public interface SwipeUpDetector{
        void onSwipeUpDetector();
    }

    static final int MIN_DISTANCE = 20;
    private float downX, downY, upX, upY;
    private SwipeDownDetector mSwipeDownDetector;
    private SwipeUpDetector mSwipeUpDetector;
    private SingleTweetFragment mActivity;
    private int mEvent;

    public SwipeDetectorUtils(SingleTweetFragment a, int b) {
        mActivity = a;
        mEvent = b;
    }

    public void setSwipeDownDetector(SwipeDownDetector a){
        mSwipeDownDetector = a;
    }

    public void setSwipeUpDetector(SwipeUpDetector a){
        mSwipeUpDetector = a;
    }

    public void onDownSwipe(){
        mSwipeDownDetector.onSwipeDownDetector();
    }

    public void onUpSwipe(){
        mSwipeUpDetector.onSwipeUpDetector();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if(Math.abs(deltaX) > MIN_DISTANCE){
                        // left or right
                        if(deltaX > 0) { return true; }
                        if(deltaX < 0) { return true; }
                    }
                    else {
                        if (mEvent == 2) //touch
                            mActivity.tweetTouch();
                        return false; // We don't consume the event
                    }
                }
                // swipe vertical?
                else
                {
                    if(Math.abs(deltaY) > MIN_DISTANCE){
                        // top or down
                        if(deltaY < 0) { this.onDownSwipe(); return true; }
                        if(deltaY > 0) { this.onUpSwipe(); return true; }
                    }
                    else {
                        if (mEvent == 2) //touch
                            mActivity.tweetTouch();
                        return false; // We don't consume the event
                    }
                }

                return true;
            }
        }
        return false;
    }

}