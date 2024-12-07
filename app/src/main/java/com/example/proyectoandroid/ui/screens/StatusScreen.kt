package com.example.proyectoandroid.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoandroid.R
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.ui.viewmodels.StatusViewModel


@Preview(showBackground = true)
@Composable
fun StatusScreenPreview() {
    StatusScreen()
}

@Composable
fun StatusScreen(
    statusViewModel: StatusViewModel = viewModel()
) {
    val context: Context = LocalContext.current

    val listStatus by statusViewModel.fileContent.collectAsState()
    statusViewModel.readFromFile(context)

    var showPopup by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showPopup = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Status")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = stringResource(R.string.status_list),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listStatus) { status ->
                    StatusItem(status, onDeleteStatus = {
                        statusViewModel.deleteStatus(context, it)
                    })
                }
            }

            if (showPopup) {
                AddStatusPopup(
                    onDismiss = { showPopup = false },
                    onAdd = { newStatus ->
                        statusViewModel.addStatus(
                            context = context,
                            status = newStatus
                        )
                        showPopup = false
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun StatusItem(
    status: Status = Status(
        name = "New",
        description = "New status",
        color = "#FFFFFF",
        isActive = true
    ),
    onDeleteStatus: (Status) -> Unit = {}
) {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .size(32.dp)
                    .background(Color(android.graphics.Color.parseColor(status.color)))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = status.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete Status",
                modifier = Modifier.clickable {
                    onDeleteStatus(status)
                },
                tint = Color.Red
            )
        }
    }

}

@Composable
fun AddStatusPopup(onDismiss: () -> Unit, onAdd: (Status) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#FFFFFF") }
    var showColorPicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.add_status), style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showColorPicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .size(24.dp)
                                .background(Color(android.graphics.Color.parseColor(color)))

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.select_color))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotEmpty() && description.isNotEmpty()) {
                                onAdd(
                                    Status(
                                        name = name,
                                        description = description,
                                        color = color,
                                        isActive = true
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }

    if (showColorPicker) {
        ColorPickerDialog(
            onDismiss = { showColorPicker = false },
            onColorSelected = { newColor ->
                color = newColor
                showColorPicker = false
            }
        )
    }
}

@Composable
fun ColorPickerDialog(onDismiss: () -> Unit, onColorSelected: (String) -> Unit) {
    val colors = listOf("#FF0000", "#00FF00", "#4BB7E9", "#FFFF00", "#FFA500", "#7F4CC6", "#000000", "#FFFFFF", "#9B9B9B")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.select_color), style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    colors.forEach { color ->
                        item {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(8.dp)
                                    .background(Color(android.graphics.Color.parseColor(color)))
                                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                                    .clickable {
                                        onColorSelected(color)
                                    }
                                    .border(1.dp, Color.Black)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}