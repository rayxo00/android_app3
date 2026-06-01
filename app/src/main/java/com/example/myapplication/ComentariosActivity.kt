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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val BabyPink = Color(0xFFFFB6C1)

fun isEmailValido(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

class ComentariosActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val libroTitulo = intent.getStringExtra("livroTitulo") ?: "Verity"
        val categoria = intent.getStringExtra("categoria") ?: "Geral"
        val libroImagem = intent.getIntExtra("livroImagem", R.drawable.verity)
        val libroDescricao = intent.getStringExtra("livroDescricao")
            ?: "Verity Lowen, uma escritora em crise, aceita terminar os livros de uma autora famosa, Verity Crawford."

        setContent {
            MyApplicationTheme {
                ComentariosScreen(libroTitulo, categoria, libroImagem, libroDescricao)
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
    val sharedPreferences = context.getSharedPreferences("comentarios", Context.MODE_PRIVATE)

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    var comentarios by remember {
        mutableStateOf(
            sharedPreferences.getStringSet(prefKey, mutableSetOf())?.toList() ?: emptyList()
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Comentários - $livroTitulo ($categoria)",
                        color = CardHeaderRed,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = CardHeaderRed
                        )
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RedPrimary)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCEEEF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
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
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bem-vindos à página de comentários e discussões do livro $livroTitulo.",
                            color = Color.DarkGray,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Resumo rápido",
                            color = CardHeaderRed,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = livroDescricao,
                            color = Color.DarkGray,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Comentários",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (comentarios.isEmpty()) {
                Text(
                    text = "Nenhum comentário ainda. Seja o primeiro a comentar!",
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            comentarios.forEach { comentarioItem ->
                val partes = comentarioItem.split("|||")
                val nomeAutor: String
                val dataComentario: String
                val textoComentario: String

                if (partes.size == 3) {
                    nomeAutor = partes[0]
                    dataComentario = partes[1]
                    textoComentario = partes[2]
                } else {
                    val partesAntigo = comentarioItem.split(":\n\"", limit = 2)
                    nomeAutor = partesAntigo.getOrNull(0) ?: "Usuário"
                    dataComentario = ""
                    textoComentario = partesAntigo.getOrNull(1)?.removeSuffix("\"") ?: comentarioItem
                }

                CommentItem(nome = nomeAutor, data = dataComentario, texto = textoComentario)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enviar um comentário",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            CommentInputField(
                label = "Nome",
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Seu nome"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                Text(
                    text = "E-mail",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "seu@gmail.com",
                            fontSize = 13.sp,
                            color = Color(0xFF555555),
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    singleLine = true,
                    isError = emailError,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CardHeaderRed,
                        unfocusedBorderColor = Color.Black,
                        errorBorderColor = Color.Red,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        errorContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        errorTextColor = Color.Black
                    ),
                    supportingText = {
                        if (emailError) {
                            Text(
                                text = "Por favor, insira um e-mail válido (ex: seu@email.com)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Comentário",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = {
                    Text(
                        "Escreva seu comentário aqui...",
                        fontSize = 13.sp,
                        color = Color(0xFF555555),
                        fontFamily = FontFamily.SansSerif
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardHeaderRed,
                    unfocusedBorderColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        nome.isBlank() -> scope.launch {
                            snackbarHostState.showSnackbar("Por favor, preencha seu nome.")
                        }
                        email.isBlank() -> scope.launch {
                            snackbarHostState.showSnackbar("Por favor, preencha seu e-mail.")
                        }
                        !isEmailValido(email) -> {
                            emailError = true
                        }
                        comentario.isBlank() -> scope.launch {
                            snackbarHostState.showSnackbar("Por favor, escreva seu comentário.")
                        }
                        else -> {
                            val dataAtual = SimpleDateFormat(
                                "dd/MM/yyyy HH:mm", Locale("pt", "BR")
                            ).format(Date())
                            val comentarioCompleto = "$nome|||$dataAtual|||$comentario"
                            val novaLista = comentarios.toMutableList()
                            novaLista.add(comentarioCompleto)
                            comentarios = novaLista
                            sharedPreferences.edit()
                                .putStringSet(prefKey, novaLista.toSet())
                                .apply()
                            nome = ""
                            email = ""
                            comentario = ""
                            emailError = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Comentário enviado com sucesso!")
                            }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = CardHeaderRed),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "Enviar comentário",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.SansSerif
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCEEEF)),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0B0B0))
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = nome,
                color = CardHeaderRed,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp
            )
            if (data.isNotBlank()) {
                Text(
                    text = data,
                    color = Color.Gray,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\"$texto\"",
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
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
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontSize = 17.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    fontSize = 13.sp,
                    color = Color(0xFF555555),
                    fontFamily = FontFamily.SansSerif
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CardHeaderRed,
                unfocusedBorderColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
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
            livroDescricao = "Verity Lowen, uma escritora em crise..."
        )
    }
}