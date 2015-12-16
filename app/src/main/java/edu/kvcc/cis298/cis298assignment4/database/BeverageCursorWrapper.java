package edu.kvcc.cis298.cis298assignment4.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.kvcc.cis298.cis298assignment4.Beverage;

import static edu.kvcc.cis298.cis298assignment4.database.BeverageDbSchema.*;

public class BeverageCursorWrapper  extends CursorWrapper {

    // Cursor wrapper needs a cursor.
    public BeverageCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // Get the beverage out of the cursor.
    public Beverage getBeverage() {

        // Retrieve all the values out of the database.
        String idString = getString(getColumnIndex(BeverageTable.Cols.ID));
        String nameString = getString(getColumnIndex(BeverageTable.Cols.NAME));
        String packString = getString(getColumnIndex(BeverageTable.Cols.PACK));
        double priceDouble = getDouble(getColumnIndex(BeverageTable.Cols.PRICE));
        int isActiveInt = getInt(getColumnIndex(BeverageTable.Cols.ACTIVE));

        // Create a beverage using those values.
        Beverage beverage = new Beverage(idString);
        beverage.setName(nameString);
        beverage.setPack(packString);
        beverage.setPrice(priceDouble);
        beverage.setActive(isActiveInt != 0); // non-zero is true

        // Finally, return the beverage.
        return beverage;
    }
}
