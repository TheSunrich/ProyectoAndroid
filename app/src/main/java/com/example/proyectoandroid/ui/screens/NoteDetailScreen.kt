package com.example.proyectoandroid.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoandroid.model.Note
import com.example.proyectoandroid.ui.components.Tag
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteDetailScreen(note: Note, onBack: () -> Unit) {

    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Título: ${note.title}",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
                Tag(
                    text = note.status.name,
                    color = Color(android.graphics.Color.parseColor(note.status.color))
                )
            }

            Row {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Fecha de Inicio:",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = formatter.format(note.initDate),
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Fecha de Fin",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = formatter.format(note.endDate),
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Descripción de la nota:",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
            Text(text = note.content, style = TextStyle(fontSize = 16.sp))

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Etiquetas:",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                note.tags?.forEach { tag ->
                    Tag(text = tag)
                }
            }

            Text(
                text = "Estado:",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )

            Text(
                text = "${note.status.name}: ${note.status.description}",
                style = TextStyle(fontSize = 16.sp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = "Update",
                    modifier = Modifier.size(24.dp)
                )
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}
