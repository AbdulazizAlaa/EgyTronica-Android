package com.abdulaziz.egytronica.utils.validators;

import android.util.Log;
import android.widget.EditText;

import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by abdulaziz on 10/31/16.
 */

public class EducationalEmailValidator implements Validator<EditText> {
    @Override
    public Observable<RxValidationResult<EditText>> validate(String s, EditText editText) {
        RxValidationResult<EditText> result;

        Log.i("TAGGG", "validate: "+s+":::"+s.endsWith("edu.eg"));

        if(s.endsWith("edu.eg")){
            result = RxValidationResult.createSuccess(editText);
        }else {
            result = RxValidationResult.createImproper(editText, "Wrong Email Format. Must end in edu.eg");
        }

        return Observable.just(result).delay(2, TimeUnit.SECONDS);
    }
}
