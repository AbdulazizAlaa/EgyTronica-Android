package com.abdulaziz.egytronica.ui.board.view.details;

import android.content.Context;
import android.util.Log;

import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.data.model.Response;
import com.abdulaziz.egytronica.data.model.User;
import com.abdulaziz.egytronica.ui.base.BasePresenter;
import com.abdulaziz.egytronica.ui.base.BaseView;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.Utils;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abdulaziz on 3/3/17.
 */

public class DetailsPresenter extends BasePresenter<DetailsBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public DetailsPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(DetailsBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void getBoard(Request r){
        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "getBoard: ");
        checkViewAttached();
        mSubscription = mDataManager.getBoard(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().boardReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.DETAILS_PRESENTER_TAG);
                        getBaseView().boardReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().boardReqSuccess(response.board);
                    }
                });
    }

    public void addMember(Request r){
        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "addMember: ");
        checkViewAttached();
        mSubscription = mDataManager.addMember(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.DETAILS_PRESENTER_TAG);
                        getBaseView().addReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().addReqSuccess(response.user);
                    }
                });
    }

    public void removeMember(Request r){
        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "removeMember: ");
        checkViewAttached();
        mSubscription = mDataManager.removeMember(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.DETAILS_PRESENTER_TAG);
                        getBaseView().removeReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().removeReqSuccess();
                    }
                });
    }

    public void getMembers(Request r){
        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "getMembers: ");
        checkViewAttached();
        mSubscription = mDataManager.getMembers(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().membersReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.DETAILS_PRESENTER_TAG);
                        getBaseView().membersReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.DETAILS_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().membersReqSuccess(response.users);
                    }
                });
    }

}
