package com.abdulaziz.egytronica.utils.textInputComponent;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by abdulaziz on 10/31/16.
 */

public class CustomTextInputEditTextL extends CustomTextInputEditText{

    private CustomTextInputLayout TIL;

    private boolean valid;

    public CustomTextInputEditTextL(Context context) {
        super(context);
        valid = false;
    }

    public CustomTextInputEditTextL(Context context, AttributeSet attrs) {
        super(context, attrs);
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setTIL(CustomTextInputLayout TIL) {
        this.TIL = TIL;
    }

    public void setHelperText(CharSequence helperText){
        TIL.setHelperText(helperText);
    }

    @Override
    public void setError(CharSequence error) {
        this.TIL.setError(error);
    }
}
