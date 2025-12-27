package com.example.philoquiz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.philoquiz.data.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListScreen(
    questions: List<Question>,
    onQuestionClick: (Question) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        // Поиск
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Поиск по вопросам...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            singleLine = true
        )

        // Список вопросов
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions) { question ->
                QuestionItem(
                    question = question,
                    onClick = { onQuestionClick(question) }
                )
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = question.category,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
