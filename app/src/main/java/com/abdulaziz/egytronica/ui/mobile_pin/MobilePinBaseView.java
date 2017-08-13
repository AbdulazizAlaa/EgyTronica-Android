package com.abdulaziz.egytronica.ui.mobile_pin;

import com.abdulaziz.egytronica.ui.base.BaseView;

/**
 * Created by abdulaziz on 3/2/17.
 */

public interface MobilePinBaseView extends BaseView {

    void verifyPinReqSuccess(String token);
    void verifyPinReqError(String e);
    void verifyPinReqComplete();

    void resendPinReqSuccess();
    void resendPinReqError(String e);
    void resendPinReqComplete();

}
