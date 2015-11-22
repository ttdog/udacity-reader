package com.example.reader;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;

/**
 * Created by yoshidayuuki on 2015/11/22.
 */
public class CustomFloatingActionButton extends FloatingActionButton {
    public CustomFloatingActionButton(Context context){
        super(context);

        setBackgroundTintList(
                new ColorStateList(
                        new int[][] {
                                new int[]{ android.R.attr.state_selected },
                                new int[]{ -android.R.attr.state_selected },
                        },
                        new int[] {
                                Color.argb(0xff, 0xff, 0xff, 0xff),
                                Color.argb(0xff,0x8f,0x8f,0x8f),
                        }
                ));
    }
}
