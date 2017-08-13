package com.abdulaziz.egytronica.ui.on_boarding;

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
 * Created by abdulaziz on 2/27/17.
 */

public class OnBoardingPresenter extends BasePresenter<OnBoardingBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public OnBoardingPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(OnBoardingBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void register(Request r){
            Log.i(GlobalEntities.ON_BOARDING_PRESENTER_TAG, "register: ");
            checkViewAttached();
            mSubscription = mDataManager.register(r)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onCompleted() {
                            Log.i(GlobalEntities.ON_BOARDING_PRESENTER_TAG, "onCompleted: ");
                            getBaseView().sendRegisterReqComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(GlobalEntities.ON_BOARDING_PRESENTER_TAG, "onError: "+e.getMessage());

                            String error = Utils.handleThrowable(e, GlobalEntities.ON_BOARDING_PRESENTER_TAG);
                            getBaseView().sendRegisterReqError(error);
                        }

                        @Override
                        public void onNext(Response response) {
                            Log.i(GlobalEntities.ON_BOARDING_PRESENTER_TAG, "onNext: ");

                            getBaseView().sendRegisterReqSuccess(response.token);
                        }
                    });
            }
}
