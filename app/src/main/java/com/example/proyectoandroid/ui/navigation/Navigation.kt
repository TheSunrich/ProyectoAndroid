package com.example.proyectoandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectoandroid.ui.screens.NoteDetailScreen
import com.example.proyectoandroid.ui.screens.NoteListScreen
import com.example.proyectoandroid.ui.viewmodels.NoteViewModel

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val noteViewModel: NoteViewModel = viewModel()
    val navController = rememberNavController()

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
            val note = noteViewModel.getNoteById(noteId)
            if (note != null) {
                NoteDetailScreen(note = note, onBack = { navController.popBackStack() })
            }
        }
        composable("status") {

        }
    }
}