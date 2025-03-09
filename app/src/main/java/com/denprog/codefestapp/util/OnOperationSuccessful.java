package com.denprog.codefestapp.util;

public interface OnOperationSuccessful <T>{

    void onSuccess(T data);

    void onError(String message);

}
