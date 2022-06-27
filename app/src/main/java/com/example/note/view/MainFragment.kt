package com.example.note.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.adapter.NoteAdapter
import com.example.note.databinding.FragmentMainBinding
import com.example.note.utils.Constants.TAG
import com.example.note.utils.NetworkResult
import com.example.note.viewmodel.AuthViewModel
import com.example.note.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        noteAdapter = NoteAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = noteAdapter
        noteViewModel.getNotes()
        observers()
    }

    private fun observers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = false
            when (state) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    Log.d(TAG, state.data.toString())
                    noteAdapter.submitList(state.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
