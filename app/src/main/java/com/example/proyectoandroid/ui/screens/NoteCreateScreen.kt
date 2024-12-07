package com.example.proyectoandroid.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.proyectoandroid.model.Note
import java.util.UUID
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.ui.components.Tag
import com.example.proyectoandroid.ui.viewmodels.NoteUiState
import com.example.proyectoandroid.ui.viewmodels.NoteViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteCreationScreen(onSave: (Note) -> Unit, onCancel: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    val initDate = Date() // Fecha actual
    val endDate = remember { mutableStateOf(LocalDate.now()) }
    var selectedStatus by remember {
        mutableStateOf(
            Status(
                "Borrador",
                "Nota no activa",
                isActive = false
            )
        )
    }

    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf(
        Status("Borrador", "Nota no activa", isActive = true),
        Status("Activo", "Nota activa", isActive = true),
        Status("Archivado", "Nota archivada", isActive = true)
    )

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onCancel) { Text("Cancelar") }
                Button(onClick = {
                    val note = Note(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        content = content,
                        initDate = initDate,
                        endDate =  Date.from(endDate.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        tags = tags.split(",").map { it.trim() },
                        status = selectedStatus
                    )
                    onSave(note)
                }) {
                    Text("Guardar")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { title = it },
                label = { Text("Título") },
                singleLine = true
            )

            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            TextField(
                value = tags,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { tags = it },
                label = { Text("Etiquetas (separadas por comas)") }
            )

            DatePickerComponent(
                initialDate = endDate.value,
                onDateSelected = { date ->
                    endDate.value = date
                }
            )

            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
                    .fillMaxWidth()
            ) {
                Button(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Seleccione un estatus: ${selectedStatus.name}")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    statuses.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(text = status.name) },
                            onClick = {
                                selectedStatus = status
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerComponent(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(initialDate) }

    var showDatePicker by remember { mutableStateOf(false) }

    Button(onClick = { showDatePicker = true }) {
        Text(text = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    }

    if (showDatePicker) {
        val context = LocalContext.current
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                onDateSelected(selectedDate) // Devuelve la fecha seleccionada
                showDatePicker = false
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        ).show()
    }
}