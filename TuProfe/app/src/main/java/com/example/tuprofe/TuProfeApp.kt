package com.example.tuprofe

import android.annotation.SuppressLint
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.compose.rememberNavController
import com.example.tuprofe.navegation.AppNavegation
import com.example.tuprofe.ui.MainScreen
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.SearchBar
import com.example.tuprofe.ui.utils.TitleHeader
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors



@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun TuProfeApp(
    onHomeClick: () -> Unit,
    onTeachersClick: () -> Unit,
    onProfileClick: () -> Unit
){
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {

            HeaderSection(
                title = stringResource(R.string.tuprofe)


            )

        },
        bottomBar = {
            BottomBar(
                on1Click = onHomeClick,
                on2Click = onTeachersClick,
                on3Click = onProfileClick

            )
        }
    ) { paddingValues ->

    val navController = rememberNavController()

        AppNavegation(
            navController = navController,
            modifier = Modifier.fillMaxSize()

        )

    }
}
@Preview()
@Composable
fun TuProfeAppPreview(){
    TuProfeApp(
        onHomeClick = {},
        onTeachersClick = {},
        onProfileClick = {}
    )
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

@Composable
fun BottomBar(
    on1Click: () -> Unit,
    on2Click: () -> Unit,
    on3Click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.verdetp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = on1Click,
                modifier = Modifier.weight(1f)
            ) {
                Text("Incio", color = Color.White, fontSize = 16.sp)
            }
            VerticalDivider(
                modifier = Modifier.height(30.dp),
                thickness = 1.dp,
                color = Color.White
            )
            TextButton(
                onClick = on2Click,
                modifier = Modifier.weight(1f)
            ) {
                Text("Profesores", color = Color.White, fontSize = 16.sp)
            }
            VerticalDivider(
                modifier = Modifier.height(30.dp),
                thickness = 1.dp,
                color = Color.White
            )
            TextButton(
                onClick = on3Click,
                modifier = Modifier.weight(1f)
            ) {
                Text("Perfil", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}