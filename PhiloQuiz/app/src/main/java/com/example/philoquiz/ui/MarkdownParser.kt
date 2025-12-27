package com.example.philoquiz.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun parseMarkdownBold(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        val pattern = Regex("\\*\\*(.+?)\\*\\*")

        pattern.findAll(text).forEach { match ->
            // Добавляем текст до совпадения
            if (match.range.first > currentIndex) {
                append(text.substring(currentIndex, match.range.first))
            }
            // Добавляем жирный текст
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(match.groupValues[1])
            }
            currentIndex = match.range.last + 1
        }

        // Добавляем оставшийся текст
        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }
}
