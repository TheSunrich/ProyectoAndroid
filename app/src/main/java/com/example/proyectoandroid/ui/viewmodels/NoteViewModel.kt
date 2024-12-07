package com.example.proyectoandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.repositories.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class NoteFilters(
    val title: String = "",
    val status: String = "",
    val tags: String = ""
)

sealed class NoteUiState {
    data object Loading : NoteUiState()
    data class Success(val notes: List<Note>) : NoteUiState()
    data class Error(val message: String) : NoteUiState()
}

class NoteViewModel(private val repository: NoteRepository = NoteRepository()) : ViewModel() {
    private var _uiState = MutableStateFlow<NoteUiState>(NoteUiState.Loading)
    val uiState: StateFlow<NoteUiState> = _uiState

    private val _filters = MutableStateFlow(NoteFilters())
    val filters: StateFlow<NoteFilters> = _filters

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

    private fun filterNotes() {
        viewModelScope.launch {
            try {
                _uiState.value = NoteUiState.Loading
                val notes = repository.getAllNotes()
                val filteredNotes = notes.filter { note ->
                    (filters.value.title.isEmpty() || note.title.contains(filters.value.title, ignoreCase = true)) &&
                            (filters.value.status.isEmpty() || note.status.name.contains(filters.value.status, ignoreCase = true)) &&
                            (filters.value.tags.isEmpty() || note.tags!!.any { tag -> filters.value.tags.contains(tag) })
                }
                _uiState.value = NoteUiState.Success(filteredNotes)

            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }

    }


    fun createNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.createNote(
                    note
                )
                fetchNotes()
            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }

    fun deleteNoteById(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteNoteById(id)
                fetchNotes()
            } catch (e: Exception) {
                _uiState.value = NoteUiState.Error("An error occurred")
            }
        }
    }

    fun onFilterChange(newFilters: NoteFilters) {
        _filters.value = newFilters
        filterNotes()  // Aplicar los filtros
    }
}