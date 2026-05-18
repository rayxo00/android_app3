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

class ListaLivrosFantasiaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ListaLivrosFantasiaScreen()
            }
        }
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
            "A Rainha Vermelha",
            "Victoria Aveyard",
            "O mundo de Mare Barrow é dividido pelo sangue: vermelho ou prateado. Mare é vermelha: plebeia, destinada a servir uma elite prateada com poderes sobrenaturais. Ao conseguir um emprego no palácio real, ela descobre ter um poder misterioso. Em meio às intrigas dos nobres, suas ações desencadearão uma dança violenta e fatal.",
            R.drawable.rainha,
            "https://www.amazon.com.br/rainha-vermelha-Victoria-Aveyard/dp/8565765695"
        ),
        LivroFantasia(
            "De Sangue e Cinzas",
            "Jennifer L. Armentrout",
            "Poppy é a Donzela, destinada a ser entregue aos deuses e a salvar o reino de Solis. Privada de todas as escolhas e obrigada a cobrir o rosto com um véu, ela leva uma vida solitária. Com a entrada de Hawke Flynn em sua vida, o mundo de Poppy vira de cabeça para baixo e ela é lançada em uma rede de mentiras, traições e desejo.",
            R.drawable.de_sangue_e_cinzas,
            "https://www.amazon.com.br/sangue-cinzas-Vol-1/dp/655981002X"
        ),
        LivroFantasia(
            "O Príncipe Cruel",
            "Holly Black",
            "Jude tinha apenas sete anos quando seus pais foram assassinados e ela foi levada para viver no Reino das Fadas. Dez anos depois, ela quer se encaixar, mas enfrenta o desprezo dos feéricos, especialmente do príncipe Cardan. Ao se envolver nas intrigas do palácio, Jude descobre sua vocação para trapaças e terá que arriscar tudo para salvar suas irmãs e o reino.",
            R.drawable.principe_cruel,
            "https://www.amazon.com.br/pr%C3%ADncipe-cruel-Vol-Povo-Ar/dp/850111555X/ref=pd_lpo_d_sccl_1/145-1601141-1088458?pd_rd_w=rxk8b&content-id=amzn1.sym.a2197dac-0fbe-4cc8-beca-b52f96ea33d5&pf_rd_p=a2197dac-0fbe-4cc8-beca-b52f96ea33d5&pf_rd_r=VTKVSC2B985VBH7NCEAT&pd_rd_wg=I6qK4&pd_rd_r=4355c4f8-0dca-4191-8cd4-8743eb771dfe&pd_rd_i=850111555X&psc=1"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Livros de Fantasia",
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
                FantasiaBookCard(livro) {
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
fun FantasiaBookCard(livro: LivroFantasia, onBuyClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ComentariosActivity::class.java)
                intent.putExtra("livroTitulo", livro.titulo)
                intent.putExtra("categoria", "Fantasia")
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
                            intent.putExtra("categoria", "Fantasia")
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
fun ListaLivrosFantasiaPreview() {
    MyApplicationTheme {
        ListaLivrosFantasiaScreen()
    }
}