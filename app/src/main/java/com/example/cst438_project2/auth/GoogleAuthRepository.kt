package com.example.cst438_project2.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.cst438_project2.BuildConfig
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthRepository(
    context: Context,
    private val webClientId: String = BuildConfig.GOOGLE_WEB_CLIENT_ID,
    private val authApi: FitTrackAuthApi = FitTrackApiClient.authApi
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(activity: Activity): Result<UserProfile> = runCatching {
        check(!webClientId.startsWith("YOUR_")) {
            "Google OAuth is not configured. Add GOOGLE_WEB_CLIENT_ID to ~/.gradle/gradle.properties."
        }

        val googleOption = GetSignInWithGoogleOption.Builder(webClientId).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        val response = credentialManager.getCredential(
            context = activity,
            request = request
        )
        val customCredential = response.credential as? CustomCredential
            ?: error("Google did not return a supported credential.")

        check(customCredential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Google returned an unexpected credential type."
        }

        val googleCredential = GoogleIdTokenCredential.createFrom(customCredential.data)
        authApi.signInWithGoogle("Bearer ${googleCredential.idToken}")
    }
}
