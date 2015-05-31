package me.lingxiao.exam.ui.listener;

import android.view.MotionEvent;
import android.view.View;

import me.lingxiao.exam.R;

public class MyPopupOnTouchListner implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setBackgroundColor(v.getResources().getColor(R.color.secondary_text));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.setBackgroundColor(v.getResources().getColor(R.color.background_material_light));
        }
        return false;
    }
}
