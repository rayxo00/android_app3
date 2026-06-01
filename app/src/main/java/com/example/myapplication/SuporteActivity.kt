package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

val TextRed = Color(0xFFC62828)
val CardContentPink = Color(0xFFFCE4EC)
val BackgroundCoral = Color(0xFFB05A5A)
val CardHeaderRed = Color(0xFFD32F2F)
val GoldStar = Color(0xFFFFD700)

class SuporteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                SuporteScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuporteScreen() {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var assunto by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Suporte",
                        color = RedDark,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = RedDark,
                        modifier = Modifier.padding(start = 35.dp)
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = RedDark,modifier = Modifier.size(35.dp))
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
                                        color = Color.White
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
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CardContentPink)
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
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Central de Suporte",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    "Estamos aqui para ajudar! Entre em contato conosco ou consulte nossas perguntas frequentes",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
                )
            }

            item {
                SuporteCard(title = "Envie sua mensagem") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SuporteTextField(
                            label = "Nome completo",
                            value = nome,
                            onValueChange = { nome = it },
                            placeholder = "Digite seu nome",
                            modifier = Modifier.testTag("nome_field")
                        )

                        Column(modifier = Modifier.testTag("email_field")) {
                            Text(
                                "E-mail",
                                color = RedDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    emailError = false
                                },
                                placeholder = { Text("seuemail@email.com", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = emailError,
                                supportingText = {
                                    if (emailError) {
                                        Text(
                                            "Por favor, insira um e-mail válido (ex: seu@email.com)",
                                            color = Color.Red,
                                            fontSize = 11.sp
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TextRed,
                                    unfocusedBorderColor = Color.Black,
                                    errorBorderColor = Color.Red,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    errorContainerColor = Color.White,
                                )
                            )
                        }

                        SuporteTextField(
                            label = "Assunto",
                            value = assunto,
                            onValueChange = { assunto = it },
                            placeholder = "Descreva sua mensagem aqui...",
                            modifier = Modifier.testTag("assunto_field"),
                            singleLine = false
                        )

                        Button(
                            onClick = {
                                when {
                                    nome.isBlank() -> scope.launch {
                                        snackbarHostState.showSnackbar("Por favor, preencha seu nome.")
                                    }
                                    email.isBlank() -> scope.launch {
                                        snackbarHostState.showSnackbar("Por favor, preencha seu e-mail.")
                                    }
                                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                        emailError = true
                                    }
                                    assunto.isBlank() -> scope.launch {
                                        snackbarHostState.showSnackbar("Por favor, descreva seu assunto.")
                                    }
                                    else -> {
                                        enviando = true
                                        scope.launch {
                                            val sucesso = sendEmail(nome, email, assunto)
                                            enviando = false
                                            snackbarHostState.showSnackbar(
                                                if (sucesso) "Mensagem enviada com sucesso!"
                                                else "Erro ao enviar mensagem."
                                            )
                                            if (sucesso) {
                                                nome = ""
                                                email = ""
                                                assunto = ""
                                                emailError = false
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enviar_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = RedDark),
                            enabled = !enviando
                        ) {
                            if (enviando) CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                            else Text("Enviar mensagem", color = Color.White)
                        }
                    }
                }
            }

            item {
                SuporteCard(title = "Perguntas Frequentes") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        FAQItem(
                            "Como funciona o site?",
                            "O Dreamy Pages é um blog literário onde você pode explorar diferentes gêneros de livros."
                        )
                        FAQItem(
                            "Vocês vendem livros?",
                            "Não vendemos livros diretamente, apenas fornecemos links."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuporteCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardContentPink)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                title,
                color = RedDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun SuporteTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            label,
            color = RedDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TextRed,
                unfocusedBorderColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            )
        )
    }
}

@Composable
fun FAQItem(pergunta: String, resposta: String) {
    Column {
        Text(pergunta, color = RedDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(resposta, color = Color.DarkGray, fontSize = 12.sp, lineHeight = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SuporteScreenPreview() {
    MyApplicationTheme { SuporteScreen() }
}

suspend fun sendEmail(nome: String, email: String, assunto: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val json = JSONObject().apply {
                put("service_id", "service_xkdvqgt")
                put("template_id", "template_slsvqwj")
                put("user_id", "xHEcg5CXdEWpsQfa8")
                put("template_params", JSONObject().apply {
                    put("nome", nome)
                    put("email", email)
                    put("mensagem", assunto)
                })
            }

            val body = json.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url("https://api.emailjs.com/api/v1.0/email/send")
                .addHeader("Content-Type", "application/json")
                .addHeader("origin", "http://localhost")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            android.util.Log.d("EmailJS", "Código: ${response.code} | Resposta: $responseBody")
            response.isSuccessful

        } catch (e: Exception) {
            android.util.Log.e("EmailJS", "Erro ao enviar: ${e.message}", e)
            false
        }
    }
}