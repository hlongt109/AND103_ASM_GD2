package com.ph30891.asm_ph30891_gd2.model;

public class District {
    private int DistrictID;
    private int ProvinceID;
    private String DistrictName;

    public District(int districtID, int provinceID, String districtName) {
        DistrictID = districtID;
        ProvinceID = provinceID;
        DistrictName = districtName;
    }

    public District() {
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public District setDistrictID(int districtID) {
        DistrictID = districtID;
        return this;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public District setProvinceID(int provinceID) {
        ProvinceID = provinceID;
        return this;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public District setDistrictName(String districtName) {
        DistrictName = districtName;
        return this;
    }
}
