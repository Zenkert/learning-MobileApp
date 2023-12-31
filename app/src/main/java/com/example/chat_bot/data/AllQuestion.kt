package com.example.chat_bot.data


import java.io.Serializable


data class AllQuestion(
    val __v: Int?,
    val _id: String,
    val answer: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val answer5: String,
    val link: String,
    val file: String,
    val introduction: String,
    val mcqs: String,
    val negFeedback: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val posFeedback: String,
    val question: String,
    val questionType: String,
    val sequence: Int,
    val statement1: String,
    val statement2: String,
    val statement3: String,
    val statement4: String,
    val statement5: String,
    val topicId: String,
    val userId: String,
    var q_type: Int
): Serializable