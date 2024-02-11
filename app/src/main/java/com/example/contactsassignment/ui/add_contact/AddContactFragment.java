package com.example.contactsassignment.ui.add_contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.contactsassignment.R;
import com.example.contactsassignment.api.ApiClient;
import com.example.contactsassignment.api.ApiService;
import com.example.contactsassignment.data.loacl_db.PrefManager;
import com.example.contactsassignment.data.models.Contact;
import com.example.contactsassignment.data.models.GenderResponse;
import com.example.contactsassignment.data.repository.ContactRepository;
import com.example.contactsassignment.data.repository.UserRepository;
import com.example.contactsassignment.databinding.FragmentAddContactBinding;
import com.example.contactsassignment.ui.view_model.ContactsViewModel;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddContactFragment extends Fragment {

    private final String[] genders = {"Male", "Female"};

    private ContactsViewModel contactsViewModel;
    private ArrayAdapter<String> genderAdapter;
    private FragmentAddContactBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, genders);
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddContactBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set the ArrayAdapter to the AutoCompleteTextView
        binding.autoCompleteTextView.setAdapter(genderAdapter);

        binding.cancelButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_addContactFragment_to_allContactsFragment);
        });

        binding.automaticGenderButton.setOnClickListener(v -> {

            String fullName = Objects.requireNonNull(binding.nameEditText.getText()).toString().trim();
            if (!fullName.isEmpty()) {
                String[] nameParts = fullName.split("\\s+");

                    String firstName = nameParts[0];
                    makeApiCall(firstName);

            } else {
                binding.nameTextInputLayout.setError("Name can't be empty!");
            }
        });

        binding.saveButton.setOnClickListener(v -> {
            // Gather data from UI elements
            String name = binding.nameEditText.getText().toString().trim();
            String phone = binding.phoneEditText.getText().toString().trim();
            String email = binding.emailEditText.getText().toString().trim();
            String gender = binding.autoCompleteTextView.getText().toString().trim();

            // Check if name is empty
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

        return view;
    }

    private void performAddContact(Contact contact) {



        new Thread(() -> {
            contactsViewModel.addContact(contact);


            // Once the contact is saved, navigate back to the contacts list fragment
            requireActivity().runOnUiThread(() -> {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_addContactFragment_to_allContactsFragment);
            });
        }).start();
    }

    private void makeApiCall(String firstName) {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GenderResponse> call = service.getGender(firstName);

        call.enqueue(new Callback<GenderResponse>() {

            public void onResponse(Call<GenderResponse> call, Response<GenderResponse> response) {
                if (response.isSuccessful()) {
                    GenderResponse genderResponse = response.body();
                    if (genderResponse != null) {
                        // Handle the response
                        String gender = genderResponse.getGender();
                        String capitalizedGender =gender.substring(0, 1).toUpperCase() + gender.substring(1);
                        double probability = genderResponse.getProbability();
                        binding.autoCompleteTextView.setText(capitalizedGender);
                        binding.genderTextInputLayout.setHelperText("Gender Probability: " + probability);

                    }
                } else {
                    // Handle error
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            public void onFailure(Call<GenderResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(requireContext(), "Failed to make API call: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}