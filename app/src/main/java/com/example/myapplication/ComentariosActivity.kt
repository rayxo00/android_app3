package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class ComentariosActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val libroTitulo =
            intent.getStringExtra("livroTitulo") ?: "Verity"

        val categoria =
            intent.getStringExtra("categoria") ?: "Geral"

        val libroImagem =
            intent.getIntExtra("livroImagem", R.drawable.verity)

        val libroDescricao =
            intent.getStringExtra("livroDescricao") ?: "Verity Lowen, uma escritora em crise, aceita terminar os livros de uma autora famosa, Verity Crawford. Ao investigar seus manuscritos, descobre um diário perturbador que revela segredos sombrios sobre Verity e sua família - misturando amor, obsessão e suspense psicológico."

        setContent {
            MyApplicationTheme {
                ComentariosScreen(
                    libroTitulo,
                    categoria,
                    libroImagem,
                    libroDescricao
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComentariosScreen(
    livroTitulo: String,
    categoria: String,
    livroImagem: Int,
    livroDescricao: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val prefKey = "$categoria-$livroTitulo"

    val sharedPreferences =
        context.getSharedPreferences(
            "comentarios",
            Context.MODE_PRIVATE
        )

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }

    var comentarios by remember {
        mutableStateOf(
            sharedPreferences.getStringSet(
                prefKey,
                mutableSetOf()
            )?.toList() ?: emptyList()
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Comentários - $livroTitulo ($categoria)",
                        color = CardHeaderRed,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            (context as? ComponentActivity)?.finish()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = CardHeaderRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardContentPink
                )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundCoral)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFCEEEF)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = livroImagem),
                        contentDescription = livroTitulo,
                        modifier = Modifier
                            .width(100.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Publicado por Dreamy Pages - 05/11/2023",
                            color = CardHeaderRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Bem-vindos à página de comentários e discussões do livro $livroTitulo. Aqui você pode ler opiniões de exemplo, comentar e compartilhar suas impressões.",
                            color = Color.DarkGray,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Resumo rápido",
                            color = CardHeaderRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = livroDescricao,
                            color = Color.DarkGray,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Comentários",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CardHeaderRed
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (comentarios.isEmpty()) {
                Text(
                    text = "Nenhum comentário ainda. Seja o primeiro a comentar!",
                    color = Color.Black,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            comentarios.forEach { comentarioItem ->
                val partes = comentarioItem.split(":\n\"", limit = 2)
                val nomeAutor = partes.getOrNull(0) ?: "Usuário"
                val textoComentario = partes.getOrNull(1)?.removeSuffix("\"") ?: comentarioItem

                CommentItem(
                    nome = nomeAutor,
                    data = "Recente",
                    texto = textoComentario
                )
            }


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enviar um comentário",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CardHeaderRed
            )

            Spacer(modifier = Modifier.height(12.dp))

            CommentInputField(
                label = "Nome",
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Seu nome"
            )

            Spacer(modifier = Modifier.height(10.dp))

            CommentInputField(
                label = "E-mail",
                value = email,
                onValueChange = { email = it },
                placeholder = "seu@gmail.com"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Comentário",
                color = CardHeaderRed,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = { Text("Escreva seu comentário aqui...", fontSize = 13.sp) },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardHeaderRed,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nome.isNotBlank() && comentario.isNotBlank()) {
                        val comentarioCompleto = "$nome:\n\"$comentario\""
                        val novaLista = comentarios.toMutableList()
                        novaLista.add(comentarioCompleto)
                        comentarios = novaLista
                        sharedPreferences.edit()
                            .putStringSet(prefKey, novaLista.toSet())
                            .apply()
                        nome = ""
                        email = ""
                        comentario = ""
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardHeaderRed
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "Enviar comentário",
                    color = Color.White,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CommentItem(nome: String, data: String, texto: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFCEEEF)
        ),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0B0B0)))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "$nome - $data",
                color = CardHeaderRed,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\"$texto\"",
                color = Color.Black,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun CommentInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            color = CardHeaderRed,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CardHeaderRed,
                unfocusedBorderColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComentariosScreenPreview() {
    MyApplicationTheme {
        ComentariosScreen(
            livroTitulo = "Verity",
            categoria = "Suspense",
            livroImagem = R.drawable.verity,
            livroDescricao = "Verity Lowen, uma escritora em crise, aceita terminar os livros de uma autora famosa, Verity Crawford. Ao investigar seus manuscritos, descobre um diário perturbador que revela segredos sombrios sobre Verity e sua família - misturando amor, obsessão e suspense psicológico."
        )
    }
    MyApplicationTheme {
        ComentariosScreen(
            livroTitulo = "A hipotese do amor",
            categoria = "romance",
            livroImagem = R.drawable.hipotese,
            livroDescricao = "Uma estudante de doutorado, Olive, que finge um namoro com um professor renomado, Adam, para convencer sua melhor amiga de que está feliz no amor. O que começa como uma farsa científica se transforma em um romance real."
        )
    }
    MyApplicationTheme {
        ComentariosScreen(
            livroTitulo = "Amor teoricamente",
            categoria = "romance",
            livroImagem = R.drawable.amor,
            livroDescricao = "Elsie Hannaway finge ser namorada de aluguel para complementar a renda. Sua vida 'fake' colide com Jack Smith, um físico experimental e possível obstáculo para o emprego dos seus sonhos no MIT."
        )
    }
    MyApplicationTheme {
        ComentariosScreen(
            livroTitulo = "O acordo ",
            categoria = "romance",
            livroImagem = R.drawable.acordo,
            livroDescricao = "Hannah Wells não se interessa por Garret Graham, o capitão do time de hóquei. Para não ser expulso, Garret concorda em ajudar Hannah a fazer ciúmes no rapaz por quem ela é apaixonada."
        )
    }
}
