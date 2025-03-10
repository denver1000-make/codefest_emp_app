package com.denprog.codefestapp.bindingAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.google.android.material.textfield.TextInputEditText;

public class BindingAdapter {


    @androidx.databinding.BindingAdapter("android:text")
    public static void setFloat(TextInputEditText editText, float value) {
        if (!editText.toString().equals(String.valueOf(value))) {
            editText.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text", event = "textAttrChanged")
    public static float getFloat(TextInputEditText view) {
        try {
            return Float.parseFloat(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    @androidx.databinding.BindingAdapter("textAttrChanged")
    public static void setListener(TextInputEditText editText, final InverseBindingListener listener) {
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

    @androidx.databinding.BindingAdapter("app:selectedItem")
    public static void setItem(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
            }
        }
    }


    @androidx.databinding.BindingAdapter("onSelectedItemListener")
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

    @InverseBindingAdapter(attribute = "selectedItem", event = "onSelectedItemListener")
    public static String getItem(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }


}
