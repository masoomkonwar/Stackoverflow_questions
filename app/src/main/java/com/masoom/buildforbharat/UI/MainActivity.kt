package com.masoom.buildforbharat.UI

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.masoom.buildforbharat.R
import com.masoom.buildforbharat.models.Item
import com.masoom.buildforbharat.repository.QuestionsRepository
import com.masoom.buildforbharat.utils.Constants.Companion.TIME_DELAY
import com.masoom.buildforbharat.utils.QuestionViewModelProviderFactory
import com.masoom.buildforbharat.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: QuestionsViewModel
    lateinit var q_adapter: QuestionsAdapter
    lateinit var q_list : MutableList<Item>
    val BANNER_AD_ID = "ca-app-pub-3940256099942544/6300978111"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
        val repository = QuestionsRepository()
        val viewModelProviderFactory = QuestionViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(QuestionsViewModel::class.java)
        q_adapter.setOnItemClickListener {
            Toast.makeText(this,"opening in browser",Toast.LENGTH_SHORT).show()
            val uri : Uri = Uri.parse(it.link)
            startActivity( Intent(Intent.ACTION_VIEW,uri))
        }
        var job : Job? = null;

        etSearch.addTextChangedListener{ editable->
            job?.cancel()
            job = MainScope().launch {
                delay(TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty())
                    {
                        avgView.visibility = View.VISIBLE
                        avgAns.visibility = View.VISIBLE
                        val sq = editable.toString()
                        var list = q_list
                        var list2 = list.filter {
                            it.title.contains(sq) or it.owner.display_name.contains(sq)

                        }
                        if(!list2.isEmpty())
                        {
                            avgView.text = viewModel.getAvgViewCount(list2).toString()
                            avgAns.text = viewModel.getAvgAnsCount(list2).toString()
                        }else
                        {
                            avgView.text = "0"
                            avgAns.text = "0"
                        }
                        q_adapter.differ.clear()
                        q_adapter.differ.addAll(list2 as MutableList<Object>)
                        q_adapter.notifyDataSetChanged()
                        viewModel.getSearchQuestion(editable.toString())
                    }
                    else{
                        avgView.visibility = View.INVISIBLE
                        avgAns.visibility = View.INVISIBLE
                        q_adapter.differ.clear()
                        q_adapter.differ.addAll(q_list as MutableList<Object>)
                        q_adapter.notifyDataSetChanged()
                        avgView.text = viewModel.getAvgViewCount(q_list).toString()
                        avgAns.text = viewModel.getAvgAnsCount(q_list).toString()
                    }
                }
            }

        }

        viewModel.questions.observe(this, Observer {
                response->
            when(response)
            {
                is Resource.Success->{
                    println("success")
                    paginationProgressBar.visibility = View.INVISIBLE
                    viewModel.avgAns?.let {
                        avgAns.text = "avg answers :"+viewModel.avgAns.toString()
                    }
                    viewModel.avgView?.let {
                        avgView.text = "avg views :"+viewModel.avgView.toString()
                    }
                    response.data?.let { newsResponse->


                        var list = newsResponse.items as MutableList<Object>
                        q_list = list as MutableList<Item>
                        var dummy = list.get(4)
                        list.add(4,dummy)
                        q_adapter.differ.addAll(list)
                        q_adapter.notifyDataSetChanged()


                    }
                }
                is Resource.Loading->{
                    println("loading")
                    paginationProgressBar.visibility = View.VISIBLE

                }
                is Resource.Error->{
                    paginationProgressBar.visibility = View.INVISIBLE

                    response.message?.let { message->
                        Log.e("TAG_2",message)
                    }

                }
            }
        })
1    }
        private fun setUpRecyclerView(){
        q_adapter = QuestionsAdapter()
        println("setting up the adapter")
        rvQuestions.apply {
            adapter = q_adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

        private fun getBannerArt() : AdView
        {
            val adView = AdView(this)
            adView.apply {
                adSize = AdSize.BANNER
                adUnitId = BANNER_AD_ID
            }
            return adView
        }
}

