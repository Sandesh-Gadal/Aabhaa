package com.example.aabhaa.controllers;

public interface RepositoryCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}
