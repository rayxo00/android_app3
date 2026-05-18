package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test

class SuporteActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun suporteScreen_displaysTitle() {
        composeTestRule.setContent {
            MyApplicationTheme {
                SuporteScreen()
            }
        }

        composeTestRule.onNodeWithText("Central de Suporte").assertIsDisplayed()
    }

    @Test
    fun suporteScreen_formFieldsAreDisplayed() {
        composeTestRule.setContent {
            MyApplicationTheme {
                SuporteScreen()
            }
        }

        composeTestRule.onNodeWithTag("nome_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("email_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("assunto_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("enviar_button").assertIsDisplayed()
    }

    @Test
    fun suporteScreen_fillFormAndButtonEnabled() {
        composeTestRule.setContent {
            MyApplicationTheme {
                SuporteScreen()
            }
        }

        // Initially button should be enabled (since it's not "enviando")
        // The condition in onClick is what prevents sending, but the button itself is enabled unless enviando is true.
        composeTestRule.onNodeWithTag("enviar_button").assertIsEnabled()

        // Fill the form
        composeTestRule.onNodeWithTag("nome_field").performTextInput("Test User")
        composeTestRule.onNodeWithTag("email_field").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("assunto_field").performTextInput("This is a test message.")
        
        // We don't click here because it would trigger a real network call in the current implementation.
    }

    @Test
    fun suporteScreen_faqItemsAreDisplayed() {
        composeTestRule.setContent {
            MyApplicationTheme {
                SuporteScreen()
            }
        }

        composeTestRule.onNodeWithText("? Perguntas Frequentes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Como funciona o site?").assertIsDisplayed()
    }
}
