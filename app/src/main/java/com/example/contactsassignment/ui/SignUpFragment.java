package com.example.contactsassignment.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.contactsassignment.R;
import com.example.contactsassignment.data.loacl_db.AppDatabase;
import com.example.contactsassignment.data.loacl_db.UserDao;
import com.example.contactsassignment.data.models.User;
import com.example.contactsassignment.data.repository.UserRepository;
import com.example.contactsassignment.databinding.FragmentSignUpBinding;


public class SignUpFragment extends Fragment {
private  FragmentSignUpBinding binding;
    private UserRepository userRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository(requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonSignup.setOnClickListener(v->performSignup());
        binding.textViewToLogin.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_signUpFragment_to_loginFragment);

        });

        return view;
    }

    private void performSignup() {
        String username = binding.editTextUsername.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Execute signup process asynchronously
        new Thread(() -> {
            // Check if the username already exists
            boolean existingUser = userRepository.isUserExists(username);
            if (existingUser) {
                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_SHORT).show());
                return;
            }

            // Register the user
            User user = new User(username, password);
            userRepository.registerUser(user);

            // Display toast message for successful registration
            requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show());

            // Clear input fields
            requireActivity().runOnUiThread(() -> {
                binding.editTextUsername.setText("");
                binding.editTextPassword.setText("");
                binding.editTextConfirmPassword.setText("");
            });
        }).start();
    }

}
