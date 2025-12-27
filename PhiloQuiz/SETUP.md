# Инструкция по настройке PhiloQuiz

## 1. Добавь зависимость в build.gradle.kts (Module: app)

Открой файл `app/build.gradle.kts` и добавь в секцию `plugins`:

```kotlin
plugins {
    // ... существующие плагины
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}
```

И в секцию `dependencies`:

```kotlin
dependencies {
    // ... существующие зависимости
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
```

## 2. Скопируй файлы в проект

Скопируй эти файлы из папки PhiloQuiz в соответствующие папки твоего проекта:

```
app/src/main/assets/questions.json → assets/questions.json
app/src/main/java/com/example/philoquiz/data/Question.kt
app/src/main/java/com/example/philoquiz/data/QuestionRepository.kt
app/src/main/java/com/example/philoquiz/ui/QuestionListScreen.kt
app/src/main/java/com/example/philoquiz/ui/QuestionDetailScreen.kt
app/src/main/java/com/example/philoquiz/MainActivity.kt (замени существующий)
```

## 3. Создай папку assets

В Android Studio:
- ПКМ на `app/src/main`
- New → Directory → `assets`
- Положи туда `questions.json`

## 4. Sync Gradle

После всех изменений нажми "Sync Now" в Android Studio.

## 5. Запуск

Нажми зелёную кнопку Run (▶) или Shift+F10
