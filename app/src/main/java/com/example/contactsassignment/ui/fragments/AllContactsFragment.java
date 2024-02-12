package com.example.contactsassignment.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contactsassignment.R;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.databinding.FragmentAllContactsBinding;
import com.example.contactsassignment.ui.adapters.AllContactsAdapter;
import com.example.contactsassignment.ui.view_models.ContactsViewModel;

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
                allContactsAdapter.filterContacts(newText);
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

        return binding.getRoot();
    }
    public void onContactClick(Contact contact) {

        Bundle bundle = new Bundle();
        bundle.putString("contactName", contact.getName());
        bundle.putInt("contactId", contact.getId());
        bundle.putString("contactPhone", contact.getPhone());
        bundle.putString("contactEmail", contact.getEmail());
        bundle.putString("contactGender", contact.getGender());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_allContactsFragment_to_contactDetailsFragment, bundle);
    }
}
