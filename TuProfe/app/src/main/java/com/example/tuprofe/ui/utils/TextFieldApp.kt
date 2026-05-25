package com.example.tuprofe.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.tuprofe.R

@Composable
fun TextFieldApp(
    texto: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(texto) },
        modifier = modifier,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(25.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedBorderColor = colorResource(R.color.BordeTuProfe),
            focusedBorderColor = colorResource(R.color.verdetp),
            unfocusedLabelColor = colorResource(R.color.gris),
            focusedLabelColor = colorResource(R.color.verdetp)
        )
    )
}
