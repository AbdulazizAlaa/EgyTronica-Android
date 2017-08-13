package com.abdulaziz.egytronica.ui.board.create;

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
 * Created by abdulaziz on 3/4/17.
 */

public class CreateBoardPresenter extends BasePresenter<CreateBoardBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public CreateBoardPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(CreateBoardBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void createBoard(Request r){
        Log.i(GlobalEntities.CREATE_BOARD_PRESENTER_TAG, "createBoard: ");
        checkViewAttached();
        mSubscription = mDataManager.createBoard(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.CREATE_BOARD_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().createBoardReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.CREATE_BOARD_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.CREATE_BOARD_PRESENTER_TAG);
                        getBaseView().createBoardReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.CREATE_BOARD_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().createBoardReqSuccess(response.board);
                    }
                });
    }

}
