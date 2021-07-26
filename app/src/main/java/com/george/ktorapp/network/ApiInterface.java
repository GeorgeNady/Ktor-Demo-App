package com.george.ktorapp.network;

import com.george.Models.Person.AuthRequests.LoginRequest;
import com.george.ktorapp.model.Auth.RegisterRequest;
import com.george.ktorapp.model.Auth.AuthResponse;
import com.george.ktorapp.model.TestResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @GET("test")
    Observable<TestResponse> test();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////// { AUTH } ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @POST("auth/register")
    Observable<AuthResponse> register(@Body RegisterRequest registerRequest);

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @POST("auth/login")
    Observable<AuthResponse> login(@Body LoginRequest loginRequest);

//    @Headers({"Accept: application/json", "Content-Tnype:  application/json"})
//    @POST("auth/edit-setting")
//    Observable<SettingResponse>editSetting(@Body EditSetting editSetting,@Header("Authorization") String authorization);

//    @Headers({"Accept: application/json", "Content-Type:  application/json"})
//    @POST("social/challenge")
//    Observable<Msg>invite(@Body Invite invite,@Header("Authorization") String authorization);

//    @Headers({"Accept: application/json", "Content-Type:  application/json"})
//    @POST("social/hide-clash")
//    Observable<ForgetPassword>hideClash(@Body UnVote clash_id,@Header("Authorization") String authorization);

//    @Headers({"Accept: application/json", "Content-Type:  application/json"})
//    @POST("clashes/delete-clash")
//    Observable<ForgetPassword>deleteClash(@Body UnVote clash_id,@Header("Authorization") String authorization);


}
