package com.abdulaziz.egytronica.ui.home;

import android.content.Context;
import android.util.Log;

import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.data.model.Response;
import com.abdulaziz.egytronica.ui.base.BasePresenter;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.Utils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abdulaziz on 3/22/17.
 */

public class HomePresenter extends BasePresenter<HomeBaseView> {


    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public HomePresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(HomeBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();

        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void sendRegistrationId(Request r){
        Log.i(GlobalEntities.HOME_PRESENTER_TAG, "send Reg id: ");
        checkViewAttached();
        mSubscription = mDataManager.sendRegistrationId(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.HOME_PRESENTER_TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.i(GlobalEntities.LOGIN_PHONE_PRESENTER_TAG, "onError: ");
                        String error = Utils.handleThrowable(e, GlobalEntities.HOME_PRESENTER_TAG);
                        getBaseView().sendRegIdReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.HOME_PRESENTER_TAG, "onNext: ");

                        getBaseView().sendRegIdReqSuccess();
                    }
                });

    }
}
