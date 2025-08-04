package com.example.aabhaa.controllers;

import com.example.aabhaa.models.Address;

import java.util.List;

public interface AddressFetchCallback {
    void onAddressesFetched(List<Address> addressList);
}

