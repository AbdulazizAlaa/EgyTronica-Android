package com.abdulaziz.egytronica.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.abdulaziz.egytronica.utils.GlobalEntities;

/**
 * Created by abdulaziz on 10/24/16.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static DbOpenHelper dbOpenHelper;

    private final static String DATABASE_NAME = "olly-credit.db";
    private final static int DATABASE_VERSION = 1;

    private DbOpenHelper(Context context){ super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public static DbOpenHelper getInstance(Context context){
        if(dbOpenHelper == null){
            dbOpenHelper = new DbOpenHelper(context);
        }

        return dbOpenHelper;
    }

    public static boolean isNull(){ return dbOpenHelper == null; }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        Log.i(GlobalEntities.DB_OPEN_HELPER_TAG, "onConfigure: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(GlobalEntities.DB_OPEN_HELPER_TAG, "onCreate: ");

        db.beginTransaction();
        try {
//            db.execSQL(Db.TransactionTable.DROP);
//
//            db.execSQL(Db.TransactionTable.CREATE);

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(GlobalEntities.DB_OPEN_HELPER_TAG, "onUpgrade: ");

        db.beginTransaction();
        try {
//            db.execSQL(Db.TransactionTable.DROP);
//
//            db.execSQL(Db.TransactionTable.CREATE);

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
}
