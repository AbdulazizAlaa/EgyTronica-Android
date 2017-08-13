package com.abdulaziz.egytronica.ui.home.member_boards;

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
 * Created by abdulaziz on 3/7/17.
 */

public class MemberBoardsPresenter extends BasePresenter<MembersBoardsBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public MemberBoardsPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(MembersBoardsBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void getBoards(Request r){
        Log.i(GlobalEntities.MEMBERS_BOARDS_PRESENTER_TAG, "getBoards: ");
        checkViewAttached();
        mSubscription = mDataManager.getMemberBoards(r)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.MEMBERS_BOARDS_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().boardsReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.MEMBERS_BOARDS_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.MEMBERS_BOARDS_PRESENTER_TAG);
                        getBaseView().boardsReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.MEMBERS_BOARDS_PRESENTER_TAG, "onNext: ");

                        getBaseView().boardsReqSuccess(response.boards);
                    }
                });
    }
}
