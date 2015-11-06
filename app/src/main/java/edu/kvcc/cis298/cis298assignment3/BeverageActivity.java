package edu.kvcc.cis298.cis298assignment3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BeverageActivity extends SingleFragmentActivity {

    private static final String EXTRA_BEVERAGE_ID = "edu.kvcc.cis298.cis298assignment3.beverage_id";

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, BeverageActivity.class);
        intent.putExtra(EXTRA_BEVERAGE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String beverageId = getIntent().getStringExtra(EXTRA_BEVERAGE_ID);
        return BeverageFragment.newInstance(beverageId);
    }
}
