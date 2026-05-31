package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ListaLivrosTerrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyApplicationTheme { ListaLivrosTerrorScreen() } }
    }
}

data class LivroTerror(
    val titulo: String,
    val autor: String,
    val descricao: String,
    val imagemRes: Int,
    val linkCompra: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivrosTerrorScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    val livros = listOf(
        LivroTerror(
            "It - A Coisa", "Stephen King",
            "Um grupo de sete amigos em Derry luta contra uma entidade maligna que se manifesta na forma de seus maiores medos, principalmente o palhaço Pennywise. A história é contada em duas linhas do tempo: a infância em 1958 e a vida adulta em 1985.",
            R.drawable.it,
            "[amazon.com.br](https://www.amazon.com.br/coisa-livro-origem-s%C3%A9rie-Bem-vindos/dp/8560280944)"
        ),
        LivroTerror(
            "Drácula", "Bram Stoker",
            "Jonathan Harker viaja para a Transilvânia para finalizar a compra de uma propriedade pelo excêntrico Conde Drácula. Após descobrir que o conde é um vampiro, um grupo de heróis liderado pelo professor Van Helsing se une para caçá-lo em Londres.",
            R.drawable.dracula,
            "[amazon.com.br](https://www.amazon.com.br/Dr%C3%A1cula-Bram-Stoker/dp/6555520000)"
        ),
        LivroTerror(
            "O Exorcista", "William Peter Blatty",
            "Uma mãe percebe que sua filha de 12 anos está sendo possuída por uma entidade demoníaca. Desesperada, ela recorre a dois padres para realizar um exorcismo. Um dos livros de terror mais perturbadores já escritos.",
            R.drawable.exorcista,
            "[amazon.com.br](https://www.amazon.com.br/Exorcista-William-Peter-Blatty/dp/8595086230)"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Livros de Terror",
                        fontSize = 18.sp,
                        color = CardHeaderRed,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = CardHeaderRed)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            containerColor = CardHeaderRed
                        ) {
                            DropdownMenuItem(
                                text = { Text("Página Inicial", fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.SansSerif) },
                                onClick = {
                                    menuExpanded = false
                                    val i = Intent(context, MainActivity::class.java)
                                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    context.startActivity(i)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Favoritos", fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.SansSerif) },
                                onClick = {
                                    menuExpanded = false
                                    context.startActivity(Intent(context, FavoritosActivity::class.java))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Suporte", fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.SansSerif) },
                                onClick = {
                                    menuExpanded = false
                                    context.startActivity(Intent(context, SuporteActivity::class.java))
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardContentPink)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundCoral)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(livros) { livro ->
                TerrorBookCard(livro) {
                    if (livro.linkCompra.isNotEmpty())
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(livro.linkCompra)))
                }
            }
        }
    }
}

@Composable
fun TerrorBookCard(livro: LivroTerror, onBuyClick: () -> Unit) {
    val context = LocalContext.current
    val chave = montarChaveFavorito(livro.titulo, livro.autor, livro.imagemRes, "Terror")
    var isFavorito by remember { mutableStateOf(getFavoritos(context).contains(chave)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Image(
                painter = painterResource(id = livro.imagemRes),
                contentDescription = livro.titulo,
                modifier = Modifier.width(120.dp).height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            livro.titulo,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val favs = getFavoritos(context)
                                if (isFavorito) favs.remove(chave) else favs.add(chave)
                                salvarFavoritos(context, favs)
                                isFavorito = !isFavorito
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                if (isFavorito) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favoritar",
                                tint = if (isFavorito) Color(0xFFFFD700) else Color.Gray
                            )
                        }
                    }
                    Text("Autor: ${livro.autor}", fontSize = 12.sp, fontFamily = FontFamily.SansSerif, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(livro.descricao, fontSize = 11.sp, fontFamily = FontFamily.SansSerif, lineHeight = 14.sp, color = Color.Black, maxLines = 6)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = {
                            val i = Intent(context, ComentariosActivity::class.java)
                            i.putExtra("livroTitulo", livro.titulo)
                            i.putExtra("categoria", "Terror")
                            i.putExtra("livroImagem", livro.imagemRes)
                            i.putExtra("livroDescricao", livro.descricao)
                            context.startActivity(i)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CardContentPink),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) { Text("Comentários", color = CardHeaderRed, fontSize = 12.sp, maxLines = 1, fontFamily = FontFamily.SansSerif) }
                    Button(
                        onClick = onBuyClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CardHeaderRed),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) { Text("Comprar", color = Color.White, fontSize = 12.sp, maxLines = 1, fontFamily = FontFamily.SansSerif) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaLivrosTerrorPreview() { MyApplicationTheme { ListaLivrosTerrorScreen() } }
