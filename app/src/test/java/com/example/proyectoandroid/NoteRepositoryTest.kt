package com.example.proyectoandroid


import com.example.proyectoandroid.datasources.NoteApiService
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.repositories.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.util.Date
import java.util.UUID

@ExperimentalCoroutinesApi
class NoteRepositoryTest {

    private lateinit var noteRepository: NoteRepository

    @Mock
    private lateinit var mockApi: NoteApiService

    @Before
    fun setUp() {
        mockApi = mock(NoteApiService::class.java)
        noteRepository = NoteRepository()
    }

    @Test
    fun `getAllNotes should return a list of notes when API call is successful`() = runTest {
        // Preparamos el mock
        val mockNotes = listOf(
            Note(
                id = "674b67543e026a6cc05e980c",
                title = "Nota de Prueba",
                content = "Esta es una nota de prueba",
                initDate = Date(2024 - 1900, 10, 30, 13, 27), // Ajuste de año para Date
                endDate = Date(2024 - 1900, 10, 30, 15, 30),
                tags = listOf("Prueba"),
                status = Status(
                    name = "Activo",
                    description = "Estatus de nota activo",
                    color = "#FFFFFF",
                    isActive = true
                )
            ),
            Note(
                id = "6753dcf32e6023abb65370c5",
                title = "prueba 2",
                content = "esta es la prueba 2",
                initDate = Date(2024 - 1900, 11, 6, 6, 30, 17),
                endDate = Date(2024 - 1900, 11, 6, 19, 30, 17),
                tags = listOf("test", "prueba", "recordatorio"),
                status = Status(
                    name = "Borrador",
                    description = "Nota no activa",
                    color = "#FFFFFF",
                    isActive = true
                )
            ),
            Note(
                id = "6753df532e6023abb65370cb",
                title = "prueba 3",
                content = "esta es la nota 3, que contiene esti",
                initDate = Date(2024 - 1900, 11, 4, 6, 37, 39),
                endDate = Date(2024 - 1900, 11, 6, 6, 37, 39),
                tags = listOf("test", "algo", "prueba"),
                status = Status(
                    name = "Borrador",
                    description = "Nota no activa",
                    color = "#FFFFFF",
                    isActive = true
                )
            ),
            Note(
                id = "6753ffe7f4caea28ff200f60",
                title = "prueba 4",
                content = "esta es la prueba 4",
                initDate = Date(2024 - 1900, 11, 6, 19, 56, 57),
                endDate = Date(2024 - 1900, 11, 6, 20, 5, 57),
                tags = listOf("tag", "etiqueta"),
                status = Status(
                    name = "Activo",
                    description = "Nota activa",
                    color = "#00FF00",
                    isActive = true
                )
            )
        )


        `when`(mockApi.getAllNotes()).thenReturn(mockNotes)

        // Llamamos a la función de repositorio
        val result = noteRepository.getAllNotes()

        // Verificamos el resultado
        assertEquals(mockNotes, result)
    }

    @Test
    fun `getNoteById should return a note when API call is successful`() = runTest {
        val mockNote = Note(
            id = "674b67543e026a6cc05e980c",
            title = "Nota de Prueba",
            content = "Esta es una nota de prueba",
            initDate = Date(2024 - 1900, 10, 30, 13, 27), // Ajuste de año para Date
            endDate = Date(2024 - 1900, 10, 30, 15, 30),
            tags = listOf("Prueba"),
            status = Status(
                name = "Activo",
                description = "Estatus de nota activo",
                color = "#FFFFFF",
                isActive = true
            )
        )

        `when`(mockApi.getNoteById("674b67543e026a6cc05e980c")).thenReturn(mockNote)

        val result = noteRepository.getNoteById("674b67543e026a6cc05e980c")

        assertEquals(mockNote, result)
    }

    @Test
    fun `createNote should return a note when API call is successful`() = runTest {
        val newNote = Note(
            id = UUID.randomUUID().toString(),
            title = "New Note",
            content = "Content",
            initDate = Date(),
            endDate = Date(),
            tags = listOf("newtag"),
            status = Status("active", "active status", "#FF00FF", true)
        )

        `when`(mockApi.createNote(newNote)).thenReturn(newNote)

        val result = noteRepository.createNote(newNote)


        assertEquals(newNote.title, result.title)
        assertEquals(newNote.content, result.content)
        assertEquals(newNote.tags, result.tags)
        assertEquals(newNote.status, result.status)
    }

    @Test
    fun `updateNoteById should return updated note when API call is successful`() = runTest {
        val updatedNote = Note(
            id = "1",
            title = "Updated Note",
            content = "Updated Content",
            initDate = Date(),
            endDate = Date(),
            tags = listOf("updatedtag"),
            status = Status("active", "active status", "#FF00FF", true)
        )

        `when`(mockApi.updateNoteById("1", updatedNote)).thenReturn(updatedNote)

        val result = noteRepository.updateNoteById("1", updatedNote)

        assertEquals(updatedNote, result)
    }

    @Test
    fun `deleteNoteById should call API delete without errors`() = runTest {

        whenever(mockApi.deleteNoteById(anyString())).thenReturn(Unit)

        mockApi.deleteNoteById("675426e1f4caea28ff201098")

        verify(mockApi).deleteNoteById("675426e1f4caea28ff201098")
    }
}