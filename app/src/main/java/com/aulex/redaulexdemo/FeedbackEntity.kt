package com.aulex.redaulexdemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class FeedbackEntity (
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val rate: Int,
    val feedback: String
)