package com.abdulaziz.egytronica.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by abdulaziz on 3/10/17.
 */

public class AddComponentsDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private TextInputLayout nameTil;
    private CustomTextInputEditText nameED;

    private TextInputLayout nodesTil;
    private CustomTextInputEditText nodesED;

    private Spinner typesSpinner;

    private Button confirmBtn;

    private String name;
    private String nodes;
    private int type;

    private ComponentsDialogInteractionInterface mListener;

    public AddComponentsDialog(Context context, int type) {
        super(context);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.components_dialog_layout);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        this.getWindow().setLayout((9 * width)/10, LinearLayout.LayoutParams.WRAP_CONTENT);

        this.type = type;

        ArrayList<CharSequence> types = new ArrayList<CharSequence>();
        types.add("Resistor");
        types.add("Inductor");
        types.add("Capacitor");
        types.add("Diode");
        types.add("Transistor");
        types.add("Chip");

        typesSpinner = (Spinner) findViewById(R.id.components_dialog_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typesSpinner.setAdapter(adapter);

        typesSpinner.setOnItemSelectedListener(this);

        typesSpinner.setSelection(type);

        nodesTil = (TextInputLayout) findViewById(R.id.components_dialog_nodes_til);
        nodesED = (CustomTextInputEditText) findViewById(R.id.components_dialog_nodes_ed);
        nodesED.setTIL(nodesTil);

        nameTil = (TextInputLayout) findViewById(R.id.components_dialog_name_til);
        nameED = (CustomTextInputEditText) findViewById(R.id.components_dialog_name_ed);
        nameED.setTIL(nameTil);

        confirmBtn = (Button) findViewById(R.id.components_dialog_btn);
        confirmBtn.setOnClickListener(this);

        Observer<RxValidationResult<EditText>> observer = new DefaultObserver(getContext(), GlobalEntities.ADD_COMPONENTS_DIALOG);

        Observable<RxValidationResult<EditText>> nameValidator =
                RxValidator.createFor(nameED)
                        .nonEmpty()
                        .minLength(2, "Must at least be 2 characters.")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        nameValidator.subscribe(observer);

        Observable<RxValidationResult<EditText>> nodesValidator =
                RxValidator.createFor(nodesED)
                        .nonEmpty()
                        .minLength(2, "Must at least be 2 characters.")
                        .onFocusChanged()
                        .onValueChanged()
                        .toObservable();

        nodesValidator.subscribe(observer);


    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        typesSpinner.setSelection(type);
    }

    public void setListener(ComponentsDialogInteractionInterface listener){
        mListener = listener;
    }

    public ComponentsDialogInteractionInterface getListener(){
        return mListener;
    }

    public interface ComponentsDialogInteractionInterface{
        void onResult(String name, String nodes, int type);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.components_dialog_btn:
                nameED.clearFocus();
                nameED.requestFocus();
                nodesED.clearFocus();
                nodesED.requestFocus();
                if(nameED.isValid() && nodesED.isValid()){
                    name = nameED.getText().toString();
                    nodes = nodesED.getText().toString();

                    mListener.onResult(name, nodes, type);
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
        Log.i(GlobalEntities.ADD_COMPONENTS_DIALOG, "onItemSelected: "+i);
        type = i+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}