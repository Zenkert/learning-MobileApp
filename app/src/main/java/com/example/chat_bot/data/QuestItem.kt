package com.example.chat_bot.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.chat_bot.Room.Coverters.MaterialListConverter
import com.example.chat_bot.data.AllQuestion
import java.io.Serializable
@Entity
data class QuestItem  (
    @PrimaryKey(autoGenerate = true)
    val qiuzID:Int,
    val __v: Int,
    val _id: String,
    val ageGroup: String,
    val ageId: String,
    @TypeConverters(MaterialListConverter::class)
    val allQuestions: List<AllQuestion>,
    val country: String,
    val grade: String,
    val language: String,
    val noOfQuestions: String,
    val subId: String,
    var time: String,
    val access: Boolean,
    val accessCode: String,
    val topic: String,
    val userId: String,
    var username: String?
): Serializable

