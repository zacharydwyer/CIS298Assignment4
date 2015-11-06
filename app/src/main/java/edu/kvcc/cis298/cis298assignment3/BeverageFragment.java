package edu.kvcc.cis298.cis298assignment3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by David Barnes on 11/3/2015.
 */
public class BeverageFragment extends Fragment {

    private static final String ARG_BEVERAGE_ID = "crime_id";

    private EditText mId;
    private EditText mName;
    private EditText mPack;
    private EditText mPrice;
    private CheckBox mActive;

    private Beverage mBeverage;

    public static BeverageFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(ARG_BEVERAGE_ID, id);

        BeverageFragment fragment = new BeverageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String beverageId = getArguments().getString(ARG_BEVERAGE_ID);
        mBeverage = BeverageCollection.get(getActivity()).getBeverage(beverageId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beverage, container, false);

        mId = (EditText) view.findViewById(R.id.beverage_id);
        mName = (EditText) view.findViewById(R.id.beverage_name);
        mPack = (EditText) view.findViewById(R.id.beverage_pack);
        mPrice = (EditText) view.findViewById(R.id.beverage_price);
        mActive = (CheckBox) view.findViewById(R.id.beverage_active);

        mId.setText(mBeverage.getId());
        mName.setText(mBeverage.getName());
        mPack.setText(mBeverage.getPack());
        mPrice.setText(Double.toString(mBeverage.getPrice()));
        mActive.setChecked(mBeverage.isActive());

        mId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mPack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setPack(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setPrice(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBeverage.setActive(isChecked);
            }
        });

        return view;
    }
}
