package com.example.note.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.note.databinding.FragmentNoteBinding
import com.example.note.models.NoteRequest
import com.example.note.models.NoteResponse
import com.example.note.utils.NetworkResult
import com.example.note.viewmodel.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding
        get() = _binding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()


    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = false
            when (state) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    findNavController().popBackStack()

                }
            }
        }
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it._id)
            }
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(title, description)
            note?.let {
                noteViewModel.updateNote(it._id, noteRequest)
            } ?: run {
                noteViewModel.createNote(noteRequest)
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        jsonNote?.let {
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            binding.txtTitle.setText(note!!.title)
            binding.txtDescription.setText(note!!.description)
        } ?: run {
            binding.btnDelete.visibility = View.INVISIBLE
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}