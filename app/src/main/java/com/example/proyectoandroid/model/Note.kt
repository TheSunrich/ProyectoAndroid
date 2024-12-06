package com.example.proyectoandroid.model

import java.util.Date
import java.util.UUID

data class Note (
    val id: UUID,
    val title: String,
    val content: String,
    val initDate: Date,
    val endDate: Date,
    val tags: List<String>?,
    val status: Status
)


data class Status (
    val name: String,
    val description: String,
    val isActive: Boolean
)