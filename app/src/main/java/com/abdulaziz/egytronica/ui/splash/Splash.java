package com.abdulaziz.egytronica.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.DatabaseHelper;
import com.abdulaziz.egytronica.data.local.DbOpenHelper;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.remote.Service;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.ui.landing.LandingPage;
import com.abdulaziz.egytronica.ui.mobile_pin.MobilePinActivity;
import com.abdulaziz.egytronica.ui.on_boarding.OnBoarding;
import com.abdulaziz.egytronica.utils.GlobalEntities;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1500;

    View mainView;

    public static Intent getStartIntent(Context context){
        Intent i = new Intent(context, Splash.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating the preferencesHelper
        PreferencesHelper preferencesHelper = PreferencesHelper.getInstance(this);

        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(this.getApplicationContext());
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(dbOpenHelper);
        Service service = Service.Creator.getService();
        DataManager dataManager = DataManager.getInstance(this, service, databaseHelper, preferencesHelper);

        //check for on boarding progress flags
        Boolean loginFlag = preferencesHelper.getBoolean(PreferencesHelper.Key.IS_USER_LOGGED_IN, false);
        String onBoardingFlag = preferencesHelper.getString(PreferencesHelper.Key.ON_BOARDING_PROGRESS, "");

        Log.i(GlobalEntities.SPLASH_ACTIVITY, "onCreate: "+loginFlag);

        if(onBoardingFlag.equals(GlobalEntities.MOBILE_PIN_ACTIVITY)){
            Log.i(GlobalEntities.SPLASH_ACTIVITY, "onCreate: pin");
            Intent i = MobilePinActivity.getStartIntent(this);
            startActivity(i);
            finish();
        }
        if(loginFlag){
            Log.i(GlobalEntities.SPLASH_ACTIVITY, "onCreate: login");
            Intent i = HomeActivity.getStartIntent(this);
            startActivity(i);
            finish();
        }else{
            Log.i(GlobalEntities.SPLASH_ACTIVITY, "onCreate: splash");
            setContentView(R.layout.activity_splash);

            mainView = findViewById(R.id.main_view);

            Timer splashTimer = new Timer();
            splashTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.i(GlobalEntities.SPLASH_ACTIVITY, "Splash timer");
                    jump();
                }
            }, SPLASH_TIME_OUT);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(GlobalEntities.SPLASH_ACTIVITY, "Touch event");

        jump();

        return super.onTouchEvent(event);
    }

    private void jump(){
        if(isFinishing())
            return;

        startActivity(LandingPage.getStartIntent(this));
        finish();
    }
}
