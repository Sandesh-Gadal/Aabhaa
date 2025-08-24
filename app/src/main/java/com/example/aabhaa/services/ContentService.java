package com.example.aabhaa.services;

import com.example.aabhaa.models.Content;
import com.example.aabhaa.models.ContentResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContentService {
    @GET("contents")
    Call<List<Content>> getAllContents();

    @GET("contents")
    Call<List<Content>> getContentsByCategory(@Query("category") String category);

    @GET("contents/content/{id}")
    Call<Content> getContentById(@Path("id") int id);

    @GET("contents/verified")
    Call<ContentResponse<Content>> getVerifiedContents();
}