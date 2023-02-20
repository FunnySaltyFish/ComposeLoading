package com.funny.compose.loading

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.funny.compose.loading.ui.Catalog
import com.funny.compose.loading.ui.theme.ComposeLoadingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLoadingTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    Catalog()
                }
            }
        }
    }
}

