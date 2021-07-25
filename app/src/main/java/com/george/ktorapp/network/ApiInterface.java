package com.george.ktorapp.network;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @POST("auth/login")
    Observable<Object> login(@Body String id,@Header("Authorization") String authorization);

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
