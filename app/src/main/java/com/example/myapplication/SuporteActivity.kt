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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Suporte",
                        color = TextRed,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = TextRed,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextRed)
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
                                    // já está na tela de Suporte, não navega
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CardContentPink)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundCoral)
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
                        SuporteTextField(
                            label = "E-mail",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "seuemail@email.com",
                            modifier = Modifier.testTag("email_field")
                        )
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
                                if (nome.isNotBlank() && email.isNotBlank() && assunto.isNotBlank()) {
                                    enviando = true
                                    scope.launch {
                                        val sucesso = sendEmail(nome, email, assunto)
                                        enviando = false
                                        snackbarHostState.showSnackbar(
                                            if (sucesso) "Mensagem enviada com sucesso!"
                                            else "Erro ao enviar mensagem."
                                        )
                                        if (sucesso) { nome = ""; email = ""; assunto = "" }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enviar_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CardHeaderRed),
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
                color = TextRed,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
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
            color = TextRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 12.sp)  },
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
        Text(pergunta, color = TextRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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