package org.ucomplex.ucomplex.Model;

import android.support.v4.app.Fragment;

/**
 * Created by Sermilion on 02/01/2016.
 */
public class PagerItem {
    private String mTitle;
    private Fragment mFragment;


    public PagerItem(String mTitle, Fragment mFragment) {
        this.mTitle = mTitle;
        this.mFragment = mFragment;
    }
    public String getTitle() {
        return mTitle;
    }
    public Fragment getFragment() {
        return mFragment;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

}