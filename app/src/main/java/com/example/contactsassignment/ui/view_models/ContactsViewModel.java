package com.example.contactsassignment.ui.view_models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactsassignment.data.loacl_db.PrefManager;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.data.repository.ContactRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {
    private final ContactRepository contactRepository;
    private LiveData<List<Contact>> allContacts;
    private PrefManager prefManager;

    private int userId;

    // Initialize selectedContact with MutableLiveData
    private Contact selectedContact;
    private MutableLiveData<Integer> selectedContactId = new MutableLiveData<>();

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        prefManager = new PrefManager(application);
        userId = prefManager.getUserId();
        allContacts = contactRepository.getAllContactsForUser(userId);
    }

    public int getUserId(){
        return this.userId;
    }

    public void setSelectedContact(Contact selectedContact) {

        this.selectedContact= selectedContact;
    }

    public void setSelectedContactId(Integer contactId) {

        selectedContactId.setValue(contactId);
    }

    public LiveData<Integer> getSelectedContactId() {
        return selectedContactId;
    }

    // Method to get selected contact
    public Contact getSelectedContact() {
        return this.selectedContact;
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


