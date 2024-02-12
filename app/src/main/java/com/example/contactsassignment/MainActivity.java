package com.example.contactsassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.contactsassignment.ui.view_models.AuthViewModel;



public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);


        ImageView logoutImageView = findViewById(R.id.logoutImageView);


        logoutImageView.setOnClickListener(v -> {
                authViewModel.logout();
        });
    }

}