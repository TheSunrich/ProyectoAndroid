package com.example.proyectoandroid.ui.screens

import android.util.Log
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoandroid.R
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.ui.components.Tag
import com.example.proyectoandroid.ui.viewmodels.NoteFilters
import com.example.proyectoandroid.ui.viewmodels.NoteUiState
import com.example.proyectoandroid.ui.viewmodels.NoteViewModel
import com.example.proyectoandroid.ui.viewmodels.StatusViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun NoteListScreenPreview() {
    NoteListScreen(
        noteViewModel = viewModel(),
        navController = NavController(LocalContext.current),
        onNoteClick = {}
    )
}

@Composable
fun NoteListScreen(
    noteViewModel: NoteViewModel = viewModel(),
    navController: NavController,
    onNoteClick: (String) -> Unit
) {
    val uiState by noteViewModel.uiState.collectAsState()
    val filters by noteViewModel.filters.collectAsState()

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
            Column {
                FilterSection(filters, noteViewModel)

                when (uiState) {
                    is NoteUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    is NoteUiState.Success -> {
                        val notes = (uiState as NoteUiState.Success).notes
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(notes) { note ->
                                NoteItem(note, onNoteClick = onNoteClick)
                            }
                        }
                    }

                    is NoteUiState.Error -> {
                        val message = (uiState as NoteUiState.Error).message
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
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
    }
}


@Preview
@Composable
fun FilterSection(
    filters: NoteFilters = NoteFilters(),
    noteViewModel: NoteViewModel = viewModel()
) {
    val statusViewModel: StatusViewModel = viewModel()
    val listStatus by statusViewModel.fileContent.collectAsState()
    statusViewModel.readFromFile(LocalContext.current)

    val searchTitleQuery = remember { mutableStateOf(filters.title) }
    val searchTagQuery = remember { mutableStateOf(filters.title) }


    val debounceJob = remember { mutableStateOf<Job?>(null) }


    Log.d("FilterSection", "listStatus: $listStatus")


    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Row(

            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchTitleQuery.value,
                onValueChange = {
                    debounceJob.value?.cancel()

                    searchTitleQuery.value = it

                    debounceJob.value = CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        noteViewModel.onFilterChange(filters.copy(title = it))
                    }
                },
                label = { Text(stringResource(R.string.title_filter)) },
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = searchTagQuery.value,
                onValueChange = {
                    debounceJob.value?.cancel()

                    searchTagQuery.value = it

                    debounceJob.value = CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        noteViewModel.onFilterChange(filters.copy(tags = it))
                    }
                },
                label = { Text(stringResource(R.string.tag_filter)) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listStatus) { status ->
                StatusButton(
                    status = status,
                    noteViewModel = noteViewModel,
                    onClick = {
                        noteViewModel.onFilterChange(
                            filters.copy(status = if (filters.status == status.name) "" else status.name)
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun StatusButton(
    status: Status = Status(
        name = "Activo",
        description = "Nota Activa",
        color = "#00FF00",
        isActive = false
    ),
    noteViewModel: NoteViewModel = viewModel(),
    onClick: () -> Unit = {}
) {
    val filters by noteViewModel.filters.collectAsState()

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (filters.status == status.name) Color.Unspecified else Color.Gray
        )
    ) {
        Text(
            text = status.name,
            style = TextStyle(
                color = if (status.isActive) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItem(
    note: Note = Note(
        id = "1",
        title = "Title",
        content = "Content",
        initDate = Date(),
        endDate = Date(),
        tags = listOf("tag1", "tag2"),
        status = Status("status name", "description", "#FFFFFF", true)
    ),
    onNoteClick: (String) -> Unit = {}
) {
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = { onNoteClick(note.id) })
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = TextStyle(fontSize = 20.sp))
            }
            Column {
                Tag(
                    text = note.status.name,
                    color = Color(
                        android.graphics.Color.parseColor(note.status.color)
                    )
                )
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
                Tag(text = tag)
            }
        }
    }
}
