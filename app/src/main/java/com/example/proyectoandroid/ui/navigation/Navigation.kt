package com.example.proyectoandroid.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.repositories.NoteRepository
import com.example.proyectoandroid.ui.screens.NoteCreationScreen
import com.example.proyectoandroid.ui.screens.NoteDetailScreen
import com.example.proyectoandroid.ui.screens.NoteListScreen
import com.example.proyectoandroid.ui.viewmodels.NoteViewModel

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val noteViewModel: NoteViewModel = viewModel()
    val repository: NoteRepository = NoteRepository()

    NavHost(navController = navController, startDestination = "notes", modifier = modifier) {
        composable("notes") {
            NoteListScreen(
                navController = navController,
                onNoteClick = { id ->
                    navController.navigate("noteDetail/$id")
                })
        }
        composable(
            "noteDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("id") ?: ""
            val coroutineScope = rememberCoroutineScope()

            var note by remember { mutableStateOf<Note?>(null) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(noteId) {
                try {
                    note = repository.getNoteById(noteId)
                } catch (e: Exception) {
                    error = e.message
                }
            }
            if (note != null) {
                NoteDetailScreen(note = note!!, onBack = { navController.popBackStack() })
            } else if (error != null) {
                Text("Error: ${error.orEmpty()}")
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }

            Log.d("NoteDetailScreen", "noteId: $noteId")
            Log.d("NoteDetailScreen", "note: $note")
        }
        composable("noteCreate") {
            NoteCreationScreen(onSave = { note ->
                //repository.createNote(note)
                navController.popBackStack()
            }, onCancel = {
                navController.popBackStack()
            })
        }
        composable("status") {

        }
    }
}