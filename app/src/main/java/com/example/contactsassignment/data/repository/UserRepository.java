package com.example.contactsassignment.data.repository;

import android.content.Context;


import com.example.contactsassignment.data.loacl_db.AppDatabase;
import com.example.contactsassignment.data.loacl_db.UserDao;
import com.example.contactsassignment.data.models.User;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        userDao = db.userDao();
    }

    public void registerUser(User user){
        userDao.register(user);
    }
    public boolean isUserExists(String username) {
        return userDao.isUserExists(username);
    }
    public Integer loginUser(String username, String password) {
        return userDao.login(username, password);
    }
}
