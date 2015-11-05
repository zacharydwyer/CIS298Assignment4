package edu.kvcc.cis298.cis298assignment3;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BeverageActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BeverageFragment();
    }
}
