package com.abdulaziz.egytronica.ui.home;

import com.abdulaziz.egytronica.ui.base.BaseView;

/**
 * Created by abdulaziz on 3/22/17.
 */

public interface HomeBaseView extends BaseView {

    void sendRegIdReqError(String e);
    void sendRegIdReqSuccess();

}
