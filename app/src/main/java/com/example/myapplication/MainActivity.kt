package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DreamyPagesScreen()
            }
        }
    }
}

@Composable
fun DreamyPagesScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RedPrimary)
    ) {
        DreamyPagesTopBar()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SobreNosSection()
            TemasDeLivrosSection()
            Spacer(modifier = Modifier.height(16.dp))
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = RedDark
        ) {
            Text(
                text = "©️ 2026 Dreamy Pages - Seu refúgio literário",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 10.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
fun DreamyPagesTopBar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = CardContentPink,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(70.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dreamy Pages",
                color = RedDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal
            )

            Box {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = RedDark,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { menuExpanded = true }
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    containerColor = RedDark
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Favoritos",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            val intent = Intent(context, FavoritosActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Suporte",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            val intent = Intent(context, SuporteActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RedDark)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardContentPink)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SobreNosSection(modifier: Modifier = Modifier) {
    InfoCard(
        title = "Sobre Nós",
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Bem-vindo ao Dreamy Pages, seu blog literário favorito!",
                color = RedDark,
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
                lineHeight = 18.sp
            )
            Text(
                text = "Aqui você encontra resenhas, dicas e novidades sobre os melhores livros.",
                color = RedDark,
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
                lineHeight = 18.sp
            )
            Text(
                text = "Nossa missão é aproximar leitores de histórias incríveis.",
                color = RedDark,
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun TemasDeLivrosSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val genreMap = mapOf(
        "Suspense/Mistério" to ListaLivrosSuspenseActivity::class.java,
        "Romance" to ListaLivrosRomanceActivity::class.java,
        "Fantasia" to ListaLivrosFantasiaActivity::class.java,
        "Terror" to ListaLivrosTerrorActivity::class.java,
        "Distopia" to ListaLivrosDistopiaActivity::class.java,
        "Ficção Científica" to ListaLivrosFiccaoCientificaActivity::class.java
    )

    InfoCard(
        title = "Temas de Livros",
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            genreMap.forEach { (genre, activityClass) ->
                Text(
                    text = genre,
                    color = RedDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(context, activityClass)
                            context.startActivity(intent)
                        }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DreamyPagesScreenPreview() {
    MyApplicationTheme {
        DreamyPagesScreen()
    }
}