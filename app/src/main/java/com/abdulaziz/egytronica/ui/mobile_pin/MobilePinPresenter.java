package com.abdulaziz.egytronica.ui.mobile_pin;

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
 * Created by abdulaziz on 3/2/17.
 */

public class MobilePinPresenter extends BasePresenter<MobilePinBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public MobilePinPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(MobilePinBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void verify(Request r){
        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "verify: ");
        checkViewAttached();
        mSubscription = mDataManager.verifyMobile(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().verifyPinReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.MOBILE_PIN_PRESENTER_TAG);
                        getBaseView().verifyPinReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "onNext: "+response.token);

                        getBaseView().verifyPinReqSuccess(response.token);
                    }
                });
    }

    public void resend(Request r){
        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "resend: ");
        checkViewAttached();
        mSubscription = mDataManager.resendPin(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().resendPinReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.MOBILE_PIN_PRESENTER_TAG);
                        getBaseView().resendPinReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.MOBILE_PIN_PRESENTER_TAG, "onNext: ");

                        getBaseView().resendPinReqSuccess();
                    }
                });
    }
}

