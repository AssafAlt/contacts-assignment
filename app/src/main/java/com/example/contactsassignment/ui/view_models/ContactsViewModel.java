package com.example.contactsassignment.ui.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactsassignment.PrefManager;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.data.repository.ContactRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {
    private final ContactRepository contactRepository;
    private final LiveData<List<Contact>> allContacts;
    private final PrefManager prefManager;




    public ContactsViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        prefManager = new PrefManager(application);
        allContacts = contactRepository.getAllContactsForUser(prefManager.getUserId());
    }

    public int getUserId(){
        return prefManager.getUserId();
    }


    public void addContact(Contact contact){
        contactRepository.addContact(contact);
    }
    public void deleteContactById(int contactId){contactRepository.deleteContactById(contactId);};
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public void updateContact(Contact contact){contactRepository.updateContact(contact);}
}


