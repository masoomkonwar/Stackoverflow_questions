package com.masoom.buildforbharat.UI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masoom.buildforbharat.models.Item
import com.masoom.buildforbharat.models.QuestionResponse
import com.masoom.buildforbharat.repository.QuestionsRepository
import com.masoom.buildforbharat.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.properties.Delegates

class QuestionsViewModel(val repository: QuestionsRepository) : ViewModel(){
    val questions : MutableLiveData<Resource<QuestionResponse>> = MutableLiveData()
    var avgView : Int = 0
    var avgAns : Int = 0

    val searchQuestions : MutableLiveData<Resource<QuestionResponse>> = MutableLiveData()
    init {
        getAllQuestions()
    }
    fun getAllQuestions(order: String = "asc",sort : String = "activity") = viewModelScope.launch {
        questions.postValue(Resource.Loading())
        val quest = repository.getAllQuestions(order,sort)
        val senItem = quest.body()?.items
        senItem?.let {
            avgView = getAvgViewCount(senItem)
            avgAns = getAvgAnsCount(senItem)
        }

        questions.postValue(handleQuestions(quest))

    }
    fun getSearchQuestion(query : String){

    }

    private  fun handleQuestions(response: Response<QuestionResponse>): Resource<QuestionResponse>
    {
        if (response.isSuccessful){
            response.body()?.let { resultResponse->

                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
      fun getAvgViewCount(items : List<Item> ) : Int{
        var sum : Int =0
        var itr = items.listIterator()
        while (itr.hasNext())
        {
            sum = sum + itr.next().view_count
        }
        return sum/items.size
    }
      fun getAvgAnsCount(items : List<Item>) :Int{
        var sum : Int =0
        var itr = items.listIterator()
        while (itr.hasNext())
        {
            sum = sum + itr.next().answer_count
        }
        return sum/items.size
    }



}