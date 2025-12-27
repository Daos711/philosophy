package com.example.philoquiz.data

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val category: String,
    val question: String,
    val answer: String,
    val keywords: List<String> = emptyList()
)

@Serializable
data class QuestionsData(
    val questions: List<Question>
)
