package com.example.finaltodo

import java.time.LocalDate

data class Task(
    val title: String,
    val description: String,
    val deadline: LocalDate
)
