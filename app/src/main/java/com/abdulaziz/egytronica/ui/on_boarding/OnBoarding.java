package com.abdulaziz.egytronica.ui.on_boarding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.ui.landing.LandingPage;
import com.abdulaziz.egytronica.ui.mobile_pin.MobilePinActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.mvp.DefaultObserver;
import com.abdulaziz.egytronica.utils.textInputComponent.CustomTextInputEditText;
import com.abdulaziz.egytronica.utils.textInputComponent.CustomTextInputLayout;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.RxValidator;
import com.hbb20.CountryCodePicker;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class OnBoarding extends AppCompatActivity implements View.OnClickListener, OnBoardingBaseView{

    private Context mContext;

    private Button confirmBtn;

    private CustomTextInputEditText nameED;
    private CustomTextInputEditText emailED;
    private CustomTextInputEditText passwordED;
    private CustomTextInputEditText phoneED;

    private TextInputLayout nameTIL;
    private CustomTextInputLayout emailTIL;
    private TextInputLayout passwordTIL;
    private TextInputLayout phoneTIL;

    private CountryCodePicker ccp;

    private View main_view;
    private ProgressDialog progress;

    private Request request;
    private OnBoardingPresenter mPresenter;

    public static Intent getStartIntent(Context mContext){
        Intent i = new Intent(mContext, OnBoarding.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);


        init();

    }

    private void init(){

        mContext = this;

        //creating presenter
        mPresenter = new OnBoardingPresenter(this, DataManager.getInstance());
        mPresenter.attachView(this);

        //progress Dialog
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setTitle("Please Wait..");

        //main view used for snackbar
        main_view = findViewById(R.id.main_view);

        //confirm button
        confirmBtn = (Button) findViewById(R.id.on_boarding_activity_confirm_btn);
        confirmBtn.setOnClickListener(this);

        //init country code picker
        ccp = (CountryCodePicker) findViewById(R.id.on_boarding_activity_ccp);

        //edit texts
        nameTIL = (TextInputLayout) findViewById(R.id.on_boarding_activity_name_til);
        emailTIL = (CustomTextInputLayout) findViewById(R.id.on_boarding_activity_email_til);
        passwordTIL = (TextInputLayout) findViewById(R.id.on_boarding_activity_password_til);
        phoneTIL = (TextInputLayout) findViewById(R.id.on_boarding_activity_phone_til);

        nameED = (CustomTextInputEditText) findViewById(R.id.on_boarding_activity_name_ed);
        nameED.setTIL(nameTIL);

        emailED = (CustomTextInputEditText) findViewById(R.id.on_boarding_activity_email_ed);
        emailED.setTIL(emailTIL);

        passwordED = (CustomTextInputEditText) findViewById(R.id.on_boarding_activity_password_ed);
        passwordED.setTIL(passwordTIL);

        phoneED = (CustomTextInputEditText) findViewById(R.id.on_boarding_activity_phone_ed);
        phoneED.setTIL(phoneTIL);

        Observer<RxValidationResult<EditText>> observer = new DefaultObserver(this, GlobalEntities.ON_BOARDING_ACTIVITY);

        Observable<RxValidationResult<EditText>> nameValidator =
                RxValidator.createFor(nameED)
                        .nonEmpty()
                        .minLength(3, "At least 3 characters")
                        .pattern("Characters Only", "^[a-zA-Z\\s]+$")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        nameValidator.subscribe(observer);

        Observable<RxValidationResult<EditText>> emailValidator =
                RxValidator.createFor(emailED)
                        .nonEmpty("Cannot be empty")
                        .email()
                        .onValueChanged()
                        .onFocusChanged()
                        .toObservable();
        emailValidator
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        Observable<RxValidationResult<EditText>> passwordValidator =
                RxValidator.createFor(passwordED)
                        .nonEmpty()
                        .minLength(6, "Password must be more than 6 letters and digits")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        passwordValidator.subscribe(observer);

        Observable<RxValidationResult<EditText>> phoneValidator =
                RxValidator.createFor(phoneED)
                        .nonEmpty()
                        .minLength(6, "Phone number must be more than 6 digits")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        phoneValidator.subscribe(observer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.on_boarding_activity_confirm_btn:
                Log.i(GlobalEntities.ON_BOARDING_ACTIVITY, "onClick: confirm button");

                if(nameED.isValid() && emailED.isValid() && passwordED.isValid() && phoneED.isValid()) {
                    handleProgress("Registering User...");

                    String name = nameED.getText().toString();
                    String email = emailED.getText().toString();
                    String password = passwordED.getText().toString();
                    String phone = ccp.getSelectedCountryCodeWithPlus() + phoneED.getText().toString();

                    request = Request.getInstance();
                    request.setName(name);
                    request.setEmail(email);
                    request.setPassword(password);
                    request.setPhone(phone);

                    mPresenter.register(request);

                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setMessage("First Fix Errors and Fill Form!!")
                            .setTitle("Error")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }
                break;
        }
    }

    @Override
    public void sendRegisterReqSuccess(String token) {
        Log.i(GlobalEntities.ON_BOARDING_ACTIVITY, "sendRegisterReqSuccess: ");
        PreferencesHelper.getInstance().put(PreferencesHelper.Key.AUTH_TOKEN, token);
        PreferencesHelper.getInstance().put(PreferencesHelper.Key.ON_BOARDING_PROGRESS, GlobalEntities.MOBILE_PIN_ACTIVITY);

        Intent i = MobilePinActivity.getStartIntent(this);
        startActivity(i);
    }

    @Override
    public void sendRegisterReqError(String e) {
        Log.i(GlobalEntities.ON_BOARDING_ACTIVITY, "sendRegisterReqError: "+e);
        handleProgress("");
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();


    }

    @Override
    public void sendRegisterReqComplete() {
        Log.i(GlobalEntities.ON_BOARDING_ACTIVITY, "sendRegisterReqComplete: ");
        handleProgress("");
    }
}
