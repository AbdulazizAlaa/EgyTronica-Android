package com.abdulaziz.egytronica.ui.news;

import com.abdulaziz.egytronica.data.model.News;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 6/7/17.
 */

public interface NewsBaseView  extends BaseView {

    void newsReqComplete();
    void newsReqSuccess(News news);
    void newsReqError(String e);

}

