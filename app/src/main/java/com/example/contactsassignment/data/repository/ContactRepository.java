package com.example.contactsassignment.data.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.contactsassignment.data.loacl_db.AppDatabase;
import com.example.contactsassignment.data.loacl_db.ContactDao;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.data.models.User;

import java.util.List;
import java.util.Optional;


public class ContactRepository {
    private  ContactDao contactDao;


    public ContactRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        this.contactDao = db.contactDao();

    }
    public void addContact(Contact contact){
        contactDao.add(contact);
    }

    public void updateContact(Contact contact){contactDao.updateContact(contact);}

    public void deleteContactById(int contactId){contactDao.deleteContactById(contactId);};
    public LiveData<List<Contact>> getAllContactsForUser(int userId) {
        return contactDao.getAllContactsForUser(userId);
    }
}
