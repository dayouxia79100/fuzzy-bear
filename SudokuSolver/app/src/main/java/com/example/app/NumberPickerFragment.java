package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by dayouxia on 1/15/14.
 */
public class NumberPickerFragment extends DialogFragment {
    public static final String EXTRA_XCOOR = ".xcoordinate";
    public static final String EXTRA_YCOOR = ".ycoordinate";
    public static final String EXTRA_NUM = ".number";

    private int mNum;

    public static NumberPickerFragment newInstance(){

        NumberPickerFragment fragment = new NumberPickerFragment();


        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null) return;

        Intent i = new Intent();
        i.putExtra(EXTRA_NUM,mNum);


        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode,i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.number_picker_fragment2,null);
        NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        //numberPicker.setMaxValue(9);
        numberPicker.setMinValue(0);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                numberPicker.setValue(i2);
                mNum = i2;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("NumberPicker")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }
}
