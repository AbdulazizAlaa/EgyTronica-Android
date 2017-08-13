package com.abdulaziz.egytronica.ui.board.create;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.ui.board.view.ViewBoardActivity;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.ui.mobile_pin.MobilePinActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.Utils;
import com.abdulaziz.egytronica.utils.mvp.DefaultObserver;
import com.abdulaziz.egytronica.utils.textInputComponent.CustomTextInputEditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.RxValidator;

import rx.Observable;
import rx.Observer;

public class CreateBoardActivity extends AppCompatActivity implements CreateBoardBaseView, View.OnClickListener{

    private TextInputLayout nameTIL;
    private CustomTextInputEditText nameED;

    private TextInputLayout codeTIL;
    private CustomTextInputEditText codeED;

    private TextInputLayout refreshTIL;
    private CustomTextInputEditText refreshED;

    private TextInputLayout maintenanceTIL;
    private CustomTextInputEditText maintenanceED;

    private Button confirmBtn;

    private CreateBoardPresenter mPresenter;
    private Request request;

    private View main_view;
    private ProgressDialog progress;

    public static Intent getStartIntent(Context mContext){
        Intent i = new Intent(mContext, CreateBoardActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        init();
    }

    private void init(){

        //progress Dialog
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setTitle("Please Wait..");

        //main view used for snackbar
        main_view = findViewById(R.id.main_view);

        //init confirm button
        confirmBtn = (Button) findViewById(R.id.create_board_activity_confirm_btn);
        confirmBtn.setOnClickListener(this);

        //init edit texts
        nameED = (CustomTextInputEditText) findViewById(R.id.create_board_activity_name_ed);
        nameTIL = (TextInputLayout) findViewById(R.id.create_board_activity_name_til);
        nameED.setTIL(nameTIL);

        codeED = (CustomTextInputEditText) findViewById(R.id.create_board_activity_code_ed);
        codeTIL = (TextInputLayout) findViewById(R.id.create_board_activity_code_til);
        codeED.setTIL(codeTIL);

        refreshED = (CustomTextInputEditText) findViewById(R.id.create_board_activity_refresh_time_ed);
        refreshTIL = (TextInputLayout) findViewById(R.id.create_board_activity_refresh_time_til);
        refreshED.setTIL(refreshTIL);

        maintenanceED = (CustomTextInputEditText) findViewById(R.id.create_board_activity_last_maintenance_ed);
        maintenanceTIL = (TextInputLayout) findViewById(R.id.create_board_activity_last_maintenance_til);
        maintenanceED.setTIL(maintenanceTIL);

        Observer<RxValidationResult<EditText>> observer = new DefaultObserver(this, GlobalEntities.CREATE_BOARD_ACTIVITY);

        Observable<RxValidationResult<EditText>> nameValidator =
                RxValidator.createFor(nameED)
                        .nonEmpty()
                        .minLength(3, "At least 3 characters")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        nameValidator.subscribe(observer);

        Observable<RxValidationResult<EditText>> codeValidator =
                RxValidator.createFor(codeED)
                        .nonEmpty()
                        .minLength(3, "At least 3 characters")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        codeValidator.subscribe(observer);

        Observable<RxValidationResult<EditText>> refreshValidator =
                RxValidator.createFor(refreshED)
                        .nonEmpty()
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        refreshValidator.subscribe(observer);

        Observable<RxValidationResult<EditText>> maintenanceValidator =
                RxValidator.createFor(maintenanceED)
                        .nonEmpty()
                        .minLength(3, "At least 3 characters")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        maintenanceValidator.subscribe(observer);

        //init presenter
        mPresenter = new CreateBoardPresenter(this, DataManager.getInstance());
        mPresenter.attachView(this);
    }

    @Override
    public void createBoardReqSuccess(Board board) {
        Log.i(GlobalEntities.CREATE_BOARD_ACTIVITY, "createBoardReqSuccess: ");
    }

    @Override
    public void createBoardReqError(String e) {
        Log.i(GlobalEntities.CREATE_BOARD_ACTIVITY, "createBoardReqError: "+e);
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
        handleProgress("");

        //if error message contains any data about invalid token
        Utils.logout(this, e);
    }

    @Override
    public void createBoardReqComplete() {
        Log.i(GlobalEntities.CREATE_BOARD_ACTIVITY, "createBoardReqComplete: ");

        handleProgress("");

        Intent i = HomeActivity.getStartIntent(this);
        startActivity(i);
        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_board_activity_confirm_btn:
                Log.i(GlobalEntities.CREATE_BOARD_ACTIVITY, "onClick: confirm");
                if(nameED.isValid() && codeED.isValid() && refreshED.isValid() && maintenanceED.isValid()){

                    handleProgress("Creating....");

                    //making create board request
                    Board tempBoard = new Board();
                    tempBoard.setName(nameED.getText().toString());
                    tempBoard.setCode(codeED.getText().toString());
                    tempBoard.setRefreshTime(refreshED.getText().toString());
                    tempBoard.setLastMaintainance(maintenanceED.getText().toString());

                    request = Request.getInstance();
                    request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
                    request.setBoards(tempBoard);

                    mPresenter.createBoard(request);
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
}
