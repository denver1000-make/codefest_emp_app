package com.denprog.codefestapp.bindingAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;
import android.widget.EditText;

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



}
