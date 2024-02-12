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


import com.example.contactsassignment.R;
import com.example.contactsassignment.PrefManager;
import com.example.contactsassignment.data.repository.UserRepository;
import com.example.contactsassignment.databinding.FragmentLoginBinding;
import com.example.contactsassignment.ui.view_models.AuthViewModel;
import com.example.contactsassignment.ui.view_models.ContactsViewModel;


public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;
    private FragmentLoginBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.loginButton.setOnClickListener(v ->{
            String username = binding.editTextLoginUsername.getText().toString();
            String password = binding.editTextLoginPassword.getText().toString();
            boolean isValid =validateInputs(username,password);
            if(isValid){
                performLogin(username,password);
            }
                    });

        binding.textViewToRegister.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_loginFragment_to_signUpFragment);

        });

        return binding.getRoot();
    }
    private void performLogin(String username,String password) {

            new Thread(() -> {
                int userId = authViewModel.loginUser(username,password);
                requireActivity().runOnUiThread(() -> {
                    if ( userId > 0) {

                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_loginFragment_to_allContactsFragment);

                    } else {
                        boolean isUserExists = authViewModel.isUserExists(username);
                        if(isUserExists){
                            binding.passwordTextInputLayout.setError("Incorrect Password!");
                        }
                        else{
                            binding.passwordTextInputLayout.setError("Username or Password incorrect, please check your credentials!");
                        }


                    }
                });
            }).start();
    }
    private boolean validateInputs(String username,String password){
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
        return isValid;
    }
    }

