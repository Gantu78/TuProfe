package com.example.tuprofe.ui.ayuda

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.HeaderSection
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.BackgroundImage

@Composable
fun AyudaYSoporteScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                HeaderSection(
                    title = stringResource(R.string.ayuda_y_soporte),
                    onBackClick = onBackClick
                )
            }



            item {
                SectionLabel(stringResource(R.string.faq))
                Spacer(Modifier.height(8.dp))
            }

            item {
                FaqItem(
                    question = stringResource(R.string.faq_q1),
                    answer = stringResource(R.string.faq_a1)
                )
            }
            item {
                FaqItem(
                    question = stringResource(R.string.faq_q2),
                    answer = stringResource(R.string.faq_a2)
                )
            }
            item {
                FaqItem(
                    question = stringResource(R.string.faq_q3),
                    answer = stringResource(R.string.faq_a3)
                )
            }
            item {
                FaqItem(
                    question = stringResource(R.string.faq_q4),
                    answer = stringResource(R.string.faq_a4)
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                SectionLabel(stringResource(R.string.terminos_y_condiciones))
                Spacer(Modifier.height(8.dp))
                InfoCard(
                    icon = Icons.Default.Info,
                    text = stringResource(R.string.terminos_desc)
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                SectionLabel(stringResource(R.string.contacto_soporte))
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:soporte@tuprofe.app")
                            putExtra(Intent.EXTRA_SUBJECT, "Soporte TuProfe")
                        }
                        context.startActivity(Intent.createChooser(intent, null))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, colorResource(R.color.verdetp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(R.color.verdetp)
                    )
                ) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.contacto_soporte), fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = colorResource(R.color.verdetp)
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(200)),
                exit = shrinkVertically(tween(180))
            ) {
                Text(
                    text = answer,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoCard(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = colorResource(R.color.verdetp))
            Spacer(Modifier.width(12.dp))
            Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
