package com.example.proyectoandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.ui.viewmodels.NoteUiState
import com.example.proyectoandroid.ui.viewmodels.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun NoteListScreen(noteViewModel: NoteViewModel = viewModel(), navController: NavController, onNoteClick: (String) -> Unit) {
    val uiState by noteViewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(1000.dp)),
                onClick = {
                    navController.navigate("noteCreate")
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is NoteUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is NoteUiState.Success -> {
                    val notes = (uiState as NoteUiState.Success).notes
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(notes) { note ->
                            NoteItem(note, onNoteClick = onNoteClick)
                        }
                    }
                }

                is NoteUiState.Error -> {
                    val message = (uiState as NoteUiState.Error).message
                    Text(
                        text = message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoteItem(
    note: Note = Note(
        id = UUID.randomUUID(),
        title = "Title",
        content = "Content",
        initDate = Date(),
        endDate = Date(),
        tags = listOf("tag1", "tag2"),
        status = Status("status name", "description", true)
    ),
    onNoteClick: (String) -> Unit = {}
) {
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = { onNoteClick(note.id.toString()) })
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = TextStyle(fontSize = 20.sp))
            }
            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(1000.dp))
                        .background(Color.Red)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = note.status.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = note.content)
            }
            Column {
                Text(text = formatter.format(note.initDate))
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = formatter.format(note.endDate))
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            for (tag in note.tags!!) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(1000.dp))
                        .background(Color.Gray)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = tag,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
