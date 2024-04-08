package com.ph30891.asm_ph30891_gd2.model;

public class DistrictRequest {
    private int province_id;

    public DistrictRequest(int province_id) {
        this.province_id = province_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public DistrictRequest setProvince_id(int province_id) {
        this.province_id = province_id;
        return this;
    }
}
