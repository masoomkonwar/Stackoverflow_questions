package com.masoom.buildforbharat.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.masoom.buildforbharat.UI.QuestionsViewModel
import com.masoom.buildforbharat.repository.QuestionsRepository

class QuestionViewModelProviderFactory(val repository: QuestionsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsViewModel(repository) as T
    }
}