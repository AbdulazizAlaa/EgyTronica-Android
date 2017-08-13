package com.abdulaziz.egytronica.ui.landing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.ui.login.LoginActivity;
import com.abdulaziz.egytronica.ui.on_boarding.OnBoarding;
import com.abdulaziz.egytronica.ui.other_section.OtherActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;


public class LandingPage extends AppCompatActivity implements View.OnClickListener {

    private Button sign_up_btn;
    private Button login_btn;
//    private TextView login_tv;
    private TextView skip_tv;
    private TextView intro_tv;
    private TextView intro_desc_tv;
    private Intent i;

    public static Intent getStartIntent(Context context){
        Intent i = new Intent(context, LandingPage.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        init();
    }


    private void init(){
        sign_up_btn = (Button) findViewById(R.id.landing_signup_btn);
        login_btn = (Button) findViewById(R.id.landing_login_btn);
        skip_tv = (TextView) findViewById(R.id.landing_skip_tv);
        intro_tv = (TextView) findViewById(R.id.landing_intro_tv);
        intro_desc_tv = (TextView) findViewById(R.id.landing_intro_desc_tv);

        intro_tv.setText(Html.fromHtml(getString(R.string.landing_intro_text)));
        intro_desc_tv.setText(Html.fromHtml(getString(R.string.landing_intro_desc_text)));

        sign_up_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        skip_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.landing_skip_tv:
                Log.i(GlobalEntities.LANDING_ACTIVITY, "onClick: skip btn");

                i = OtherActivity.getStartIntent(this);
                startActivity(i);

                break;
            case R.id.landing_login_btn:
                Log.i(GlobalEntities.LANDING_ACTIVITY, "onClick: login btn");

                i = LoginActivity.getStartIntent(this);
                startActivity(i);

                break;
            case R.id.landing_signup_btn:
                Log.i(GlobalEntities.LANDING_ACTIVITY, "onClick: sign up btn");

                i = OnBoarding.getStartIntent(this);
                startActivity(i);

                break;
        }

    }
}
