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
    private FragmentSignUpBinding binding;
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

        binding.registerButton.setOnClickListener(v -> {
            String username = binding.editTextRegisterUsername.getText().toString().trim();
            String password = binding.editTextRegisterPassword.getText().toString();
            String confirmPassword = binding.editTextRegisterConfirmPassword.getText().toString();
            if (validateInputs(username,password,confirmPassword)) {
                performSignup(username,password);
            }

        });
        binding.textViewToLogin.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_signUpFragment_to_loginFragment);

        });

        return view;
    }

    private boolean validateInputs(String username,String password,String confirmPassword) {
        boolean isValid = true;


        if (username.isEmpty()) {
            binding.usernameTextInputLayout.setError("Username cannot be empty");
            isValid = false;
        } else {
            binding.usernameTextInputLayout.setError(null);
        }



        if (password.isEmpty()) {
            binding.passwordTextInputLayout.setError("Password cannot be empty");
            isValid = false;
        } else {
            binding.passwordTextInputLayout.setError(null);
        }



        if (!confirmPassword.equals(password)) {
            binding.confirmPasswordTextInputLayout.setError("Passwords do not match");
            isValid = false;
        } else {
            binding.confirmPasswordTextInputLayout.setError(null);
        }

        return isValid;
    }

    private void performSignup(String username,String password) {


            new Thread(() -> {
                boolean existingUser = userRepository.isUserExists(username);
                if(existingUser){
                    requireActivity().runOnUiThread(() -> {
                        binding.usernameTextInputLayout.setError("Username is already exists");

                    });
                    return;

                }
                User user = new User(username, password);
                userRepository.registerUser(user);

                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show());

                requireActivity().runOnUiThread(() -> {
                    binding.editTextRegisterUsername.setText("");
                    binding.editTextRegisterPassword.setText("");
                    binding.editTextRegisterConfirmPassword.setText("");
                });

                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signUpFragment_to_loginFragment);
            }).start();

    }


}


