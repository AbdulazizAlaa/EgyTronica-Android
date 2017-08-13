package com.abdulaziz.egytronica.ui.mobile_pin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.ui.on_boarding.OnBoarding;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.Utils;
import com.alimuzaffar.lib.pin.PinEntryEditText;

public class MobilePinActivity extends AppCompatActivity implements View.OnClickListener, MobilePinBaseView{

    private View main_view;
    private TextView descTV;
    private PinEntryEditText pinView;
    private Button confirmBtn;
    private TextView resendTV;

    private ProgressDialog progress;

    private String pinCode;
    private Request request;

    private MobilePinPresenter mPresenter;

    public static Intent getStartIntent(Context mContext){
        Intent i = new Intent(mContext, MobilePinActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_pin);

        init();
    }

    private void init(){
        //main view
        main_view = findViewById(R.id.main_view);

        //progress Dialog
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setTitle("Please Wait..");

        //description text view
        descTV = (TextView) findViewById(R.id.mobile_pin_desc_tv);
        //resend text view
        resendTV = (TextView) findViewById(R.id.mobile_pin_resend_message_tv);

        confirmBtn = (Button) findViewById(R.id.mobile_pin_confirm_btn);

        resendTV.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        pinView = (PinEntryEditText) findViewById(R.id.mobile_pin_pinView);

        pinView.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence code) {
                Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "onPinEntered: ");
                pinCode = code.toString();
            }
        });

        //init presenter
        mPresenter = new MobilePinPresenter(this, DataManager.getInstance());
        mPresenter.attachView(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mobile_pin_confirm_btn:
                Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "onClick: confirm");
                handleProgress("Verifying...");

                request = Request.getInstance();
                request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
                request.setPin(pinCode);

                mPresenter.verify(request);

                break;
            case R.id.mobile_pin_resend_message_tv:
                Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "onClick: resend");
                handleProgress("Resend Message...");

                request = Request.getInstance();
                request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));

                mPresenter.resend(request);

                break;
        }
    }

    private void handleProgress(String message){
        if(progress.isShowing()){
            Log.i(GlobalEntities.ON_BOARDING_ACTIVITY, "handleProgress: showing");
            progress.dismiss();
        }else{
            Log.i(GlobalEntities.ON_BOARDING_ACTIVITY, "handleProgress: not showing");
            progress.setMessage(message);
            progress.show();
        }
    }

    @Override
    public void verifyPinReqSuccess(String token) {
        Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "verifyPinReqSuccess: ");
//        PreferencesHelper.getInstance().put(PreferencesHelper.Key.AUTH_TOKEN, token);
        PreferencesHelper.getInstance().put(PreferencesHelper.Key.ON_BOARDING_PROGRESS, "");

        Intent i = HomeActivity.getStartIntent(this);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void verifyPinReqError(String e) {
        Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "verifyPinReqError: "+e);
        handleProgress("");
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();

        //if error message contains any data about invalid token
        Utils.logout(this, e);

    }

    @Override
    public void verifyPinReqComplete() {
        Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "verifyPinReqComplete: ");
        handleProgress("");
    }

    @Override
    public void resendPinReqSuccess() {
        Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "resendPinReqSuccess: ");

    }

    @Override
    public void resendPinReqError(String e) {
        Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "resendPinReqError: "+e);
        handleProgress("");
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();

        //if error message contains any data about invalid token
        Utils.logout(this, e);

    }

    @Override
    public void resendPinReqComplete() {
        Log.i(GlobalEntities.MOBILE_PIN_ACTIVITY, "resendPinReqComplete: ");
        handleProgress("");

    }
}
