package edu.kvcc.cis298.cis298assignment3;

import android.support.v4.app.Fragment;

/**
 * Created by David Barnes on 11/3/2015.
 */
public class BeverageListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BeverageListFragment();
    }
}
