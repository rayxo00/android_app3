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

class ListaLivrosFiccaoCientificaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ListaLivrosFiccaoCientificaScreen()
            }
        }
    }
}

data class LivroFiccaoCientifica(
    val titulo: String,
    val autor: String,
    val descricao: String,
    val imagemRes: Int,
    val linkCompra: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivrosFiccaoCientificaScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    val livros = listOf(
        LivroFiccaoCientifica(
            "Duna",
            "Frank Herbert",
            "A vida do jovem Paul Atreides está prestes a mudar radicalmente. Após a visita de uma mulher misteriosa, ele é obrigado a deixar seu planeta natal para sobreviver ao ambiente árido de Arrakis, o Planeta Deserto. Envolvido numa intrincada teia política e religiosa, Paul divide-se entre as obrigações de herdeiro e seu treinamento nas doutrinas secretas de uma antiga irmandade.",
            R.drawable.duna,
            "https://www.amazon.com.br/Duna-Frank-Herbert/dp/857657313X"
        ),
        LivroFiccaoCientifica(
            "2001: Uma Odisséia no Espaço",
            "Arthur C. Clarke",
            "Nos primórdios da humanidade, um objeto inusitado chega à Terra e influencia os homens a descobrir coisas que permitem sua própria evolução. Milhões de anos depois, a descoberta de um monólito soterrado na Lua leva a Terra a enviar uma equipe ao espaço com HAL 9000, uma inteligência artificial que começa a apresentar falhas suspeitas.",
            R.drawable.odisseia_no_espaco,
            "https://www.amazon.com.br/2001-Uma-odiss%C3%A9ia-no-espa%C3%A7o/dp/8576571552"
        ),
        LivroFiccaoCientifica(
            "Eu, Robô",
            "Isaac Asimov",
            "Uma coleção de contos interligados que explora a evolução da robótica e as complexidades éticas da inteligência artificial através das histórias da robopsicóloga Susan Calvin. Centradas nas famosas Três Leis da Robótica, as histórias mostram como essas leis regem o comportamento dos robôs e as interações entre robôs e humanos.",
            R.drawable.robo,
            "https://www.amazon.com.br/Eu-Robo-Isaac-Asimov/dp/8576572001"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Livros de Ficção Científica",
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
                                text = {
                                    Text(
                                        "Página Inicial",
                                        fontWeight = FontWeight.Bold,
                                        color = TextRed
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
                                        color = TextRed
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
                                        color = TextRed
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
                FiccaoCientificaBookCard(livro) {
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
fun FiccaoCientificaBookCard(livro: LivroFiccaoCientifica, onBuyClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ComentariosActivity::class.java)
                intent.putExtra("livroTitulo", livro.titulo)
                intent.putExtra("categoria", "Ficção Científica")
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
                            intent.putExtra("categoria", "Ficção Científica")
                            intent.putExtra("livroImagem", livro.imagemRes)
                            intent.putExtra("livroDescricao", livro.descricao)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardContentPink),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("Comentários", color = CardHeaderRed, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onBuyClick,
                        modifier = Modifier.padding(top = 9.dp),
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
fun ListaLivrosFiccaoCientificaPreview() {
    MyApplicationTheme {
        ListaLivrosFiccaoCientificaScreen()
    }
}