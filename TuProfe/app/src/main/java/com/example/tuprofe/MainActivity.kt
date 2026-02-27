package com.example.tuprofe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.ui.HomeScreen
import com.example.tuprofe.ui.RegisterScreen
import com.example.tuprofe.ui.ResetPasswordScreen
import com.example.tuprofe.ui.theme.TuProfeTheme
import com.example.tuprofe.ui.LoadingScreen
import com.example.tuprofe.ui.MainScreen
import com.example.tuprofe.ui.ProfeScreen
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.SearchBar
import com.example.tuprofe.ui.utils.TitleHeader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TuProfeTheme {
                TuProfeApp()
            }

        }
    }
}
