package com.example.proyectoandroid.model

import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class Note (
    @SerializedName("_id")
    val id: String,
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
    val color: String = "#FFFFFF",
    val isActive: Boolean
)