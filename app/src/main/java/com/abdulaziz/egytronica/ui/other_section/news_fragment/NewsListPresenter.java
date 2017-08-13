package com.abdulaziz.egytronica.ui.other_section.news_fragment;

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
 * Created by abdulaziz on 6/7/17.
 */

public class NewsListPresenter  extends BasePresenter<NewsListBaseView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private final Context mContext;

    public NewsListPresenter(Context mContext, DataManager mDataManager) {
        this.mContext = mContext;
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(NewsListBaseView baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null) mSubscription.unsubscribe();
    }

    public void getNewsList(){
        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "get news list: ");
        checkViewAttached();
        mSubscription = mDataManager.getNewsList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "onCompleted: ");
                        getBaseView().newsListReqComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "onError: "+e.getMessage());

                        String error = Utils.handleThrowable(e, GlobalEntities.NEWS_LIST_PRESENTER_TAG);
                        getBaseView().newsListReqError(error);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "onNext: "+response.status);

                        getBaseView().newsListReqSuccess(response.newsList);
                    }
                });
    }

}
