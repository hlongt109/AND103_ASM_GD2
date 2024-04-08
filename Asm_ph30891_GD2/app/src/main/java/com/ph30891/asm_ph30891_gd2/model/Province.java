package com.ph30891.asm_ph30891_gd2.model;

public class Province {
    private int ProvinceID;
    private String ProvinceName;

    public Province(int provinceID, String provinceName) {
        ProvinceID = provinceID;
        ProvinceName = provinceName;
    }

    public Province() {
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public Province setProvinceID(int provinceID) {
        ProvinceID = provinceID;
        return this;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public Province setProvinceName(String provinceName) {
        ProvinceName = provinceName;
        return this;
    }
}
