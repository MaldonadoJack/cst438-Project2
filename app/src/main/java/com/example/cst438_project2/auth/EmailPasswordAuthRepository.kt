package com.example.cst438_project2.auth

import com.example.cst438_project2.BuildConfig
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmailPasswordAuthRepository(
    private val supabaseUrl: String = BuildConfig.SUPABASE_URL,
    private val publishableKey: String = BuildConfig.SUPABASE_PUBLISHABLE_KEY,
    private val authApi: SupabaseAuthApi = createAuthApi(supabaseUrl)
) {
    suspend fun signIn(email: String, password: String): Result<UserProfile> = runCatching {
        check(!supabaseUrl.contains("YOUR_PROJECT")) {
            "Supabase is not configured."
        }
        check(!publishableKey.startsWith("YOUR_")) {
            "Supabase is not configured."
        }

        val response = try {
            authApi.signInWithPassword(
                publishableKey = publishableKey,
                credentials = EmailPasswordCredentials(
                    email = email.trim(),
                    password = password
                )
            )
        } catch (error: HttpException) {
            if (error.code() == 400 || error.code() == 401) {
                throw IllegalArgumentException("Incorrect email or password.")
            }
            throw error
        }

        val signedInEmail = response.user.email ?: email.trim()
        val displayName = response.user.userMetadata?.fullName
            ?: response.user.userMetadata?.name
            ?: signedInEmail.substringBefore('@')

        UserProfile(
            id = 0,
            email = signedInEmail,
            displayName = displayName
        )
    }

    private companion object {
        fun createAuthApi(supabaseUrl: String): SupabaseAuthApi {
            val baseUrl = if (supabaseUrl.endsWith('/')) supabaseUrl else "$supabaseUrl/"
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SupabaseAuthApi::class.java)
        }
    }
}
