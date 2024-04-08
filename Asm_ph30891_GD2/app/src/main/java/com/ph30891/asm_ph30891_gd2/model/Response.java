package com.ph30891.asm_ph30891_gd2.model;

public class Response<T> {
    private int status;
    private String message;
    private T data;
    private String token;
    private String refreshToken;

    public Response(int status, String message, T data, String token, String refreshToken) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public Response<T> setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Response<T> setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Response() {
    }

    public int getStatus() {
        return status;
    }

    public Response<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }
}
