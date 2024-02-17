package com.example.contactsassignment.helpers;

import android.content.Context;
import android.widget.Toast;

import com.example.contactsassignment.api.ApiClient;
import com.example.contactsassignment.api.ApiService;
import com.example.contactsassignment.data.models.GenderResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHelper {

    public static void getGenderFromApi(String firstName, Context context, GenderCallback callback) {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GenderResponse> call = service.getGender(firstName);

        call.enqueue(new Callback<GenderResponse>() {
            public void onResponse(Call<GenderResponse> call, Response<GenderResponse> response) {
                if (response.isSuccessful()) {
                    GenderResponse genderResponse = response.body();
                    if (genderResponse != null) {
                        String gender = genderResponse.getGender();
                        String capitalizedGender = gender.substring(0, 1).toUpperCase() + gender.substring(1);
                        double probability = genderResponse.getProbability();
                        callback.onSuccess(capitalizedGender, probability);
                    }
                } else {

                    Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<GenderResponse> call, Throwable t) {

                Toast.makeText(context, "Failed to make API call: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface GenderCallback {
        void onSuccess(String gender, double probability);
    }
}

