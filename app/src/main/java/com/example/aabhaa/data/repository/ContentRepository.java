package com.example.aabhaa.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aabhaa.data.ContentDao;
import com.example.aabhaa.data.ContentDatabase;
import com.example.aabhaa.models.Content;
import com.example.aabhaa.models.ContentResponse;
import com.example.aabhaa.services.ContentService;
import com.example.aabhaa.services.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentRepository {
    private ContentService contentService;
    private MutableLiveData<List<Content>> contentsLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    private MutableLiveData<String> errorLiveData;

    private Context context;

    private ContentDao contentDao;


    public ContentRepository(Context context) {
        this.context = context;
        contentService = RetrofitClient.getClient(context).create(ContentService.class);
        contentDao = ContentDatabase.getInstance(context).contentDao();
        contentsLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Content>> getContentsLiveData() {
        return contentsLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchAllContents() {
        loadingLiveData.setValue(true);

        Call<List<Content>> call = contentService.getAllContents();
        call.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    contentsLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to load contents");
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void fetchContentsByCategory(String category) {
        loadingLiveData.setValue(true);

        Call<List<Content>> call = contentService.getContentsByCategory(category);
        call.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    contentsLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to load contents by category");
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void fetchVerifiedContents() {
        loadingLiveData.setValue(true);

        Call<ContentResponse<Content>> call = contentService.getVerifiedContents();
        call.enqueue(new Callback<ContentResponse<Content>>() {
            @Override
            public void onResponse(Call<ContentResponse<Content>> call, Response<ContentResponse<Content>> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    contentsLiveData.setValue(response.body().data); // ✅ only the list
                } else {
                    errorLiveData.setValue("Failed to load verified contents");
                }
            }

            @Override
            public void onFailure(Call<ContentResponse<Content>> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }


}