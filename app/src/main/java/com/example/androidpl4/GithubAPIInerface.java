package com.example.androidpl4;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubAPIInerface {

    @GET("/users/{username}/repos")
    Call<List<GitReqestModel>> getUser(@Path("username") String username);
}
