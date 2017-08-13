package com.abdulaziz.egytronica.ui.home;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.data.remote.EgyTronicaFirebaseMessagingService;
import com.abdulaziz.egytronica.ui.board.create.CreateBoardActivity;
import com.abdulaziz.egytronica.ui.home.member_boards.MemberBoardsFragment;
import com.abdulaziz.egytronica.ui.home.my_boards.MyBoardsFragment;
import com.abdulaziz.egytronica.ui.landing.LandingPage;
import com.abdulaziz.egytronica.ui.login.LoginActivity;
import com.abdulaziz.egytronica.ui.login.LoginPresenter;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.PagerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;
import com.robohorse.pagerbullet.PagerBullet;

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener,
        MyBoardsFragment.OnMyBoardsFragmentInteractionListener,
        MemberBoardsFragment.OnMemberBoardsFragmentInteractionListener,
        HomeBaseView{

    private View main_view;

    private ProgressDialog progress;

    private HomePresenter mPresenter;

    private Request request;

    private FloatingActionButton fab;

    private PagerBullet viewPager;
    private PagerAdapter fragmentAdapter;

    private Board board;

    public static Intent getStartIntent(Context mContext){
        Intent i = new Intent(mContext, HomeActivity.class);
        return i;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(logoutBroadcast, new IntentFilter(GlobalEntities.LOGOUT_BROADCAST_EVENT));
        registerReceiver(registrationIDUpdate, new IntentFilter(GlobalEntities.REGISTRATION_ID_UPDATED_EVENT));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(logoutBroadcast);
        unregisterReceiver(registrationIDUpdate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerReceiver(logoutBroadcast, new IntentFilter(GlobalEntities.LOGOUT_BROADCAST_EVENT));
        registerReceiver(registrationIDUpdate, new IntentFilter(GlobalEntities.REGISTRATION_ID_UPDATED_EVENT));

        init();
    }

    private void init(){
        Log.d(GlobalEntities.HOME_ACTIVITY, "init: ");
        //progress Dialog
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setTitle("Please Wait...");

        main_view = findViewById(R.id.main_view);

        //floating action button
        fab = (FloatingActionButton) findViewById(R.id.home_fab);
        fab.setOnClickListener(this);

        //pager
        viewPager = (PagerBullet) findViewById(R.id.home_pagerBullet);

        fragmentAdapter = new PagerAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(MyBoardsFragment.newInstance());
        fragmentAdapter.addFragment(MemberBoardsFragment.newInstance());

        viewPager.setAdapter(fragmentAdapter);

        //init presenter
        mPresenter = new HomePresenter(this, DataManager.getInstance());
        mPresenter.attachView(this);

        //notification registration id init
        Intent service = new Intent(HomeActivity.this, EgyTronicaFirebaseMessagingService.class);
        startService(service);

        Boolean isRegIdSent = PreferencesHelper.getInstance().getBoolean(PreferencesHelper.Key.IS_REGISTRATION_ID_SENT, false);
        String reg_id = FirebaseInstanceId.getInstance().getToken();
        if(reg_id != null && !isRegIdSent){
            Log.d(GlobalEntities.HOME_ACTIVITY, "Refreshed token: " + reg_id);

            request = Request.getInstance();
            request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
            request.setRegistrationID(reg_id);

            mPresenter.sendRegistrationId(request);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_fab:
                Log.i(GlobalEntities.HOME_ACTIVITY, "onClick: floating point button");
                startActivity(CreateBoardActivity.getStartIntent(this));
                break;
        }
    }

    @Override
    public void onMyBoardsFragmentInteraction(String action, String message) {
        if(action.equals(GlobalEntities.LOGOUT_TAG)){
            logout();
        }
    }

    @Override
    public void onMemberBoardsFragmentInteraction(String action, String message) {
        if(action.equals(GlobalEntities.LOGOUT_TAG)){
            logout();
        }
    }

    private void logout(){
        Log.i(GlobalEntities.HOME_ACTIVITY, "onCreate: logout func");

        PreferencesHelper.getInstance().clear();

        Intent i = LandingPage.getStartIntent(this);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private BroadcastReceiver logoutBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(GlobalEntities.HOME_ACTIVITY, "onCreate: logout");

            PreferencesHelper.getInstance().clear();

            Intent i = LandingPage.getStartIntent(HomeActivity.this);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    } ;

    private BroadcastReceiver registrationIDUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String reg_id = intent.getStringExtra(GlobalEntities.REGISTRATION_ID_TAG);
            Log.d(GlobalEntities.HOME_ACTIVITY, "Refreshed token: " + reg_id);
            if(reg_id != null){
                request = Request.getInstance();
                request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
                request.setRegistrationID(reg_id);

                mPresenter.sendRegistrationId(request);
            }
        }
    };

    @Override
    public void sendRegIdReqError(String e) {
        Log.i(GlobalEntities.LOGIN_ACTIVITY, "loginReqError: "+e);
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void sendRegIdReqSuccess() {
        PreferencesHelper.getInstance().put(PreferencesHelper.Key.IS_REGISTRATION_ID_SENT, true);
    }
}
