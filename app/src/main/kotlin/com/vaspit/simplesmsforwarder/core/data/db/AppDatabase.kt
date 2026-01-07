package com.vaspit.simplesmsforwarder.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vaspit.simplesmsforwarder.core.data.dao.SmsDao
import com.vaspit.simplesmsforwarder.core.data.model.SmsEntity

@Database(
    entities = [SmsEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun smsDao(): SmsDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build().also { INSTANCE = it }
            }
    }
}