package com.example.contactsassignment.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.contactsassignment.MainActivity;
import com.example.contactsassignment.R;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.databinding.FragmentAddContactBinding;
import com.example.contactsassignment.helpers.ApiHelper;
import com.example.contactsassignment.ui.adapters.GenderAdapter;
import com.example.contactsassignment.ui.view_models.ContactsViewModel;


import java.util.Objects;




public class AddContactFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private GenderAdapter genderAdapter;
    private FragmentAddContactBinding binding;
    private ImageView logoutIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        genderAdapter = new GenderAdapter(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddContactBinding.inflate(inflater, container, false);
        MainActivity mainActivity = (MainActivity) requireActivity();
        logoutIcon = mainActivity.findViewById(R.id.logoutImageView);
        logoutIcon.setVisibility(View.VISIBLE);

        logoutIcon.setOnClickListener(v->{
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_addContactFragment_to_loginFragment);
            logoutIcon.setVisibility(View.INVISIBLE);
        });


        binding.genderTextView.setAdapter(genderAdapter);

        binding.cancelButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_addContactFragment_to_allContactsFragment);
        });

        binding.automaticGenderButton.setOnClickListener(v -> {

            String fullName = Objects.requireNonNull(binding.nameEditText.getText()).toString().trim();
            if (!fullName.isEmpty()) {
                String[] nameParts = fullName.split("\\s+");
                    String firstName = nameParts[0];
                ApiHelper.getGenderFromApi(firstName, requireContext(), (gender, probability) -> {
                    binding.genderTextView.setText(gender);
                    binding.genderTextInputLayout.setHelperText("Gender Probability: " + probability);
                });

            } else {
                binding.nameTextInputLayout.setError("Name can't be empty!");
            }
        });

        binding.saveButton.setOnClickListener(v -> {

            String name = binding.nameEditText.getText().toString().trim();
            String phone = binding.phoneEditText.getText().toString().trim();
            String email = binding.emailEditText.getText().toString().trim();
            String gender = binding.genderTextView.getText().toString().trim();


            if (!name.isEmpty()) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            } else {
                binding.nameTextInputLayout.setError("Name can't be empty!");
                return;
            }

            int userId = contactsViewModel.getUserId();


            Contact contact = new Contact(userId, name, phone, email, gender);


            performAddContact(contact);
        });

        return binding.getRoot();
    }

    private void performAddContact(Contact contact) {

        new Thread(() -> {
            contactsViewModel.addContact(contact);

            requireActivity().runOnUiThread(() -> {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_addContactFragment_to_allContactsFragment);
            });
        }).start();
    }


}