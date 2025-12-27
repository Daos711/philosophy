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

    fun getCategories(): List<String> {
        return loadQuestions().map { it.category }.distinct()
    }

    fun getByCategory(category: String): List<Question> {
        return loadQuestions().filter { it.category == category }
    }

    fun search(query: String): List<Question> {
        val q = query.lowercase().trim()
        if (q.isEmpty()) return loadQuestions()

        return loadQuestions().filter { question ->
            question.question.lowercase().contains(q) ||
            question.answer.lowercase().contains(q) ||
            question.keywords.any { it.lowercase().contains(q) }
        }
    }

    fun getById(id: Int): Question? {
        return loadQuestions().find { it.id == id }
    }
}
