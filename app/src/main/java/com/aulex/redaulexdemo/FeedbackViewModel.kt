package com.aulex.redaulexdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedbackViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = RoomDB.getDB(application).feedbackDao()
    val allFeedbackEntries: Flow<List<FeedbackEntity>> = dao.getAllFeedback()

    fun saveFeedback(rate: Int, feedback: String) {
        viewModelScope.launch {
            dao.insertFeedback(FeedbackEntity(rate = rate, feedback = feedback))
        }
    }
}