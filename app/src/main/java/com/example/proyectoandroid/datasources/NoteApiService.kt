package com.example.proyectoandroid.datasources

import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface NoteApiService {
    @GET("notes")
    suspend fun getAllNotes(): List<Note>

    @GET("notes/{id}")
    suspend fun getNoteById(id: String): Note

    @POST("notes")
    suspend fun createNote(note: Note): Note

    @PUT("notes/{id}")
    suspend fun updateNoteById(id: Int, note: Note): Note

    @PATCH("notes/{id}")
    suspend fun changeStatus(id: Int, status: Status): Note

    @DELETE("notes/{id}")
    suspend fun deleteNoteById(id: Int)

}