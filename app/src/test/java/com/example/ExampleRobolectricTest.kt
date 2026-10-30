package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.getBoundsInRoot
import androidx.test.core.app.ApplicationProvider
import com.example.ui.theme.DigitalSaathiTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Digital Saathi", appName)
  }

  @Test
  fun app_renders_correctly() {
    composeTestRule.setContent {
      DigitalSaathiTheme {
        DigitalSaathiApp(voiceHelper = null)
      }
    }
    composeTestRule.onNodeWithTag("brand_header").assertExists()
    composeTestRule.onNodeWithTag("user_profile_chip").assertExists()
    composeTestRule.onNodeWithTag("welcome_banner_card").assertExists()
    composeTestRule.onNodeWithTag("digital_sathi_bottom_nav").assertExists()
  }
}
