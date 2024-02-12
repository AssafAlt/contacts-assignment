package com.example.contactsassignment.api;

import com.example.contactsassignment.data.models.GenderResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/")
    Call<GenderResponse> getGender(@Query("name") String name);
}
