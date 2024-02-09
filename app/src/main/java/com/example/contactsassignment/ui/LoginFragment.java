package com.example.contactsassignment.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.contactsassignment.R;
import com.example.contactsassignment.data.loacl_db.PrefManager;
import com.example.contactsassignment.data.repository.UserRepository;
import com.example.contactsassignment.databinding.FragmentLoginBinding;
import com.example.contactsassignment.databinding.FragmentSignUpBinding;


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
        View view = binding.getRoot();
        binding.buttonLogin.setOnClickListener(v -> performLogin());
        binding.textViewToRegister.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_loginFragment_to_signUpFragment);

        });

        return view;
    }
    private void performLogin() {
        String username = binding.editTextLoginUsername.getText().toString();
        String password = binding.editTextLoginPassword.getText().toString();

        new Thread(() -> {
            Integer userId = userRepository.loginUser(username, password);
            requireActivity().runOnUiThread(() -> {
                if (userId != null && userId > 0) {

                    prefManager.saveUserId(userId);
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_loginFragment_to_allContactsFragment);

                } else {

                    Toast.makeText(requireContext(), "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
    }

