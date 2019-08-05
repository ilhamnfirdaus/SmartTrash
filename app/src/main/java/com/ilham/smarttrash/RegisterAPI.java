package com.ilham.smarttrash;

import com.ilham.smarttrash.modelKontrol.ResponseKontrol;
import com.ilham.smarttrash.modelKontrol.ResultItem;
import com.ilham.smarttrash.modelSensortes.Value;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegisterAPI {

    @GET("view.php")
    Call<Value> view();

    @GET("viewKontrol.php")
    Call<ResponseKontrol> viewKontrol();

    @FormUrlEncoded
    @POST("kontrol.php")
    Call<Value> ubah(
            @Field("id") String id,
            @Field("nilai") String nilai,
            @Field("waktu_berakhir") String waktu_berakhir);
}
