package com.abdulaziz.egytronica.ui.board.view.components;

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
 * Created by abdulaziz on 3/9/17.
 */

public class ComponentsPresenter extends BasePresenter<ComponentsBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public ComponentsPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ComponentsBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void addComponent(Request r){
        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "addComponent: ");
        checkViewAttached();
        mSubscription = mDataManager.addComponent(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.COMPONENTS_PRESENTER_TAG);
                        getBaseView().addReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().addReqSuccess(response.component);
                    }
                });
    }

    public void getComponents(Request r){
        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "getComponents: ");
        checkViewAttached();
        mSubscription = mDataManager.getComponents(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().componentsReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.COMPONENTS_PRESENTER_TAG);
                        getBaseView().componentsReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().componentsReqSuccess(response.components, response.board);
                    }
                });
    }

    public void sendComponentControl(Request r){
        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "sendComponentControl: ");
        checkViewAttached();
        mSubscription = mDataManager.sendComponentControl(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.COMPONENTS_PRESENTER_TAG);
                        getBaseView().sendControlReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.COMPONENTS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().sendControlReqSuccess(response.component);
                    }
                });
    }

}