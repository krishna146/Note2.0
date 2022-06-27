package com.example.note.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.note.R
import com.example.note.databinding.FragmentRegisterBinding
import com.example.note.models.UserRequest
import com.example.note.utils.NetworkResult
import com.example.note.utils.validateCredentials
import com.example.note.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.registerUser(provideUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }

        }
        observer()
    }

    private fun provideUserRequest(): UserRequest {
        val userName = binding.txtUsername.text.toString()
        val userEmail = binding.txtEmail.text.toString()
        val userPassword = binding.txtPassword.text.toString()
        return UserRequest(userEmail, userPassword, userName)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = provideUserRequest()
        return validateCredentials(userRequest)
    }

    private fun observer() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) { networkResult ->
            binding.progressBar.isVisible = false
            when (networkResult) {
                is NetworkResult.Error -> {
                    binding.txtError.text = networkResult.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        authViewModel.getSession {
            it?.let {
                findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}