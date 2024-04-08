package com.ph30891.asm_ph30891_gd2.model;

public class Page<T> {
    private T data;
    private int currentPage, totalPage;

    public Page(T data, int currentPage, int totalPage) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public Page() {
    }

    public T getData() {
        return data;
    }

    public Page<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Page<T> setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public Page<T> setTotalPage(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }
}
