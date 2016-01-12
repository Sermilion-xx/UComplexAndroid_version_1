package org.ucomplex.ucomplex;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.quinny898.library.persistentsearch.SearchBox;

/**
 * Created by Sermilion on 12/01/2016.
 */

public class MySearchBox extends SearchBox {

    private Activity activity;

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public MySearchBox(Context context) {
        super(context);
    }

    public MySearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.KEYCODE_BACK && this.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return super.dispatchKeyEvent(e);
        }
    }
}