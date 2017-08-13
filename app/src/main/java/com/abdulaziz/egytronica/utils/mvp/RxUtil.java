package com.abdulaziz.egytronica.utils.mvp;

import rx.Subscription;

/**
 * Created by abdulaziz on 9/27/16.
 */
public class RxUtil {

    public static void unsubscribe(Subscription subscription){
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}
