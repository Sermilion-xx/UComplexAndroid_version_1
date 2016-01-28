package org.ucomplex.ucomplex.Activities;

import android.support.v4.app.Fragment;

import org.ucomplex.ucomplex.Fragments.RoleSelectFragment;

public class RoleSelectActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RoleSelectFragment();
    }

}
