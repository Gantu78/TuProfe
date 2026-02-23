package com.example.tuprofe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TuProfeTheme {
                Scaffold(
                    topBar = {
                        HeaderSection(
                            title = stringResource(R.string.tuprofe)
                        )
                    }
                ) {

                    MainScreen (
                        modifier = Modifier.padding(it))

                }

            }

        }
    }
}

@Composable
fun HeaderSection(
    title: String,
    showSearchBar: Boolean = true,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {



    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onBackClick != null) {
            BackButtonHeader(onBackClick = onBackClick)
        }

        if (showSearchBar) {
            SearchBar()
            Spacer(modifier = Modifier.height(20.dp))
        }

        TitleHeader(title = title)

        Spacer(modifier = Modifier.height(20.dp))
    }
}
