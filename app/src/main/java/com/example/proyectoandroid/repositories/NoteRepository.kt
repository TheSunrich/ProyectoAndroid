package com.example.proyectoandroid.repositories

import com.example.proyectoandroid.datasources.RetrofitInstance
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status

class NoteRepository {
    private var api = RetrofitInstance.api

    suspend fun getAllNotes(): List<Note> {
        return api.getAllNotes()
    }

    suspend fun getNoteById(id: String): Note {
        return api.getNoteById(id)
    }

    suspend fun createNote(note: Note): Note {
        return api.createNote(note)
    }

    suspend fun updateNoteById(id: String, note: Note): Note {
        return api.updateNoteById(id, note)
    }

    suspend fun changeStatus(id: String, status: Status): Note {
        return api.changeStatus(id, status)
    }

    suspend fun deleteNoteById(id: String) {
        return api.deleteNoteById(id)
    }
}