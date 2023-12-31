package com.example.chat_bot.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chat_bot.Room.Coverters.ButtonConverter
import com.example.chat_bot.Room.Coverters.MaterialListConverter
import com.example.chat_bot.Room.Dao.SeedsDao
import com.example.chat_bot.Room.Entities.Alarms
import com.example.chat_bot.Room.Entities.SubjectCount
import com.example.chat_bot.data.QuestItem
import com.example.chat_bot.data.R_Message
import com.example.chat_bot.data.User


@Database(
    entities = [
        User::class,
        R_Message::class,
        QuestItem::class,
        Alarms::class,
        SubjectCount::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(value = [ButtonConverter::class, MaterialListConverter::class])
abstract class SeedsDatabase : RoomDatabase() {

    abstract val seedsDao: SeedsDao

    companion object {
        @Volatile
        private var INSTANCE: SeedsDatabase? = null

        fun getInstance(context: Context): SeedsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SeedsDatabase::class.java,
                    "seeds_db"
                )
                    .fallbackToDestructiveMigration() // Set destructive migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

