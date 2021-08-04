package com.george.ktorapp.network;

import com.george.Models.Person.AuthRequests.LoginRequest;
import com.george.ktorapp.model.Auth.RegisterRequest;
import com.george.ktorapp.model.Auth.AuthResponse;
import com.george.ktorapp.model.posts.CreatePostRequest;
import com.george.ktorapp.model.posts.InsDelPostResponse;
import com.george.ktorapp.model.posts.GetPostsResponse;
import com.george.ktorapp.model.posts.react.ReactRequest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////// { AUTH } ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @POST("auth/register")
    Observable<AuthResponse> register(@Body RegisterRequest registerRequest);

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @POST("auth/login")
    Observable<AuthResponse> login(@Body LoginRequest loginRequest);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////// { POST } ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @POST("social/posts/create-post")
    Observable<InsDelPostResponse> createPost(
            @Body CreatePostRequest content,
            @Header("Authorization") String token
    );

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @GET("social/posts")
    Observable<GetPostsResponse> getPosts(
            @Query("page") int page,
            @Header("Authorization") String token
    );

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @GET("social/posts/my-posts")
    Observable<GetPostsResponse> getMyPosts(
            @Query("page") int page,
            @Header("Authorization") String token
    );

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @DELETE("social/posts/delete-post/{post_id}")
    Observable<InsDelPostResponse> deletePost(
            @Path("post_id") String postId,
            @Header("Authorization") String token
    );

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @PATCH("social/posts/edit-post/{post_id}")
    Observable<InsDelPostResponse> editPost(
            @Path("post_id") String postId,
            @Body CreatePostRequest updatesPostRequest,
            @Header("Authorization") String token
    );

    @Headers({"Accept: application/json", "Content-Type:  application/json"})
    @PATCH("social/posts/react-post/{post_id}")
    Observable<InsDelPostResponse> react(
            @Path("post_id") String postId,
            @Body ReactRequest updatesPostRequest,
            @Header("Authorization") String token
    );

}
