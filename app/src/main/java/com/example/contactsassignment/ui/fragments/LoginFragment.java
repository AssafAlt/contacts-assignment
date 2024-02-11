package com.example.contactsassignment.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.contactsassignment.R;
import com.example.contactsassignment.data.loacl_db.PrefManager;
import com.example.contactsassignment.data.repository.UserRepository;
import com.example.contactsassignment.databinding.FragmentLoginBinding;




public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private PrefManager prefManager;
    private UserRepository userRepository;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(requireContext());
        userRepository = new UserRepository(requireContext());


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
                Integer userId = userRepository.loginUser(username, password);
                requireActivity().runOnUiThread(() -> {
                    if (userId != null && userId > 0) {

                        prefManager.saveUserId(userId);
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_loginFragment_to_allContactsFragment);

                    } else {
                        boolean isUserExists = userRepository.isUserExists(username);
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

