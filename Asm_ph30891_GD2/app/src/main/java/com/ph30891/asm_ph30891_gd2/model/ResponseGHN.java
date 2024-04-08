package com.ph30891.asm_ph30891_gd2.model;

public class ResponseGHN<T> {
    private int code;
    private String message;
    private T data;

    public ResponseGHN(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseGHN() {
    }

    public int getCode() {
        return code;
    }

    public ResponseGHN<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseGHN<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseGHN<T> setData(T data) {
        this.data = data;
        return this;
    }
}
