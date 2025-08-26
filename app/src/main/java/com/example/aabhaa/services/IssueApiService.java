package com.example.aabhaa.services;

import com.example.aabhaa.models.Issue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IssueApiService {

    @Headers("Accept: application/json")
    @POST("issues")
    Call<Issue> sendIssue(@Body Issue issue);
}
