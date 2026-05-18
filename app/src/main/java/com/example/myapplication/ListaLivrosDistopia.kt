package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ListaLivrosDistopiaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ListaLivrosDistopiaScreen()
            }
        }
    }
}

data class LivroDistopia(
    val titulo: String,
    val autor: String,
    val descricao: String,
    val imagemRes: Int,
    val linkCompra: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivrosDistopiaScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    val livros = listOf(
        LivroDistopia(
            "Jogos Vorazes",
            "Suzanne Collins",
            "Em um futuro pós-apocalíptico, a nação de Panem é dividida em 12 distritos governados com mão de ferro pela Capital. Como punição por uma revolta passada, cada distrito deve enviar dois jovens para participar dos Jogos Vorazes, um reality show mortal onde apenas um sobrevive.",
            R.drawable.jogos_vorazes,
            "https://www.amazon.com.br/Jogos-Vorazes-Suzanne-Collins/dp/8579800242"
        ),
        LivroDistopia(
            "Estilhaça-me",
            "Tahereh Mafi",
            "Juliette Ferrars não toca em ninguém há 264 dias. Seu toque é fatal. O Restabelecimento a mantém presa, vendo-a como uma arma em potencial. No entanto, em um mundo em ruínas, Juliette descobre que seu poder pode ser a única esperança para a resistência.",
            R.drawable.estilhaca_me,
            "https://www.amazon.com.br/Estilha%C3%A7a-me-Tahereh-Mafi/dp/8531210870"
        ),
        LivroDistopia(
            "Divergente",
            "Veronica Roth",
            "Em uma Chicago futurista, a sociedade é dividida em cinco facções dedicadas a uma virtude. Aos 16 anos, Beatrice Prior deve escolher sua facção. No entanto, ela descobre que é uma Divergente, alguém que não se encaixa em apenas um grupo e que é vista como uma ameaça ao sistema.",
            R.drawable.divergente,
            "https://www.amazon.com.br/Divergente-Veronica-Roth/dp/8579801311"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Livros de Distopia",
                        fontSize = 18.sp,
                        color = CardHeaderRed,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = CardHeaderRed)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Página Inicial", fontWeight = FontWeight.Bold, color = TextRed) },
                                onClick = {
                                    menuExpanded = false
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    context.startActivity(intent)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Favoritos", fontWeight = FontWeight.Bold, color = TextRed) },
                                onClick = {
                                    menuExpanded = false
                                    val intent = Intent(context, FavoritosActivity::class.java)
                                    context.startActivity(intent)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Suporte", fontWeight = FontWeight.Bold, color = TextRed) },
                                onClick = {
                                    menuExpanded = false
                                    val intent = Intent(context, SuporteActivity::class.java)
                                    context.startActivity(intent)
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
                DistopiaBookCard(livro) {
                    if (livro.linkCompra.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(livro.linkCompra))
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun DistopiaBookCard(livro: LivroDistopia, onBuyClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ComentariosActivity::class.java)
                intent.putExtra("livroTitulo", livro.titulo)
                intent.putExtra("categoria", "Distopia")
                intent.putExtra("livroImagem", livro.imagemRes)
                intent.putExtra("livroDescricao", livro.descricao)
                context.startActivity(intent)
            },
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
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = livro.titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Autor: ${livro.autor}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = livro.descricao,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        color = Color.Black,
                        maxLines = 6
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(context, ComentariosActivity::class.java)
                            intent.putExtra("livroTitulo", livro.titulo)
                            intent.putExtra("categoria", "Distopia")
                            intent.putExtra("livroImagem", livro.imagemRes)
                            intent.putExtra("livroDescricao", livro.descricao)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(top = 9.dp, end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardContentPink),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("Comentários", color = CardHeaderRed, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onBuyClick,
                        modifier = Modifier.padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardHeaderRed),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text("Comprar", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaLivrosDistopiaPreview() {
    MyApplicationTheme {
        ListaLivrosDistopiaScreen()
    }
}
