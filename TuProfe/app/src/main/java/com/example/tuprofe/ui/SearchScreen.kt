package com.example.tuprofe.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.SearchBar

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {

    var query by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {

        BackgroundImage()

        Column {

            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }


}

@Composable
fun SearchBar(
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    placeholder: String = "Busca a TuProfe"
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = colorResource(R.color.verdetp)
            )
        },
        shape = RoundedCornerShape(50.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = colorResource(R.color.verdetp)
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}

@Preview(showBackground = true)
@Composable
fun SearchFieldPreview() {
    var query by remember { mutableStateOf("") }

    SearchBar(
        query = query,
        onQueryChange = { query = it },
        modifier = Modifier.padding(16.dp)
    )
}