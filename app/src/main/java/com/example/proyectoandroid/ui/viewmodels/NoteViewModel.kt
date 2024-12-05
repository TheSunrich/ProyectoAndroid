package com.example.proyectoandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.repositories.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NoteUiState {
    data object Loading : NoteUiState()
    data class Success(val countries: List<Note>) : NoteUiState()
    data class Error(val message: String) : NoteUiState()
}

class CountryViewModel(private val repository: NoteRepository = NoteRepository()) :
    ViewModel() {
    private var _uiState = MutableStateFlow<NoteUiState>(NoteUiState.Loading)
    val uiState: StateFlow<NoteUiState> = _uiState

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            try {
                _uiState.value = NoteUiState.Loading
                val notes = repository.getAllNotes()
                _uiState.value = NoteUiState.Success(notes)

            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }

    fun createNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.createNote(note)
                fetchNotes()
            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }

    fun updateNoteById(id: Int, note: Note) {
        viewModelScope.launch {
            try {
                repository.updateNoteById(id, note)
                fetchNotes()
            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }

    fun changeStatus(id: Int, status: Status) {
        viewModelScope.launch {
            try {
                repository.changeStatus(id, status)
                fetchNotes()
            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteNoteById(id)
                fetchNotes()
            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }


}