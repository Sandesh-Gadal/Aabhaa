package com.example.aabhaa.controllers;

import com.example.aabhaa.models.User;

public interface UserProfileCallback {
    void onUserDataFetched(User user);
}
