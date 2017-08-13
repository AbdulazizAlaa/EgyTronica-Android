package com.abdulaziz.egytronica.data.local;

import android.util.Log;

import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by abdulaziz on 10/24/16.
 */
public class DatabaseHelper {

    private static DatabaseHelper databaseHelper;

    private BriteDatabase mDB;

    private DatabaseHelper(DbOpenHelper dbOpenHelper){
        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "DatabaseHelper: created");
        mDB = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public static DatabaseHelper getInstance(DbOpenHelper dbOpenHelper){
        if(databaseHelper == null){
            databaseHelper = new DatabaseHelper(dbOpenHelper);
        }

        return databaseHelper;
    }

    public static boolean isNull(){ return databaseHelper == null; }

    public BriteDatabase getBriteDb(){ return mDB; }

//    public void emptyDatabase(){
//        mDB.delete(Db.TransactionTable.TABLE_NAME, null, null);
//    }


    //TODO remove transaction functions and replace with boards and board components functions
//    public Observable<Transaction> setTransactions(final Collection<Transaction> transactions){
//        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "DatabaseHelper: setTransactions: "+transactions.size());
//        return Observable.create(new Observable.OnSubscribe<Transaction>() {
//            @Override
//            public void call(Subscriber<? super Transaction> subscriber) {
//                if(subscriber.isUnsubscribed()) return;
//                BriteDatabase.Transaction transaction = mDB.newTransaction();
//                try {
//                    mDB.delete(Db.TransactionTable.TABLE_NAME, null);
//                    for(Transaction t : transactions){
//                        long result = mDB.insert(Db.TransactionTable.TABLE_NAME,
//                                Db.TransactionTable.toContentValues(t),
//                                SQLiteDatabase.CONFLICT_REPLACE);
////                        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "DatabaseHelper: setTransactions: Transaction: "+t.id+" result: "+result);
//
//                        if(result >= 0) subscriber.onNext(t);
//                    }
//                    transaction.markSuccessful();
//                    subscriber.onCompleted();
//                }finally {
//                    transaction.end();
//                }
//            }
//        });
//
//    }
//
//    public Observable<List<Transaction>> getTransactions(){
//        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "DatabaseHelper: getTransactions");
//        return mDB.createQuery(Db.TransactionTable.TABLE_NAME,
//                "SELECT * FROM " + Db.TransactionTable.TABLE_NAME )
//                .mapToList(new Func1<Cursor, Transaction>() {
//                    @Override
//                    public Transaction call(Cursor cursor) {
//                        Transaction transaction = Db.TransactionTable.parseCursor(cursor);
////                        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "Cursor Count:: "+cursor.getCount());
////                        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "---------------------------------------");
////                        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "id :: "+transaction.id);
////                        Log.i(GlobalEntities.DATABASE_HELPER_TAG, "title :: "+transaction.reason);
//                        return transaction;
//                    }
//                });
//    }

}
