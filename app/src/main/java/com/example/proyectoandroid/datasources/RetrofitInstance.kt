package com.example.proyectoandroid.datasources

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://restcountries.com/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val client : OkHttpClient = OkHttpClient.Builder().build()

    val api: NoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NoteApiService::class.java)
    }


}