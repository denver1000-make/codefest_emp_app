package com.denprog.codefestapp.util;

public class UIState<T> {

    public static final class Loading<T> extends UIState<T> {

    }

    public static final class Success<T> extends UIState<T> {
        public T data;

        public Success(T data) {
            this.data = data;
        }
    }

    public static final class Fail<T> extends UIState<T> {
        public String message;

        public Fail(String message) {
            this.message = message;
        }
    }

}
