package com.example.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.note.databinding.FragmentRegisterBinding
import com.example.note.models.User
import com.example.note.models.UserRequest
import com.example.note.utils.Constants
import com.example.note.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

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
        // Inflate the layout for this fragment
//        val view =  inflater.inflate(R.layout.fragment_register, container, false)
//        val txtNavigate = view.findViewById<TextView>(R.id.txtNavigate)
        binding.txtLogin.setOnClickListener {
            authViewModel.loginUser(UserRequest("heljasiwasloo@gmail.com", "111111", "krishnajswq2"))
        }
        binding.btnSignUp.setOnClickListener {
            authViewModel.registerUser(UserRequest("heljasiwasloo@gmail.com", "111111", "krishnajswq2"))
//            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {networkResult ->
            binding.progressBar.isVisible = false
            when(networkResult){
                is NetworkResult.Error ->{
                    binding.txtError.text = networkResult.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    //TODO : Saving Token
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}