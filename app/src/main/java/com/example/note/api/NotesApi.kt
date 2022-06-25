package com.example.note.api

import com.example.note.models.NoteRequest
import com.example.note.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesApi {
    @GET("/note")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/note")
    suspend fun insertNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("/note/{noteId}")//since noteId is variable
    suspend fun updateNote(@Path("noteId") noteId: String, @Body noteRequest: NoteRequest): Response<NoteResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NoteResponse>
}