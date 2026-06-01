package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext

class FavoritosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

const val PREF_FAVORITOS = "favoritos_global"
const val KEY_FAVORITOS = "lista_favoritos"

fun getFavoritos(context: Context): MutableSet<String> {
    val prefs = context.getSharedPreferences(PREF_FAVORITOS, Context.MODE_PRIVATE)
    return prefs.getStringSet(KEY_FAVORITOS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
}

fun salvarFavoritos(context: Context, favoritos: Set<String>) {
    val prefs = context.getSharedPreferences(PREF_FAVORITOS, Context.MODE_PRIVATE)
    prefs.edit().putStringSet(KEY_FAVORITOS, favoritos).apply()
}

fun montarChaveFavorito(titulo: String, autor: String, imagemRes: Int, categoria: String): String {
    return "$titulo|||$autor|||$imagemRes|||$categoria"
}

data class LivroFavorito(
    val titulo: String,
    val autor: String,
    val imagemRes: Int,
    val categoria: String
)

fun parsearFavorito(raw: String): LivroFavorito? {
    val partes = raw.split("|||")
    if (partes.size != 4) return null
    return try {
        LivroFavorito(
            titulo = partes[0],
            autor = partes[1],
            imagemRes = partes[2].toInt(),
            categoria = partes[3]
        )
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    var favoritosRaw by remember {
        mutableStateOf(getFavoritos(context))
    }

    val livrosFavoritos = favoritosRaw.mapNotNull { parsearFavorito(it) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(78.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Favoritos",
                            color = RedDark,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(30.dp)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = RedDark,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                                containerColor = RedDark
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Página Inicial",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontFamily = FontFamily.SansSerif
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
                                            color = Color.White,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    },
                                    onClick = { menuExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Suporte",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontFamily = FontFamily.SansSerif
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(RedPrimary)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Meus Livros Favoritos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Toque na estrela em qualquer livro para salvar aqui!",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
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
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(color = RedDark.copy(alpha = 0.2f), thickness = 1.dp)

                    if (livrosFavoritos.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum favorito ainda.\nToque na ⭐ em qualquer livro para adicionar!",
                                color = RedDark,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(top = 10.dp)
                        ) {
                            items(livrosFavoritos, key = { it.titulo + it.categoria }) { livro ->
                                FavoritoCard(
                                    livro = livro,
                                    onRemover = {
                                        val chave = montarChaveFavorito(
                                            livro.titulo, livro.autor, livro.imagemRes, livro.categoria
                                        )
                                        val novo = getFavoritos(context)
                                        novo.remove(chave)
                                        salvarFavoritos(context, novo)
                                        favoritosRaw = novo
                                    },
                                    onImageClick = {
                                        val intent = Intent(context, ComentariosActivity::class.java)
                                        intent.putExtra("livroTitulo", livro.titulo)
                                        intent.putExtra("categoria", livro.categoria)
                                        intent.putExtra("livroImagem", livro.imagemRes)
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritoCard(livro: LivroFavorito, onRemover: () -> Unit, onImageClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, RedDark, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = livro.imagemRes),
                contentDescription = livro.titulo,
                modifier = Modifier
                    .width(60.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onImageClick() },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = livro.titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = RedDark
                )
                Text(
                    text = livro.autor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Gray
                )
                Text(
                    text = livro.categoria,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = RedPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onRemover) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover favorito",
                    tint = RedDark
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