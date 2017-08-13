package com.abdulaziz.egytronica.ui.login;

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
import android.text.Html;
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
import com.abdulaziz.egytronica.data.remote.EgyTronicaFirebaseMessagingService;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.ui.landing.LandingPage;
import com.abdulaziz.egytronica.ui.mobile_pin.MobilePinActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.Utils;
import com.abdulaziz.egytronica.utils.mvp.DefaultObserver;
import com.abdulaziz.egytronica.utils.textInputComponent.CustomTextInputEditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.RxValidator;
import com.google.firebase.iid.FirebaseInstanceId;

import rx.Observable;
import rx.Observer;

public class LoginActivity extends AppCompatActivity implements LoginBaseView, View.OnClickListener{

    private TextInputLayout emailTil;
    private CustomTextInputEditText emailED;

    private TextInputLayout passwordTil;
    private CustomTextInputEditText passwordED;

    private TextView desc;
    private Button loginBtn;

    private LoginPresenter mPresenter;

    private Request request;
    private String email;
    private String password;
    private View main_view;

    private ProgressDialog progress;

    public static Intent getStartIntent(Context mContext){
        Intent i = new Intent(mContext, LoginActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){

        //progress Dialog
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setTitle("Please Wait..");

        main_view = findViewById(R.id.main_view);

        //desc text view
        desc = (TextView) findViewById(R.id.login_activity_text_tv);
        desc.setText(Html.fromHtml(getString(R.string.login_desc)));

        //login button
        loginBtn = (Button) findViewById(R.id.login_activity_login_btn);
        loginBtn.setOnClickListener(this);

        //setting email edit text
        emailTil = (TextInputLayout) findViewById(R.id.login_activity_email_til);
        emailED = (CustomTextInputEditText) findViewById(R.id.login_activity_email_ed);
        emailED.setTIL(emailTil);

        Observer<RxValidationResult<EditText>> observer = new DefaultObserver(this, GlobalEntities.LOGIN_ACTIVITY);

        Observable<RxValidationResult<EditText>> emailValidator =
                RxValidator.createFor(emailED)
                        .nonEmpty()
                        .email()
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        emailValidator.subscribe(observer);

        //setting email edit text
        passwordTil = (TextInputLayout) findViewById(R.id.login_activity_password_til);
        passwordED = (CustomTextInputEditText) findViewById(R.id.login_activity_password_ed);
        passwordED.setTIL(passwordTil);

        Observable<RxValidationResult<EditText>> passwordValidator =
                RxValidator.createFor(passwordED)
                        .nonEmpty()
                        .minLength(6, "Password must be more than 6 letters and digits")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        passwordValidator.subscribe(observer);


        passwordED.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i(GlobalEntities.LOGIN_ACTIVITY, "onEditorAction: DONE key press");

                    Utils.hideSoftKeyboard(LoginActivity.this);

                    if(!passwordED.isValid()) {
                        passwordED.setValid(true);
                        passwordED.requestFocus();
                    }

                    return true;
                }
                else {
                    return false;
                }
            }
        });

        //creating presenter
        mPresenter = new LoginPresenter(this, DataManager.getInstance());
        mPresenter.attachView(this);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleProgress(String message){
        if(progress.isShowing()){
            Log.i(GlobalEntities.LOGIN_ACTIVITY, "handleProgress: showing");
            progress.dismiss();
        }else{
            Log.i(GlobalEntities.LOGIN_ACTIVITY, "handleProgress: not showing");
            progress.setMessage(message);
            progress.show();
        }
    }

    @Override
    public void loginReqCompleted() {
        Log.i(GlobalEntities.LOGIN_ACTIVITY, "loginReqCompleted: ");

    }

    @Override
    public void loginReqError(String e) {
        Log.i(GlobalEntities.LOGIN_ACTIVITY, "loginReqError: "+e);

        handleProgress("");
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void loginReqSuccess(String token) {
        Log.i(GlobalEntities.LOGIN_ACTIVITY, "loginReqSuccess: ");
        handleProgress("");
        PreferencesHelper.getInstance().put(PreferencesHelper.Key.AUTH_TOKEN, token);
        PreferencesHelper.getInstance().put(PreferencesHelper.Key.IS_USER_LOGGED_IN, true);

        Intent i = HomeActivity.getStartIntent(this);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_activity_login_btn:
                Log.i(GlobalEntities.LOGIN_ACTIVITY, "onClick: login");

                if(emailED.isValid() && passwordED.isValid()){
                    handleProgress("Login...");


                    password = passwordED.getText().toString();
                    email = emailED.getText().toString();

                    request = Request.getInstance();
                    request.setEmail(email);
                    request.setPassword(password);

                    mPresenter.login(request);
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
}
