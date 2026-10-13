package com.example.cst438_project2

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project2.ui.theme.Cst438Project2Theme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreenShowsSignInOptions() {
        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent()
            }
        }

        composeTestRule.onNodeWithText("Fitness App").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in to see your workouts").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in with email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in with Google").assertIsDisplayed()
    }

    @Test
    fun emailButtonCallsSignInWithEnteredCredentials() {
        var enteredEmail = ""
        var enteredPassword = ""

        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent(
                    onEmailPasswordSignIn = { email, password ->
                        enteredEmail = email
                        enteredPassword = password
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Email").performTextInput("user@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Sign in with email").performClick()

        composeTestRule.runOnIdle {
            assertTrue(enteredEmail == "user@example.com")
            assertTrue(enteredPassword == "password123")
        }
    }

    @Test
    fun googleButtonCallsSignIn() {
        var signInCalled = false

        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent(
                    onGoogleSignInClick = { signInCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Sign in with Google").performClick()

        composeTestRule.runOnIdle {
            assertTrue(signInCalled)
        }
    }

    @Test
    fun loginScreenDisplaysSignInError() {
        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent(errorMessage = "Google sign-in failed")
            }
        }

        composeTestRule.onNodeWithText("Google sign-in failed").assertIsDisplayed()
    }

    @Test
    fun homeScreenShowsWelcomeMessage() {
        composeTestRule.setContent {
            Cst438Project2Theme {
                HomeScreen(displayName = "Alex")
            }
        }

        composeTestRule.onNodeWithText("Welcome, Alex!").assertIsDisplayed()
    }
}
