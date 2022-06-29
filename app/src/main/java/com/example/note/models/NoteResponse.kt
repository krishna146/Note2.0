package com.example.note.models

data class NoteResponse(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    var description: String,
    var title: String,
    val updatedAt: String,
    val userId: String
)