package com.abdulaziz.egytronica.ui.other_section.news_fragment;

import com.abdulaziz.egytronica.data.model.News;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 6/7/17.
 */

public interface NewsListBaseView extends BaseView{

    void newsListReqComplete();
    void newsListReqSuccess(ArrayList<News> newsList);
    void newsListReqError(String e);

}
