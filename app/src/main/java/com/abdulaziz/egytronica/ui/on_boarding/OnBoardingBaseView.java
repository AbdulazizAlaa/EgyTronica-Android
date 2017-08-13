package com.abdulaziz.egytronica.ui.on_boarding;

import com.abdulaziz.egytronica.ui.base.BaseView;

/**
 * Created by abdulaziz on 2/27/17.
 */

public interface OnBoardingBaseView extends BaseView {

    void sendRegisterReqSuccess(String token);
    void sendRegisterReqError(String e);
    void sendRegisterReqComplete();

}
