package com.example.contactsassignment.ui;

import android.app.AlertDialog;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.contactsassignment.R;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.databinding.FragmentContactDetailsBinding;
import com.example.contactsassignment.ui.view_model.ContactsViewModel;

public class ContactDetailsFragment extends Fragment {

    private final String[] genders = {"Male", "Female"};
    private ContactsViewModel contactsViewModel;
    private FragmentContactDetailsBinding binding;
    private ArrayAdapter<String> genderAdapter;
    private boolean isEditMode = false;

   private Integer contactId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, genders);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactDetailsBinding.inflate(inflater, container, false);
        binding.autoCompleteTextView.setAdapter(genderAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String contactName = bundle.getString("contactName");
            contactId = bundle.getInt("contactId");
            binding.automaticGenderButton.setVisibility(View.INVISIBLE);
            binding.nameEditText.setText(contactName);
            binding.nameEditText.setClickable(false);
            binding.nameEditText.setEnabled(false);
            binding.phoneEditText.setClickable(false);
            binding.phoneEditText.setEnabled(false);
            binding.emailEditText.setClickable(false);
            binding.emailEditText.setEnabled(false);
            binding.autoCompleteTextView.setClickable(false);
            binding.autoCompleteTextView.setEnabled(false);

        } else {
            Log.d("Frag_Check", "Received bundle is null");
            // Handle null case (e.g., show a placeholder or error message)
        }

        binding.editButton.setOnClickListener(view -> {
            if (!isEditMode) {
                enableEditMode();
                binding.editButton.setText("Save");
            } else {
                performUpdate();
                disableEditMode();
                binding.editButton.setText("Edit");
            }
            isEditMode = !isEditMode;
        });

        binding.cancelButton.setOnClickListener(v -> {
            if (!isEditMode) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_contactDetailsFragment_to_allContactsFragment);

            } else {
                // If in edit mode, show a confirmation dialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Discard Changes")
                        .setMessage("Are you sure do you want to discard changes?")
                        .setPositiveButton("Discard", (dialog, which) -> {
                            // User confirmed to discard changes
                            disableEditMode();
                            binding.editButton.setText("Edit");
                        })
                        .setNegativeButton("Cancel", null) // Do nothing if user cancels
                        .show();
            }

        });

        binding.deleteImageView.setOnClickListener(v -> {

                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            performDelete(contactId);
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.action_contactDetailsFragment_to_allContactsFragment);
                        })
                        .setNegativeButton("Cancel", null) // Do nothing if user cancels
                        .show();


        });

        return binding.getRoot();
    }

    private void enableEditMode() {
        binding.titleTextView.setText("Edit Mode");
        binding.nameEditText.setClickable(true);
        binding.nameEditText.setEnabled(true);
        binding.phoneEditText.setClickable(true);
        binding.phoneEditText.setEnabled(true);
        binding.emailEditText.setClickable(true);
        binding.emailEditText.setEnabled(true);
        binding.autoCompleteTextView.setClickable(true);
        binding.autoCompleteTextView.setEnabled(true);
        binding.automaticGenderButton.setVisibility(View.VISIBLE);
        // Enable other fields if needed
    }

    private void disableEditMode() {
        binding.nameEditText.setClickable(false);
        binding.nameEditText.setEnabled(false);
        binding.phoneEditText.setClickable(false);
        binding.phoneEditText.setEnabled(false);
        binding.emailEditText.setClickable(false);
        binding.emailEditText.setEnabled(false);
        binding.autoCompleteTextView.setClickable(false);
        binding.autoCompleteTextView.setEnabled(false);
        binding.automaticGenderButton.setVisibility(View.INVISIBLE);
    }

    private void performUpdate() {
        Log.d("Details_Frag",contactId.toString());

        new Thread(() -> {
            int userId = contactsViewModel.getUserId();
            String contactName = binding.nameEditText.getText().toString();
            String phone = binding.phoneEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String gender = binding.autoCompleteTextView.getText().toString();
            Contact updatedContact = new Contact(userId, contactName, phone, email, gender);
            updatedContact.setId(contactId);

            contactsViewModel.updateContact(updatedContact);



        }).start();
    }
    private void performDelete(int idToDelete) {

        new Thread(() -> {
            contactsViewModel.deleteContactById(idToDelete);

        }).start();
    }
}

