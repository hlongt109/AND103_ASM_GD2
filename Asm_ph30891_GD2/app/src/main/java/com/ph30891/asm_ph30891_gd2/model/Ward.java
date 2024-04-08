package com.ph30891.asm_ph30891_gd2.model;

public class Ward {
    private String WardCode;
    private int DistrictID;
    private String WardName;

    public Ward() {
    }

    public Ward(String wardCode, int districtID, String wardName) {
        WardCode = wardCode;
        DistrictID = districtID;
        WardName = wardName;
    }

    public String getWardCode() {
        return WardCode;
    }

    public Ward setWardCode(String wardCode) {
        WardCode = wardCode;
        return this;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public Ward setDistrictID(int districtID) {
        DistrictID = districtID;
        return this;
    }

    public String getWardName() {
        return WardName;
    }

    public Ward setWardName(String wardName) {
        WardName = wardName;
        return this;
    }
}
