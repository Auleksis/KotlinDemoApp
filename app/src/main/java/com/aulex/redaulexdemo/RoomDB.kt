package com.aulex.redaulexdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FeedbackEntity::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun feedbackDao(): FeedbackDAO

    companion object {
        @Volatile private var INSTANCE: RoomDB? = null

        fun getDB(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "feedback_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}