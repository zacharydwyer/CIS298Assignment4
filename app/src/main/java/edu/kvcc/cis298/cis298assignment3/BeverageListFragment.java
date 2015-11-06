package edu.kvcc.cis298.cis298assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by David Barnes on 11/3/2015.
 */
public class BeverageListFragment extends Fragment {

    private RecyclerView mBeverageRecyclerView;
    private BeverageAdapter mBeverageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beverage_list, container, false);

        mBeverageRecyclerView = (RecyclerView) view.findViewById(R.id.beverage_recycler_view);

        mBeverageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        BeverageCollection beverageCollection = BeverageCollection.get(getActivity());
        List<Beverage> beverages = beverageCollection.getBeverages();

        if (mBeverageAdapter == null) {
            mBeverageAdapter = new BeverageAdapter(beverages);
            mBeverageRecyclerView.setAdapter(mBeverageAdapter);
        } else {
            mBeverageAdapter.notifyDataSetChanged();
        }
    }

    private class BeverageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mIdTextView;

        private Beverage mBeverage;

        public BeverageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            mIdTextView = (TextView) itemView.findViewById(R.id.list_item_id);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_price);
        }

        public void bindBeverage(Beverage beverage) {
            mBeverage = beverage;

            mNameTextView.setText(mBeverage.getName());
            mIdTextView.setText(mBeverage.getId());

            NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
            mPriceTextView.setText(moneyFormat.format(mBeverage.getPrice()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = BeverageActivity.newIntent(getActivity(), mBeverage.getId());
            startActivity(intent);
        }
    }

    private class BeverageAdapter extends RecyclerView.Adapter<BeverageHolder> {
        private List<Beverage> mBeverages;

        public BeverageAdapter(List<Beverage> beverages) {
            mBeverages = beverages;
        }

        @Override
        public BeverageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_beverage, parent, false);
            return new BeverageHolder(view);
        }

        @Override
        public void onBindViewHolder(BeverageHolder beverageHolder, int position) {
            Beverage beverage = mBeverages.get(position);
            beverageHolder.bindBeverage(beverage);
        }

        @Override
        public int getItemCount() {
            return mBeverages.size();
        }
    }
}
