package edu.kvcc.cis298.cis298assignment4.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.kvcc.cis298.cis298assignment4.database.BeverageDbSchema.BeverageTable;       // Added so we don't have to type a longer qualifier when referencing the BeverageTable.

public class BeverageBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "beverageBase.db";

    public BeverageBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table with the table name "beverageBase.db" with the specified columns.
        db.execSQL("create table " + BeverageTable.NAME + "(" +
                BeverageTable.Cols.ID + ", " +
                BeverageTable.Cols.NAME + ", " +
                BeverageTable.Cols.PACK + ", " +
                BeverageTable.Cols.PRICE + ", " +
                BeverageTable.Cols.ACTIVE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
