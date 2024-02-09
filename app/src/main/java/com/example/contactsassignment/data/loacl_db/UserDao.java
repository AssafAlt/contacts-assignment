package com.example.contactsassignment.data.loacl_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contactsassignment.data.models.User;

@Dao
public interface UserDao {
    @Insert
    void register(User user);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    boolean isUserExists(String username);

    @Query("SELECT id FROM users WHERE username = :username AND password = :password")
    Integer login(String username, String password);
}
