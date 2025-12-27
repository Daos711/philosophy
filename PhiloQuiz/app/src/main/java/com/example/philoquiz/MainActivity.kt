package com.example.philoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.philoquiz.data.Question
import com.example.philoquiz.data.QuestionRepository
import com.example.philoquiz.ui.QuestionDetailScreen
import com.example.philoquiz.ui.QuestionListScreen
import com.example.philoquiz.ui.theme.PhiloQuizTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: QuestionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = QuestionRepository(this)

        setContent {
            PhiloQuizTheme {
                PhiloQuizApp(repository)
            }
        }
    }
}

@Composable
fun PhiloQuizApp(repository: QuestionRepository) {
    var selectedQuestion by remember { mutableStateOf<Question?>(null) }
    var selectedSectionId by remember { mutableStateOf<Int?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val sections = remember { repository.getSections() }
    var questions by remember { mutableStateOf(repository.loadQuestions()) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (selectedQuestion != null) {
            QuestionDetailScreen(
                question = selectedQuestion!!,
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
}
