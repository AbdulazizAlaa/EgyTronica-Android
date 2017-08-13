package com.abdulaziz.egytronica.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.mvp.DefaultObserver;
import com.abdulaziz.egytronica.utils.textInputComponent.CustomTextInputEditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.RxValidator;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;

/**
 * Created by abdulaziz on 3/4/17.
 */

public class AddMembersDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private TextInputLayout emailTil;
    private CustomTextInputEditText emailED;

    private Spinner professionSpinner;

    private Button confirmBtn;

    private String email;
    private int type;

    private MembersDialogInteractionInterface mListener;

    public AddMembersDialog(Context context, int type) {
        super(context);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.members_dialog_layout);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        this.getWindow().setLayout((9 * width)/10, LinearLayout.LayoutParams.WRAP_CONTENT);

        this.type = type;

        ArrayList<CharSequence> professions = new ArrayList<CharSequence>();
        professions.add("Engineer");
        professions.add("Technician");

        professionSpinner = (Spinner) findViewById(R.id.members_dialog_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, professions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(adapter);

        professionSpinner.setOnItemSelectedListener(this);

        professionSpinner.setSelection(type);

        emailTil = (TextInputLayout) findViewById(R.id.members_dialog_email_til);
        emailED = (CustomTextInputEditText) findViewById(R.id.members_dialog_email_ed);
        emailED.setTIL(emailTil);

        confirmBtn = (Button) findViewById(R.id.members_dialog_btn);
        confirmBtn.setOnClickListener(this);

        Observer<RxValidationResult<EditText>> observer = new DefaultObserver(getContext(), GlobalEntities.ADD_MEMBERS_DIALOG);

        Observable<RxValidationResult<EditText>> emailValidator =
                RxValidator.createFor(emailED)
                        .nonEmpty()
                        .email()
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        emailValidator.subscribe(observer);


    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        professionSpinner.setSelection(type);
    }

    public void setListener(MembersDialogInteractionInterface listener){
        mListener = listener;
    }

    public MembersDialogInteractionInterface getListener(){
        return mListener;
    }

    public interface MembersDialogInteractionInterface{
        void onResult(String email, int type);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.members_dialog_btn:
                emailED.clearFocus();
                emailED.requestFocus();
                if(emailED.isValid()){
                    email = emailED.getText().toString();

                    mListener.onResult(email, type);
                    this.dismiss();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setCancelable(true)
                            .setMessage("First Fix Errors and Fill Form!!")
                            .setTitle("Error")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create();

                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    alertDialog.show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(GlobalEntities.ADD_MEMBERS_DIALOG, "onItemSelected: "+i);
        type = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}