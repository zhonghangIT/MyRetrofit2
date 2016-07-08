package com.education.myretrofit;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhonghang on 16/7/7.
 */

public interface RetrofitServiceTest {
    //    http://apicloud.mob.com/v1/weather/query?province=江苏&key=14b49edf0cfb1&city=南京
    @GET("v1/weather/query?province=江苏&key=14b49edf0cfb1&city=南京")
    Call<ResponseBody> getWeatherString();

    @GET("{path}?")
    Call<ResponseBody> getWeatherParams(@Path("path") String path, @QueryMap() Map<String, String> map);

    @GET("v1/weather/query?province=江苏&key=14b49edf0cfb1&city=南京")
    Call<Weather> getWeatherJson();

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);

    @Multipart
    @POST
    Call<ResponseBody> upload(@Part() RequestBody body);

    //    RequestBody body = new MultipartBody.Builder().addFormDataPart("username", "测试").addFormDataPart("app", "app.png", MultipartBody.create(MultipartBody.FORM, file)).build();
    @POST
    Call<RequestBody> upload(@Body() MultipartBody body);
}
