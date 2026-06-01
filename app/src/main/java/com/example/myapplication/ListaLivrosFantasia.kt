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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ListaLivrosFantasiaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyApplicationTheme { ListaLivrosFantasiaScreen() } }
    }
}

data class LivroFantasia(
    val titulo: String,
    val autor: String,
    val descricao: String,
    val imagemRes: Int,
    val linkCompra: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivrosFantasiaScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    val livros = listOf(
        LivroFantasia(
            "A Rainha Vermelha", "Victoria Aveyard",
            "O mundo de Mare Barrow é dividido pelo sangue: vermelho ou prateado. Mare é vermelha: plebeia, destinada a servir uma elite prateada com poderes sobrenaturais. Ao conseguir um emprego no palácio real, ela descobre ter um poder misterioso. Em meio às intrigas dos nobres, suas ações desencadearão uma dança violenta e fatal.",
            R.drawable.rainha,
            "https://www.amazon.com.br/rainha-vermelha-Victoria-Aveyard/dp/8565765695"
        ),
        LivroFantasia(
            "De Sangue e Cinzas", "Jennifer L. Armentrout",
            "Poppy é a Donzela, destinada a ser entregue aos deuses e a salvar o reino de Solis. Privada de todas as escolhas e obrigada a cobrir o rosto com um véu, ela leva uma vida solitária. Com a entrada de Hawke Flynn em sua vida, o mundo de Poppy vira de cabeça para baixo e ela é lançada em uma rede de mentiras, traições e desejo.",
            R.drawable.de_sangue_e_cinzas,
            "https://www.amazon.com.br/sangue-cinzas-Vol-1/dp/655981002X"
        ),
        LivroFantasia(
            "O Príncipe Cruel", "Holly Black",
            "Jude tinha apenas sete anos quando seus pais foram assassinados e ela foi levada para viver no Reino das Fadas. Dez anos depois, ela quer se encaixar, mas enfrenta o desprezo dos feéricos, especialmente do príncipe Cardan. Ao se envolver nas intrigas do palácio, Jude descobre sua vocação para trapaças e terá que arriscar tudo para salvar suas irmãs e o reino.",
            R.drawable.principe_cruel,
            "https://www.amazon.com.br/pr%C3%ADncipe-cruel-Vol-Povo-Ar/dp/850111555X"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Livros de Fantasia",
                        fontSize = 18.sp,
                        color = RedDark,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = RedDark,  modifier = Modifier.size(35.dp))
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            containerColor = RedDark
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(RedPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(livros) { livro ->
                FantasiaBookCard(livro) {
                    if (livro.linkCompra.isNotEmpty())
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(livro.linkCompra)))
                }
            }
        }
    }
}

@Composable
fun FantasiaBookCard(livro: LivroFantasia, onBuyClick: () -> Unit) {
    val context = LocalContext.current
    val chave = montarChaveFavorito(livro.titulo, livro.autor, livro.imagemRes, "Fantasia")
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
                            i.putExtra("categoria", "Fantasia")
                            i.putExtra("livroImagem", livro.imagemRes)
                            i.putExtra("livroDescricao", livro.descricao)
                            context.startActivity(i)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CardContentPink),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) { Text("Comentários", color = RedDark, fontSize = 12.sp, maxLines = 1, fontFamily = FontFamily.SansSerif) }
                    Button(
                        onClick = onBuyClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = RedDark),
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
fun ListaLivrosFantasiaPreview() { MyApplicationTheme { ListaLivrosFantasiaScreen() } }