package com.example.cst438_project2

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project2.ui.theme.Cst438Project2Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_verifyAllUIElementsAreDisplayed() {
        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent()
            }
        }

        // Verify titles are displayed
        composeTestRule.onNodeWithText("Fitness App").assertIsDisplayed()
        
        // Verify text fields are displayed by checking their labels
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        
        // Verify the sign-up button is displayed
        composeTestRule.onNodeWithText("Don't have an account? Sign up").assertIsDisplayed()
    }

    @Test
    fun loginScreen_canInputUsername() {
        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent()
            }
        }

        // Input text into username field
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        
        // Verify the typed text is displayed
        composeTestRule.onNodeWithText("testuser").assertIsDisplayed()
    }
    
    @Test
    fun loginScreen_canInputPassword() {
        composeTestRule.setContent {
            Cst438Project2Theme {
                LoginScreenContent()
            }
        }

        // Input text into password field
        // Since it's a password field, visually the text transforms to dots, 
        // but performTextInput validates that the UI can receive the characters.
        composeTestRule.onNodeWithText("Password").performTextInput("securepassword")
    }
}
