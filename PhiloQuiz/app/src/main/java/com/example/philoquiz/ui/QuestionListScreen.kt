package com.example.philoquiz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.philoquiz.data.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListScreen(
    questions: List<Question>,
    sections: List<Pair<Int, String>>,
    selectedSectionId: Int?,
    onSectionSelect: (Int?) -> Unit,
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Поиск (номер или текст)...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = ""
                        onSearch("")
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            },
            singleLine = true
        )

        // Вкладки разделов
        ScrollableTabRow(
            selectedTabIndex = if (selectedSectionId == null) 0 else sections.indexOfFirst { it.first == selectedSectionId } + 1,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 8.dp
        ) {
            Tab(
                selected = selectedSectionId == null,
                onClick = { onSectionSelect(null) },
                text = { Text("Все", maxLines = 1) }
            )
            sections.forEach { (id, title) ->
                Tab(
                    selected = selectedSectionId == id,
                    onClick = { onSectionSelect(id) },
                    text = {
                        Text(
                            "Р$id",
                            maxLines = 1
                        )
                    }
                )
            }
        }

        // Название выбранного раздела
        if (selectedSectionId != null) {
            val sectionTitle = sections.find { it.first == selectedSectionId }?.second ?: ""
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Р${question.sectionId}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "№${question.number}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
