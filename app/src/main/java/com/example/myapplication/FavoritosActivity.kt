package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.theme.MyApplicationTheme

class FavoritosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                FavoritosScreen()
            }
        }
    }
}

val RedPrimary = Color(0xFFE57373)
val RedDark = Color(0xFF8B0000)
val OffWhite = Color(0xFFFFF5F5)

data class Livro(val titulo: String, val autor: String, val nota: Int, val imagemRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }  // ← estado do menu

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favoritos", color = RedDark, fontSize = 20.sp) },
                navigationIcon = {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.padding(8.dp)
                    )
                },
                actions = {
                    Box {  // ← Box para ancorar o DropdownMenu
                        IconButton(onClick = { menuExpanded = true }) {  // ← abre o menu
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = RedDark)
                        }
                        DropdownMenu(  // ← menu dropdown igual ao Distopia
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Página Inicial",
                                        fontWeight = FontWeight.Bold,
                                        color = RedDark
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    context.startActivity(intent)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Favoritos",
                                        fontWeight = FontWeight.Bold,
                                        color = RedDark
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    // já está na tela de Favoritos, não navega
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Suporte",
                                        fontWeight = FontWeight.Bold,
                                        color = RedDark
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
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = OffWhite)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = RedDark
            ) {
                Text(
                    text = "© 2026 Dreamy Pages - Seu refúgio literário",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(RedPrimary)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Meus livros Favoritos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Salve aqui os livros que mais gostou para consultar depois!",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(2.dp, RedDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = OffWhite),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Lista de Favoritos",
                        color = RedDark,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(color = RedDark.copy(alpha = 0.2f), thickness = 1.dp)

                    val livrosFake = listOf(
                        Livro("Eu, robô", "Isaac Asimov", 0, R.drawable.robo),
                        Livro("Amor teoricamente", "Ali Hazelwood", 0, R.drawable.amor),
                        Livro("A Rainha Vermelha", "Victoria Aveyard", 0, R.drawable.rainha),
                        Livro("Divergente", "Veronica Roth", 0, R.drawable.divergente)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(livrosFake) { livro ->
                            BookCard(livro)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, RedDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = OffWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Como funciona?",
                        color = RedDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Para adicionar livros aos favoritos, visite as páginas de cada tema e clique no ícone de estrela. Seus favoritos ficarão salvos aqui!",
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BookCard(livro: Livro) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, RedDark, RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = livro.imagemRes),
                contentDescription = livro.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = livro.titulo,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = livro.autor,
                    fontSize = 9.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritosScreenPreview() {
    MyApplicationTheme {
        FavoritosScreen()
    }
}