package com.example.chat_bot.Activities.Notification.Activity.repository

import com.example.chat_bot.Room.Entities.Alarms
import com.example.chat_bot.Room.SeedsDatabase


class AlarmRepository(private val db: SeedsDatabase) {
    suspend fun insert(alarm: Alarms) = db.seedsDao.insert(alarm)
    suspend fun update(alarm: Alarms) = db.seedsDao.update(alarm)
    suspend fun delete(alarm: Alarms) = db.seedsDao.delete(alarm)
}