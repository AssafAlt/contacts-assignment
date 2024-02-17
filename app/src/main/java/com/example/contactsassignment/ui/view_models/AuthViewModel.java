package com.example.contactsassignment.ui.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.contactsassignment.PrefManager;
import com.example.contactsassignment.data.models.User;
import com.example.contactsassignment.data.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final PrefManager prefManager;




    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        prefManager = new PrefManager(application);

    }

    public boolean isUserExists(String username){
        return userRepository.isUserExists(username);
    }
    public void registerUser(User user){
        userRepository.registerUser(user);
    }


    public int loginUser(String username, String password) {
        Integer userId = userRepository.loginUser(username, password);
        if (userId != null && userId > 0) {
            prefManager.saveUserId(userId);

            return userId;
        } else {

            return 0;
        }
    }
    public void logout() {
        prefManager.clearUserId();

    }



}

