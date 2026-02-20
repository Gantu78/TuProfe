package com.example.tuprofe.ui



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ConfigItem
import com.example.tuprofe.ui.utils.HeaderSection


@Composable
fun ConfigScreen(
    modifier: Modifier = Modifier
) {


        Box(
            modifier = modifier
                .fillMaxSize()

        ) {

            BackgroundImage()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp),
                contentPadding = PaddingValues(
                    top = 40.dp,
                    bottom = 120.dp
                )
            ) {

                item {
                    HeaderSection(title = "Configuración",
                        showSearchBar = false,
                        onBackClick = null)
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    UserCard(
                        username = "Gantu870",
                        email = "c.jimenez@javeriana.edu.co"
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    ConfigItem(
                        icon = Icons.Default.Star,
                        title = "Historial de calificaciones",
                        subtitle = "¿Qué profes has calificado?"
                    )
                }

                item {
                    ConfigItem(
                        icon = Icons.Default.MailOutline,
                        title = "Ayuda y Soporte",
                        subtitle = "FAQ, Términos y condiciones"
                    )
                }

                item {
                    ConfigItem(
                        icon = Icons.Default.Star,
                        title = "Privacidad",
                        subtitle = "Perfil Anónimo, Visibilidad..."
                    )
                }

                item {
                    ConfigItem(
                        icon = Icons.Default.Star,
                        title = "Notificaciones",
                        subtitle = "Alertas y novedades"
                    )
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }

                item {
                    AppButton(
                        textoBoton = "CERRAR SESIÓN",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    )
                }


            }
        }
    }


@Composable
fun UserCard(
    username: String,
    email: String
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.pastel)
        )
    ) {
        Row (
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = null,
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column() {
                Text(username, fontWeight = FontWeight.Bold)
                Text(email, color = Color.Gray)
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun ConfigScreenPreview() {
    ConfigScreen()
}

