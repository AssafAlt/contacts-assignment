package com.example.contactsassignment.data.loacl_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactsassignment.data.models.Contact;

import java.util.List;
import java.util.Optional;


@Dao
public interface ContactDao {
    @Insert
    void add(Contact contact);
    @Update
    void updateContact(Contact contact);

    @Query("DELETE FROM contacts WHERE id = :contactId")
    void deleteContactById(int contactId);
    @Query("SELECT * FROM contacts WHERE userId = :userId")
    LiveData<List<Contact>> getAllContactsForUser(int userId);
}
