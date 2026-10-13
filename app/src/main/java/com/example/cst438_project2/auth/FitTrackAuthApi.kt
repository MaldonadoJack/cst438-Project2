package com.example.cst438_project2.auth

import retrofit2.http.Header
import retrofit2.http.POST

interface FitTrackAuthApi {
    @POST("api/v1/fittrack/auth/google")
    suspend fun signInWithGoogle(
        @Header("Authorization") authorization: String
    ): UserProfile
}
