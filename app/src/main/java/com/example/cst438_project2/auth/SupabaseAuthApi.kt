package com.example.cst438_project2.auth

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseAuthApi {
    @POST("auth/v1/token")
    suspend fun signInWithPassword(
        @Header("apikey") publishableKey: String,
        @Query("grant_type") grantType: String = "password",
        @Body credentials: EmailPasswordCredentials
    ): SupabaseAuthResponse
}

data class EmailPasswordCredentials(
    val email: String,
    val password: String
)

data class SupabaseAuthResponse(
    @SerializedName("access_token") val accessToken: String,
    val user: SupabaseUser
)

data class SupabaseUser(
    val id: String,
    val email: String?,
    @SerializedName("user_metadata") val userMetadata: SupabaseUserMetadata?
)

data class SupabaseUserMetadata(
    @SerializedName("full_name") val fullName: String?,
    val name: String?
)
