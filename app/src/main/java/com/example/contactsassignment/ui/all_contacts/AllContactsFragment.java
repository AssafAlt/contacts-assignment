package com.example.contactsassignment.ui.all_contacts;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.contactsassignment.R;
import com.example.contactsassignment.data.loacl_db.PrefManager;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.data.repository.ContactRepository;
import com.example.contactsassignment.data.repository.UserRepository;
import com.example.contactsassignment.databinding.FragmentAddContactBinding;
import com.example.contactsassignment.databinding.FragmentAllContactsBinding;
import com.example.contactsassignment.databinding.FragmentLoginBinding;
import com.example.contactsassignment.ui.view_model.ContactsViewModel;

import java.util.ArrayList;


public class AllContactsFragment extends Fragment implements AllContactsAdapter.OnContactClickListener  {

    private ContactsViewModel contactsViewModel;
    private AllContactsAdapter allContactsAdapter;
    private FragmentAllContactsBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAllContactsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        RecyclerView recyclerView = binding.contactsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
         allContactsAdapter = new AllContactsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(allContactsAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                allContactsAdapter.filterContacts(newText); // Filter contacts when text changes
                return true;
            }
        });
        contactsViewModel.getAllContacts().observe(getViewLifecycleOwner(), contacts -> {
            allContactsAdapter.setContacts(contacts);
        });

        binding.addContactButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_allContactsFragment_to_addContactFragment);
        });

        return view;
    }
    public void onContactClick(Contact contact) {
        Bundle bundle = new Bundle();
        bundle.putString("contactName", contact.getName());
        bundle.putInt("contactId", contact.getId());
        bundle.putString("contactPhone", contact.getPhone());
        bundle.putString("contactEmail", contact.getEmail());
        bundle.putString("contactGender", contact.getGender());
contactsViewModel.setSelectedContactId(contact.getId());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_allContactsFragment_to_contactDetailsFragment, bundle);
    }
}
