package com.example.cst438_project2.auth

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FitTrackApiClient {
    private const val LOCAL_EMULATOR_URL = "http://10.0.2.2:8080/"

    val authApi: FitTrackAuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(LOCAL_EMULATOR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FitTrackAuthApi::class.java)
    }
}
