package com.example.contactsassignment.data.repository;

import android.app.Application;


import androidx.lifecycle.LiveData;

import com.example.contactsassignment.data.loacl_db.AppDatabase;
import com.example.contactsassignment.data.loacl_db.ContactDao;
import com.example.contactsassignment.data.models.Contact;


import java.util.List;



public class ContactRepository {
    private final ContactDao contactDao;


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
