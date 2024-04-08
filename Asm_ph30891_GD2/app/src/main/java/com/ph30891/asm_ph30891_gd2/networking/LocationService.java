package com.ph30891.asm_ph30891_gd2.networking;

import com.ph30891.asm_ph30891_gd2.model.District;
import com.ph30891.asm_ph30891_gd2.model.DistrictRequest;
import com.ph30891.asm_ph30891_gd2.model.Province;
import com.ph30891.asm_ph30891_gd2.model.ResponseGHN;
import com.ph30891.asm_ph30891_gd2.model.Ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LocationService {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";


    @GET("shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();

    @POST("shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);

    @GET("shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
}
