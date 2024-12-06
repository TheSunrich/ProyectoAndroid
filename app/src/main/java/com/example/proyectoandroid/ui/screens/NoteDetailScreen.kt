package com.example.proyectoandroid.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoandroid.model.Note
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NoteDetailScreen(note: Note, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detalle de Nota") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            })
        }
    ) {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Título:", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(text = note.title, style = TextStyle(fontSize = 18.sp))

            Text(text = "Contenido:", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(text = note.content, style = TextStyle(fontSize = 16.sp))

            Text(text = "Fecha de Inicio:", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(text = formatter.format(note.initDate), style = TextStyle(fontSize = 16.sp))

            Text(text = "Fecha de Fin:", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(text = formatter.format(note.endDate), style = TextStyle(fontSize = 16.sp))

            Text(text = "Etiquetas:", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                note.tags?.forEach { tag ->
                    Chip(label = tag)
                }
            }

            Text(text = "Estado:", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(text = "${note.status.name}: ${note.status.description}", style = TextStyle(fontSize = 16.sp))
        }
    }
}

@Composable
fun Chip(label: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = label, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
    }
}
