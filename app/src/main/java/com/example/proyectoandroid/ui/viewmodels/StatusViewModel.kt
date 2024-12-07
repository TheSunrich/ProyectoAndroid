package com.example.proyectoandroid.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoandroid.model.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun String.toStatusList(): List<Status> {
    val listType = object : TypeToken<List<Status>>() {}.type
    return Gson().fromJson(this, listType) ?: emptyList()
}

class StatusViewModel : ViewModel() {
    private val fileName = "status.json"
    private val _status = MutableStateFlow(
        Status(
            name = "",
            description = "",
            color = "#FFFFFF",
            isActive = false
        )
    )
    val status: StateFlow<Status> = _status

    private val _fileContent = MutableStateFlow<List<Status>>(emptyList())
    val fileContent: StateFlow<List<Status>> = _fileContent



    fun onStatusChange(newStatus: Status) {
        _status.value = newStatus
    }

    fun addStatus(context: Context, status: Status) {
        viewModelScope.launch {
            val file = File(context.filesDir, fileName)
            val statusList = _fileContent.value.toMutableList()
            statusList.add(status)
            file.writeText(Gson().toJson(statusList))
            _fileContent.value = statusList
        }
    }

    fun deleteStatus(context: Context, status: Status) {
        viewModelScope.launch {
            val file = File(context.filesDir, fileName)
            val statusList = _fileContent.value.toMutableList()
            statusList.remove(status)
            file.writeText(Gson().toJson(statusList))
            _fileContent.value = statusList
        }
    }

    fun readFromFile(context: Context) {
        viewModelScope.launch {
            val file = File(context.filesDir, fileName)
            _fileContent.value = if (file.exists()) {
                file.readText().toStatusList()
            } else {
                emptyList()
            }
        }
    }
}