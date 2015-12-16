package edu.kvcc.cis298.cis298assignment4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by David Barnes on 11/3/2015.
 */
public class BeverageListFragment extends Fragment {

    //Private variables for the recycler view and the required adapter
    private RecyclerView mBeverageRecyclerView;
    private BeverageAdapter mBeverageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start fetching the beverages in a new thread
        new FetchBeveragesTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Use the inflator to create a new view
        View view = inflater.inflate(R.layout.fragment_beverage_list, container, false);

        //Get a handle for the recycler view
        mBeverageRecyclerView = (RecyclerView) view.findViewById(R.id.beverage_recycler_view);

        //Set the layout manager, which is a required piece of the view
        mBeverageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Call the updateUI method to do any remaining setup and get the view displayed
        updateUI();

        //return the view
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //If this fragment is resumed from returning from the detail view, update the UI
        updateUI();
    }

    //Method to setup the view with an adapter if it doesn't already have one.
    //and update changes if it does.
    private void updateUI() {

        //Get the collection of data.
        BeverageCollection beverageCollection = BeverageCollection.get(getActivity());

        // Get the beverages from the collection's database.
        List<Beverage> beverages = beverageCollection.getBeverages();

        // If there is no adapter, make a new one and send it in the list of beverages
        if (mBeverageAdapter == null) {

            // Set up the adapter
            setupAdapter();
        } else {
            // Update the beverages adapter.
            mBeverageAdapter.setBeverages(beverages);
            mBeverageAdapter.notifyDataSetChanged();
        }
    }

    //Private class that is required to get a recyclerview working
    private class BeverageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //private widget vars
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mIdTextView;

        //private var for a single beverage
        private Beverage mBeverage;

        //constructor that takes in a view
        public BeverageHolder(View itemView) {
            super(itemView);
            //Set an onclick listener for the view holder
            itemView.setOnClickListener(this);

            //Wire up the vars to the layout widgets
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            mIdTextView = (TextView) itemView.findViewById(R.id.list_item_id);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_price);
        }

        //Implement the bindBeverage method that takes in a single beverage
        public void bindBeverage(Beverage beverage) {
            //set the beverage to the class level one
            mBeverage = beverage;

            //set the name and id from the model
            mNameTextView.setText(mBeverage.getName());
            mIdTextView.setText(mBeverage.getId());

            //Create and use a number formatter that will display the price as currency
            NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
            mPriceTextView.setText(moneyFormat.format(mBeverage.getPrice()));
        }

        //Override the onClick method so clicking on a viewHolder starts up a new activity
        @Override
        public void onClick(View v) {
            //get a properly formatted intent from the BeveragePagerActivity
            Intent intent = BeveragePagerActivity.newIntent(getActivity(), mBeverage.getId());
            //Start the new activity
            startActivity(intent);
        }
    }

    //Private class for the adapter that the recyclerview needs
    private class BeverageAdapter extends RecyclerView.Adapter<BeverageHolder> {
        //private list of the beverages
        private List<Beverage> mBeverages;

        //Constructor that takes in a list of beverages and then assigns it to the class level version
        public BeverageAdapter(List<Beverage> beverages) {
            mBeverages = beverages;
        }

        //Method to get the create the view holder
        @Override
        public BeverageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //get an inflator
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //inflate the view
            View view = layoutInflater.inflate(R.layout.list_item_beverage, parent, false);
            //return a new holder with the view
            return new BeverageHolder(view);
        }

        //Method to bind a view holder to a beverage
        @Override
        public void onBindViewHolder(BeverageHolder beverageHolder, int position) {
            //get the beverage from the list based on the position being searched for
            Beverage beverage = mBeverages.get(position);
            //call the bind beverage method with the found beverage
            beverageHolder.bindBeverage(beverage);
        }

        //Method to get the size of the list of beverages.
        @Override
        public int getItemCount() {
            return mBeverages.size();
        }

        public void setBeverages(List<Beverage> beverages) {
            mBeverages = beverages;
        }
    }

    private class FetchBeveragesTask extends AsyncTask<Void, Void, List<Beverage>> {

        // Gets a list of beverages out of the JSON file on the internet.
        @Override
        protected List<Beverage> doInBackground(Void... params) {
            return new BeverageFetcher().fetchBeverages();
        }

        // This is executed and given the argument from doInBackground.
        @Override
        protected void onPostExecute(List<Beverage> beverages) {

            // Get reference to singleton collection of Beverages.
            BeverageCollection collection = BeverageCollection.get(getActivity());

            // Populate data singleton's database with this new list of beverages.
            collection.setBeverages(beverages);

            // Now that the collection is proper, we can set up the adapter.
            setupAdapter();
        }
    }

    private void setupAdapter() {

        // If the Fragment has already been added to the Activity...
        if(isAdded()) {
            BeverageCollection collection = BeverageCollection.get(getActivity());      // Get the collection of beverages.
            mBeverageAdapter = new BeverageAdapter(collection.getBeverages());          // Create an adapter with it.
            mBeverageRecyclerView.setAdapter(mBeverageAdapter);                         // Give the RecyclerView that adapter.
        }
    }
}




















