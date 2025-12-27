package com.example.philoquiz.data

import android.content.Context
import kotlinx.serialization.json.Json

class QuestionRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private var questions: List<Question> = emptyList()

    fun loadQuestions(): List<Question> {
        if (questions.isEmpty()) {
            val jsonString = context.assets.open("questions.json")
                .bufferedReader()
                .use { it.readText() }
            questions = json.decodeFromString<QuestionsData>(jsonString).questions
        }
        return questions
    }

    fun getSections(): List<Pair<Int, String>> {
        return loadQuestions()
            .map { it.sectionId to it.section }
            .distinct()
            .sortedBy { it.first }
    }

    fun getBySection(sectionId: Int): List<Question> {
        return loadQuestions()
            .filter { it.sectionId == sectionId }
            .sortedBy { it.number }
    }

    fun search(query: String, sectionId: Int? = null): List<Question> {
        val q = query.lowercase().trim()
        var result = loadQuestions()

        // Фильтр по разделу
        if (sectionId != null) {
            result = result.filter { it.sectionId == sectionId }
        }

        // Поиск по номеру вопроса
        if (q.isNotEmpty()) {
            val numberQuery = q.toIntOrNull()
            if (numberQuery != null) {
                val byNumber = result.filter { it.number == numberQuery }
                if (byNumber.isNotEmpty()) return byNumber
            }

            // Поиск по тексту
            result = result.filter { question ->
                question.question.lowercase().contains(q) ||
                question.answer.lowercase().contains(q) ||
                question.keywords.any { it.lowercase().contains(q) }
            }
        }

        return result.sortedBy { it.number }
    }
}
