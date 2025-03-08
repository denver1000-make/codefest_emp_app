package com.denprog.codefestapp.bindingAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class BindingAdapter {


    @androidx.databinding.BindingAdapter("android:text")
    public static void setFloat(EditText editText, float value) {
        if (editText.getText().toString().equals(String.valueOf(value))) {
            editText.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text", event = "textAttrChanged")
    public static float getFloat(EditText view) {
        try {
            return Float.parseFloat(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    @androidx.databinding.BindingAdapter("textAttrChanged")
    public static void setListener(EditText editText, final InverseBindingListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @androidx.databinding.BindingAdapter("selectedItem")
    public static void setSelectedItem(Spinner spinner, String newValue) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals("newValue")) {
                spinner.setSelection(i);
            }
        }
    }

    @InverseBindingAdapter(attribute = "selectedItem", event = "selectedItemAttrChange")
    public static String getSelectedItem(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }


    @androidx.databinding.BindingAdapter("selectedItemAttrChange")
    public static void setListener(Spinner spinner, final InverseBindingListener listener) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



}
