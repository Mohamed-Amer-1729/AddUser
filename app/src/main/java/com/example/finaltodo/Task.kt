package com.example.finaltodo

import java.time.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val deadline: LocalDate
)
