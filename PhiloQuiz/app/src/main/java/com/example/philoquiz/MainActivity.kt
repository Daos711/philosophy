package com.example.philoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.philoquiz.data.Question
import com.example.philoquiz.data.QuestionRepository
import com.example.philoquiz.data.SettingsRepository
import com.example.philoquiz.ui.QuestionDetailScreen
import com.example.philoquiz.ui.QuestionListScreen
import com.example.philoquiz.ui.theme.PhiloQuizTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: QuestionRepository
    private lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = QuestionRepository(this)
        settingsRepository = SettingsRepository(this)

        setContent {
            var isDarkTheme by remember { mutableStateOf(settingsRepository.isDarkTheme) }
            var fontSize by remember { mutableIntStateOf(settingsRepository.fontSize) }

            PhiloQuizTheme(darkTheme = isDarkTheme) {
                PhiloQuizApp(
                    repository = repository,
                    isDarkTheme = isDarkTheme,
                    fontSize = fontSize,
                    onThemeChange = {
                        isDarkTheme = it
                        settingsRepository.isDarkTheme = it
                    },
                    onFontSizeChange = {
                        fontSize = it
                        settingsRepository.fontSize = it
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhiloQuizApp(
    repository: QuestionRepository,
    isDarkTheme: Boolean,
    fontSize: Int,
    onThemeChange: (Boolean) -> Unit,
    onFontSizeChange: (Int) -> Unit
) {
    var selectedQuestion by remember { mutableStateOf<Question?>(null) }
    var selectedSectionId by remember { mutableStateOf<Int?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showSettings by remember { mutableStateOf(false) }

    val sections = remember { repository.getSections() }
    var questions by remember { mutableStateOf(repository.loadQuestions()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectedQuestion == null) {
                TopAppBar(
                    title = { Text("PhiloQuiz") },
                    actions = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Настройки")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (selectedQuestion != null) {
            QuestionDetailScreen(
                question = selectedQuestion!!,
                fontSize = fontSize,
                onBack = { selectedQuestion = null }
            )
        } else {
            QuestionListScreen(
                questions = questions,
                sections = sections,
                selectedSectionId = selectedSectionId,
                searchQuery = searchQuery,
                onSectionSelect = { sectionId ->
                    selectedSectionId = sectionId
                    questions = repository.search(searchQuery, sectionId)
                },
                onQuestionClick = { selectedQuestion = it },
                onSearch = { query ->
                    searchQuery = query
                    questions = repository.search(query, selectedSectionId)
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    // Диалог настроек
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { showSettings = false },
            title = { Text("Настройки") },
            text = {
                Column {
                    // Тёмная тема
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Тёмная тема")
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onThemeChange
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Размер шрифта
                    Text("Размер шрифта: $fontSize")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onFontSizeChange(fontSize - 2) },
                            enabled = fontSize > 12
                        ) {
                            Text("A-")
                        }
                        Button(
                            onClick = { onFontSizeChange(16) }
                        ) {
                            Text("Сброс")
                        }
                        Button(
                            onClick = { onFontSizeChange(fontSize + 2) },
                            enabled = fontSize < 24
                        ) {
                            Text("A+")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettings = false }) {
                    Text("Готово")
                }
            }
        )
    }
}
