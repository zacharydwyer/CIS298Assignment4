package edu.kvcc.cis298.cis298assignment4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.kvcc.cis298.cis298assignment4.database.BeverageBaseHelper;
import edu.kvcc.cis298.cis298assignment4.database.BeverageCursorWrapper;
import edu.kvcc.cis298.cis298assignment4.database.BeverageDbSchema;

import static edu.kvcc.cis298.cis298assignment4.database.BeverageDbSchema.*;


public class BeverageCollection {

    private static final String TAG = "BeverageCollection";

    private static BeverageCollection sBeverageCollection;              // Singleton instance
    private Context mContext;                                           // Context of application
    private SQLiteDatabase mDatabase;                                   // The database itself

    //public static method to get the single instance of this class
    public static BeverageCollection get(Context context) {
        //If the collection is null
        if (sBeverageCollection == null) {
            //make a new one
            sBeverageCollection = new BeverageCollection(context);
        }
        //regardless of whether it was just made or not, return the instance
        return sBeverageCollection;
    }

    //Private constructor to create a new BeverageCollection
    private BeverageCollection(Context context) {
        mContext = context.getApplicationContext();                             // Set the context of this to the application context. Not sure why we have to change this though.

        // Open the database file. If the first time its been created, call onCreate. If not the first time, call onUpgrade.
        mDatabase = new BeverageBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Add a beverage to the database.
     * @param b
     */
    public void addBeverage(Beverage b) {
        // Get the content values for the passed in beverage
        ContentValues contentValuesForBeverage = getContentValues(b);

        // Insert the set of content values into the BeverageTable.
        mDatabase.insert(BeverageTable.NAME, null, contentValuesForBeverage);
    }

    public List<Beverage> getBeverages() {
        List<Beverage> beverages = new ArrayList<>();

        // Return THE ENTIRE DATABASE.
        BeverageCursorWrapper cursor = queryBeverages(null, null);

        try {
            cursor.moveToFirst();       // Start at the first row.

            // While we are not at the position after the last row (meaning while there's still rows left to read)
            while (! cursor.isAfterLast()) {

                // Make the beverageCursorWrapper (cursor) create a beverage from the data in the current row that it is on.
                beverages.add(cursor.getBeverage());

                // Move to the next row.
                cursor.moveToNext();
            }
        } finally {
            // Close this cursor, android will yell at us if we don't. Or we'll run out of open file handles and crash our app.
            cursor.close();
        }

        // Finally, return the List<Beverage> of beverages we just created.
        return beverages;
    }

    public Beverage getBeverage(String id) {

        // Query to get the beverage(s) with the given "id" argument in the ID column.
        BeverageCursorWrapper cursor = queryBeverages(
                BeverageTable.Cols.ID + " = ?",
                new String[] { id }
        );

        try {
            // If there were no beverages with that id...
            if (cursor.getCount() == 0) {

                // return nothing.
                return null;
            }

            // Go to the first row.
            cursor.moveToFirst();

            // Create a beverage out of the row we're on, and return it.
            return cursor.getBeverage();
        } finally {

            // Close your cursor.
            cursor.close();
        }
    }

    public void updateBeverage(Beverage beverage) {

        // Get usable key-value package for the beverage.
        ContentValues values = getContentValues(beverage);

        // Update the Beverage table using the ContentValues package we just got, where the Beverage's ID is equal to the beverage that was passed in.
        mDatabase.update(BeverageTable.NAME, values, BeverageTable.Cols.ID + " = ?", new String[] { beverage.getId() });
    }

    // Creates a package of ContentValues out of a Beverage, which can later be used to add to the database.
    private static ContentValues getContentValues(Beverage beverage) {
        // New pack of values.
        ContentValues values = new ContentValues();

        // Populate this pack of values.
        values.put(BeverageTable.Cols.ID, beverage.getId());
        values.put(BeverageTable.Cols.NAME, beverage.getName());
        values.put(BeverageTable.Cols.PACK, beverage.getPack());
        values.put(BeverageTable.Cols.PRICE, beverage.getPrice());
        values.put(BeverageTable.Cols.ACTIVE, beverage.isActive() ? 1 : 0);     // stores 1 if true, 0 is false.

        return values;      // Return the ContentValues package.
    }

    private BeverageCursorWrapper queryBeverages(String whereClause, String[] whereArgs) {

        // Query the database with the passed in arguments.
        Cursor cursor = mDatabase.query(
                BeverageTable.NAME,         // Name of table we are querying
                null,                       // Columns we are selecting (null means the entire row)
                whereClause,                // What column are we looking at? (for example, the ID)
                whereArgs,                  // What must the value of that column be in order for us to return the row?
                null,                       // Group by
                null,                       // Having
                null                        // Order by
        );

        return new BeverageCursorWrapper(cursor);
    }

    // Populate beverage list with a brand new list, start from scratch.
    // This will actually add values into the database.
    public void setBeverages(List<Beverage> beverageList) {

        // For each beverage in beverageList...
        for (Beverage currentBeverage : beverageList) {

            this.addBeverage(currentBeverage);
        }
    }
}
