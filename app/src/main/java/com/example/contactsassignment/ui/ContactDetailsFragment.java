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

    private  Contact originalContact;
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
        binding.genderTextView.setAdapter(genderAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int userId = contactsViewModel.getUserId();
            String contactName = bundle.getString("contactName");
            contactId = bundle.getInt("contactId");
            String contactPhone = bundle.getString("contactPhone");
            String contactEmail = bundle.getString("contactEmail");
            String contactGender = bundle.getString("contactGender");
            if(contactName!=null){
                originalContact =  new Contact(userId, contactName, contactPhone, contactEmail, contactGender);

            }
            binding.automaticGenderButton.setVisibility(View.INVISIBLE);
            binding.nameEditText.setText(contactName);
            binding.phoneEditText.setText(contactPhone);
            binding.emailEditText.setText(contactEmail);
            binding.genderTextView.setText(contactGender);
            binding.nameEditText.setClickable(false);
            binding.nameEditText.setEnabled(false);
            binding.phoneEditText.setClickable(false);
            binding.phoneEditText.setEnabled(false);
            binding.emailEditText.setClickable(false);
            binding.emailEditText.setEnabled(false);
            binding.genderTextView.setClickable(false);
            binding.genderTextView.setEnabled(false);

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
                new AlertDialog.Builder(requireContext())
                        .setTitle("Discard Changes")
                        .setMessage("Are you sure do you want to discard changes?")
                        .setPositiveButton("Discard", (dialog, which) -> {
                            // User confirmed to discard changes
                            disableEditMode();
                            binding.editButton.setText("Edit");
                            binding.nameEditText.setText(originalContact.getName());
                            binding.phoneEditText.setText(originalContact.getPhone());
                            binding.emailEditText.setText(originalContact.getEmail());
                            binding.genderTextView.setText(originalContact.getGender());
                        })
                        .setNegativeButton("Cancel", null) // Do nothing if user cancels
                        .show();
            }
            isEditMode = !isEditMode;

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
        binding.genderTextView.setClickable(true);
        binding.genderTextView.setEnabled(true);
        binding.automaticGenderButton.setVisibility(View.VISIBLE);

    }

    private void disableEditMode() {
        binding.nameEditText.setClickable(false);
        binding.nameEditText.setEnabled(false);
        binding.phoneEditText.setClickable(false);
        binding.phoneEditText.setEnabled(false);
        binding.emailEditText.setClickable(false);
        binding.emailEditText.setEnabled(false);
        binding.genderTextView.setClickable(false);
        binding.genderTextView.setEnabled(false);
        binding.automaticGenderButton.setVisibility(View.INVISIBLE);

    }

    private void performUpdate() {


        new Thread(() -> {

            String contactName = binding.nameEditText.getText().toString();
            String phone = binding.phoneEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String gender = binding.genderTextView.getText().toString();
            if (!contactName.isEmpty()) {
                contactName = contactName.substring(0, 1).toUpperCase() + contactName.substring(1);
            } else {
                binding.nameTextInputLayout.setError("Name can't be empty!");
                return;
            }
            Contact updatedContact = new Contact(originalContact.getUserId(), contactName, phone, email, gender);
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

