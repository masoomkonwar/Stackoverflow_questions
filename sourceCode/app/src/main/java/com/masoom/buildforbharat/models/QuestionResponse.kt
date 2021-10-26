package com.masoom.buildforbharat.models

data class QuestionResponse(
    val has_more: Boolean,
    val items: List<Item>,
    val quota_max: Int,
    val quota_remaining: Int
)