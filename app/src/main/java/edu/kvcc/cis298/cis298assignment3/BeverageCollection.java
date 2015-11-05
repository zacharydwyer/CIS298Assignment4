package edu.kvcc.cis298.cis298assignment3;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by David Barnes on 11/3/2015.
 */
public class BeverageCollection {

    private static BeverageCollection sBeverageCollection;

    private Context mContext;

    private List<Beverage> mBeverages;

    public static BeverageCollection get(Context context) {
        if (sBeverageCollection == null) {
            sBeverageCollection = new BeverageCollection(context);
        }
        return sBeverageCollection;
    }

    private BeverageCollection(Context context) {
        mBeverages = new ArrayList<>();
        mContext = context;
        loadBeverageList();
    }

    public List<Beverage> getBeverages() {
        return mBeverages;
    }

    public Beverage getCrime(String Id) {
        for (Beverage beverage : mBeverages) {
            if (beverage.getId().equals(Id)) {
                return beverage;
            }
        }
        return null;
    }

    private void loadBeverageList() {

        Scanner scanner;

        try {

            scanner = new Scanner(mContext.getResources().openRawResource(R.raw.beverage_list));

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                String parts[] = line.split(",");

                String id = parts[0];
                String name = parts[1];
                String pack = parts[2];
                double price = Double.parseDouble(parts[3]);
                boolean active;

                active = ((parts[4].equals("true")));
                mBeverages.add(new Beverage(id, name, pack, price, active));
            }

        } catch (Exception e) {
            Log.e("Read CSV", e.toString());
        }
    }
}
