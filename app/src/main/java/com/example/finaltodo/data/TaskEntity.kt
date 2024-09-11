package com.example.finaltodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val deadline: String
):Parcelable