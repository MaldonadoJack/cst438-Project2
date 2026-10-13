package com.example.cst438_project2

import android.Manifest
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438_project2.auth.EmailPasswordAuthRepository
import com.example.cst438_project2.auth.GoogleAuthRepository
import com.example.cst438_project2.auth.UserProfile
import com.example.cst438_project2.ui.theme.Cst438Project2Theme
import kotlinx.coroutines.launch

class LoginScreen : ComponentActivity() {
    private val localNetworkPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocalNetworkPermissionForDebugBuild()
        enableEdgeToEdge()
        setContent {
            Cst438Project2Theme {
                var signedInUser by remember { mutableStateOf<UserProfile?>(null) }
                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                val scope = rememberCoroutineScope()
                val googleAuthRepository = remember {
                    GoogleAuthRepository(applicationContext)
                }
                val emailPasswordAuthRepository = remember {
                    EmailPasswordAuthRepository()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (signedInUser == null) {
                        LoginScreenContent(
                            modifier = Modifier.padding(innerPadding),
                            isLoading = isLoading,
                            errorMessage = errorMessage,
                            onEmailPasswordSignIn = { email, password ->
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    emailPasswordAuthRepository.signIn(email, password)
                                        .onSuccess { signedInUser = it }
                                        .onFailure {
                                            errorMessage = it.message
                                                ?: "Email sign-in failed. Please try again."
                                        }
                                    isLoading = false
                                }
                            },
                            onGoogleSignInClick = {
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    googleAuthRepository.signIn(this@LoginScreen)
                                        .onSuccess { signedInUser = it }
                                        .onFailure {
                                            errorMessage = it.message
                                                ?: "Google sign-in failed. Please try again."
                                        }
                                    isLoading = false
                                }
                            }
                        )
                    } else {
                        HomeScreen(
                            displayName = signedInUser!!.displayName,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun requestLocalNetworkPermissionForDebugBuild() {
        if (
            BuildConfig.DEBUG &&
            Build.VERSION.SDK_INT >= 37 &&
            checkSelfPermission(Manifest.permission.ACCESS_LOCAL_NETWORK) != PackageManager.PERMISSION_GRANTED
        ) {
            localNetworkPermissionLauncher.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
        }
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onEmailPasswordSignIn: (String, String) -> Unit = { _, _ -> },
    onGoogleSignInClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Fitness App",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Sign in to see your workouts",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEmailPasswordSignIn(email, password) },
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with email")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("or", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGoogleSignInClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenContentPreview() {
    Cst438Project2Theme {
        LoginScreenContent()
    }
}
