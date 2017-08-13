package com.abdulaziz.egytronica.ui.news;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.News;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.utils.GlobalEntities;

public class NewsActivity extends AppCompatActivity implements NewsBaseView{

    private TextView titleTV;
    private TextView contentTV;

    private View mainView;

    private NewsPresenter presenter;
    private Request request;

    private String id;

    public static Intent getStartIntent(Context context){
        Intent i = new Intent(context, NewsActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                id = getIntent().getStringExtra(GlobalEntities.NEWS_ID_TAG);
            }
        }

        init();
    }

    private void init(){
        //title and content tv
        titleTV = (TextView) findViewById(R.id.news_title_tv);
        contentTV = (TextView) findViewById(R.id.news_content_tv);

        //main view
        mainView = findViewById(R.id.main_view);

        //presenter
        presenter = new NewsPresenter(this, DataManager.getInstance());
        presenter.attachView(this);

        request = Request.getInstance();
        request.setId(id);

        presenter.getNews(request);
    }

    @Override
    public void newsReqComplete() {
        Log.i(GlobalEntities.NEWS_PRESENTER_TAG, "newsReqComplete: ");
    }

    @Override
    public void newsReqSuccess(News news) {
        Log.i(GlobalEntities.NEWS_PRESENTER_TAG, "newsReqSuccess: ");

        titleTV.setText(news.getTitle());
        contentTV.setText(news.getContent());

    }

    @Override
    public void newsReqError(String e) {
        Log.i(GlobalEntities.NEWS_PRESENTER_TAG, "newsReqError: "+e);
        Snackbar.make(mainView, e, Snackbar.LENGTH_SHORT).show();
    }
}
