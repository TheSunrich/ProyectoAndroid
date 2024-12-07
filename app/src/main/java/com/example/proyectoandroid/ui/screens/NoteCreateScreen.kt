package com.example.proyectoandroid.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoandroid.R
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.model.Status
import com.example.proyectoandroid.ui.viewmodels.StatusViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun NoteCreationScreenPreview() {
    NoteCreationScreen(onSave = {}, onCancel = {})
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteCreationScreen(onSave: (Note) -> Unit, onCancel: () -> Unit) {
    val statusViewModel: StatusViewModel = viewModel()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var initDate by remember { mutableStateOf(LocalDateTime.now()) }
    var endDate by remember { mutableStateOf(LocalDateTime.now()) }

    var expanded by remember { mutableStateOf(false) }


    val listStatus by statusViewModel.fileContent.collectAsState()
    statusViewModel.readFromFile(context)

    var selectedStatus by remember {
        mutableStateOf(
            listStatus.firstOrNull() ?: Status(
                name = "Activo",
                description = "Nota Activa",
                color = "#00FF00",
                isActive = false
            )
        )
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onCancel) { Text(stringResource(R.string.cancel)) }
                Button(onClick = {
                    val note = Note(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        content = content,
                        initDate = Date.from(initDate.atZone(ZoneId.systemDefault()).toInstant()),
                        endDate =  Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()),
                        tags = tags.split(",").map { it.trim() },
                        status = selectedStatus
                    )
                    onSave(note)
                }) {
                    Text(stringResource(R.string.save))
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
                label = { Text(stringResource(R.string.title)) },
                singleLine = true
            )

            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.content)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            TextField(
                value = tags,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { tags = it },
                label = { Text(stringResource(R.string.tags)) }
            )

            DateTimePickerComponent(
                text = stringResource(R.string.init_date),
                initialDateTime = initDate,
                onDateTimeSelected = { date ->
                    initDate = date
                }
            )

            DateTimePickerComponent(
                text = stringResource(R.string.end_date),
                initialDateTime = endDate,
                onDateTimeSelected = { date ->
                    endDate = date
                }
            )

            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
                    .fillMaxWidth()
            ) {
                Button(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "${stringResource(R.string.select_status)}: ${selectedStatus.name}")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listStatus.forEach { status ->
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
fun DateTimePickerComponent(
    text: String,
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDateTimeSelected: (LocalDateTime) -> Unit = {}
) {

    var selectedDateTime by remember { mutableStateOf(initialDateTime) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempDateTime by remember { mutableStateOf(initialDateTime) }

    Button(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${stringResource(R.string.select)} ${text}: ${selectedDateTime.format(DateTimeFormatter.ofPattern(" dd / MM / yyyy HH:mm"))}",
            modifier = Modifier.padding(8.dp)
        )
    }

    if (showDatePicker) {
        val context = LocalContext.current
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                tempDateTime = tempDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
                showDatePicker = false
                showTimePicker = true
            },
            tempDateTime.year,
            tempDateTime.monthValue - 1,
            tempDateTime.dayOfMonth
        ).show()
    }

    if (showTimePicker) {
        val context = LocalContext.current
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                tempDateTime = tempDateTime.withHour(hourOfDay).withMinute(minute)
                selectedDateTime = tempDateTime
                showTimePicker = false
                onDateTimeSelected(selectedDateTime)
            },
            tempDateTime.hour,
            tempDateTime.minute,
            true
        ).show()
    }
}
