package com.abdulaziz.egytronica.utils.textInputComponent;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by abdulaziz on 10/31/16.
 */

public class CustomTextInputEditText extends TextInputEditText {

    private TextInputLayout TIL;

    private boolean valid;

    public CustomTextInputEditText(Context context) {
        super(context);
        valid = false;
    }

    public CustomTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setTIL(TextInputLayout TIL) {
        this.TIL = TIL;
    }

    @Override
    public void setError(CharSequence error) {
        this.TIL.setError(error);
    }
}
