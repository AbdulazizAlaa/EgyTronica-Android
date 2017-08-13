package com.abdulaziz.egytronica.ui.login;

import com.abdulaziz.egytronica.ui.base.BaseView;

/**
 * Created by abdulaziz on 2/26/17.
 */

public interface LoginBaseView extends BaseView {

    void loginReqCompleted();
    void loginReqError(String e);
    void loginReqSuccess(String token);
}