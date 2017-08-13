package com.abdulaziz.egytronica.utils.mvp;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.abdulaziz.egytronica.utils.textInputComponent.CustomTextInputEditText;
import com.github.phajduk.rxvalidator.RxValidationResult;

import rx.Observer;

/**
 * Created by abdulaziz on 1/13/17.
 */

public class DefaultObserver implements Observer<RxValidationResult<EditText>> {

    private String TAG;
    private Context context;

    public DefaultObserver(Context context, String TAG) {
        this.TAG = TAG;
        this.context = context;
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.i(TAG, "onError: "+e.getMessage());
    }

    @Override
    public void onNext(RxValidationResult<EditText> editTextRxValidationResult) {
        Log.i(TAG, "onNext: "+editTextRxValidationResult.getMessage());
        Log.i(TAG, "onNext: "+editTextRxValidationResult.isProper());

        CustomTextInputEditText ed = (CustomTextInputEditText) editTextRxValidationResult.getItem();

        if(!editTextRxValidationResult.isProper()){
            ed.setError(editTextRxValidationResult.getMessage());
            ed.setValid(false);
        }else {
            ed.setError("");
//            if(ed instanceof CustomTextInputEditTextL){
//                CustomTextInputEditTextL edL = (CustomTextInputEditTextL) ed;
//                edL.setHelperText(Html.fromHtml(context.getString(R.string.register_university_email_desc)));
//            }
            ed.setValid(true);
        }
    }
}
