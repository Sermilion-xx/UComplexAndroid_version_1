package org.ucomplex.ucomplex.Adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.PagerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sermilion on 12/12/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private  List<Fragment> mFragmentList = new ArrayList<>();
    private  List<String> mFragmentTitleList = new ArrayList<>();

    private FragmentManager mFragmentManager;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        mFragmentManager = manager;
    }

    public void setPagerItems(List<Fragment> pagerItems) {
        if (mFragmentList != null)
            for (int i = 0; i < mFragmentList.size(); i++) {
                mFragmentManager.beginTransaction().add(mFragmentList.get(i),"fromFolder").addToBackStack("").commit();
            }
        mFragmentList = pagerItems;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragmentStack(CourseMaterialsFragment fragment, String title) {
        mFragmentManager.beginTransaction().add(fragment,"fromFolder").addToBackStack("Материалы").commit();
    }
}