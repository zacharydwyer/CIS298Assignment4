package edu.kvcc.cis298.cis298assignment4.database;


public class BeverageDbSchema {
    public static final class BeverageTable {
        public static final String NAME = "Beverages";          // Name of table

        // Beverage table column name resolvers
        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PACK = "pack";
            public static final String PRICE = "price";
            public static final String ACTIVE = "active";
        }
    }
}
