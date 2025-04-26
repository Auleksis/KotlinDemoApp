package com.aulex.redaulexdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FeedbackDAO {
    @Insert
    suspend fun insertFeedback(feedback: FeedbackEntity)

    @Query("SELECT * FROM feedback")
    fun getAllFeedback(): kotlinx.coroutines.flow.Flow<List<FeedbackEntity>>
}