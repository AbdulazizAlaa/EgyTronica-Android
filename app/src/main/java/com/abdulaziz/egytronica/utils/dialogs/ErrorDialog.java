package com.abdulaziz.egytronica.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by abdulaziz on 1/1/17.
 */

public class ErrorDialog extends Dialog {

    private String title;
    private String message;
    private String btnText;

    public ErrorDialog(Context context, String title, String message, String btnText) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.title = title;
        this.message = message;
        this.btnText = btnText;

        this.setTitle(this.title);
        this.setCancelable(true);
    }
}
