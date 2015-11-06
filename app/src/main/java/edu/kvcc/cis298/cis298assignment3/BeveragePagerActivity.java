package edu.kvcc.cis298.cis298assignment3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by David Barnes on 11/5/2015.
 */
public class BeveragePagerActivity extends FragmentActivity {

    private static final String EXTRA_BEVERAGE_ID = "edu.kvcc.cis298.cis298assignment3.beverage_id";

    private ViewPager mViewPager;
    private List<Beverage> mBeverages;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, BeveragePagerActivity.class);
        intent.putExtra(EXTRA_BEVERAGE_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beverage_pager);

        String id = getIntent().getStringExtra(EXTRA_BEVERAGE_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_beverage_pager_view_pager);

        mBeverages = BeverageCollection.get(this).getBeverages();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Beverage beverage = mBeverages.get(position);
                return BeverageFragment.newInstance(beverage.getId());
            }

            @Override
            public int getCount() {
                return mBeverages.size();
            }
        });

        for (int i = 0; i < mBeverages.size(); i++) {
            if (mBeverages.get(i).getId().equals(id)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
