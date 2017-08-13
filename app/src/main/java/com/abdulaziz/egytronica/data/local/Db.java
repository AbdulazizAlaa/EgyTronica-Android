package com.abdulaziz.egytronica.data.local;

/**
 * Created by abdulaziz on 10/24/16.
 */
public class Db {

    public Db(){}

//    public abstract static class TransactionTable{
//        public static final String TABLE_NAME = "transactions";
//
//        public static final String COLUMN_ID = "id";
//        public static final String COLUMN_REASON = "reason";
//        public static final String COLUMN_VALUE = "value";
//        public static final String COLUMN_CURRENCY = "currency";
//        public static final String COLUMN_TYPE = "type";
//        public static final String COLUMN_DATE = "date";
//
//        public static final String CREATE =
//                "CREATE TABLE " + TABLE_NAME + " ( " +
//                        COLUMN_ID + " TEXT PRIMARY KEY, " +
//                        COLUMN_REASON + " TEXT NOT NULL, " +
//                        COLUMN_VALUE + " TEXT NOT NULL, " +
//                        COLUMN_CURRENCY + " TEXT NOT NULL, " +
//                        COLUMN_TYPE + " TEXT NOT NULL, " +
//                        COLUMN_DATE + " TEXT NOT NULL" +
//                " );";
//
//        public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
//
//        public static ContentValues toContentValues(Transaction transaction){
//            ContentValues values = new ContentValues();
//
//            values.put(COLUMN_ID, transaction.id);
//            values.put(COLUMN_REASON, transaction.reason);
//            values.put(COLUMN_VALUE, transaction.value);
//            values.put(COLUMN_TYPE, transaction.transactionType);
//            values.put(COLUMN_CURRENCY, transaction.currency);
//            values.put(COLUMN_DATE, transaction.date);
//
//            return values;
//        }
//
//        public static Transaction parseCursor(Cursor cursor){
//            Transaction transaction = new Transaction(
//                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
//                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON)),
//                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE)),
//                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY)),
//                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
//                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
//            );
//
//            return transaction;
//        }
//    }


}
