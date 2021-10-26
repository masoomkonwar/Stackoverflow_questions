package com.masoom.buildforbharat.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.masoom.buildforbharat.R
import com.masoom.buildforbharat.models.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ad_baner.view.*
import kotlinx.android.synthetic.main.baner.view.*
import java.text.SimpleDateFormat
import java.util.*

class QuestionsAdapter : Adapter<RecyclerView.ViewHolder>() {


    inner class QViewHolder(itemView: View) : ViewHolder(itemView) {

    }
    inner class AdViewHolder(itemView: View) : ViewHolder(itemView)
    {

    }
    val ITEM_TYPE_AD = 0
    val ITEM_TYPE_QUESTION = 1

    override fun getItemViewType(position: Int): Int {
        when(position){
            4->return ITEM_TYPE_AD
            else->return ITEM_TYPE_QUESTION
        }
    }
    val differ: MutableList<Object> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        when(viewType){
           ITEM_TYPE_QUESTION-> return QViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.baner,parent,false)
            )
           ITEM_TYPE_AD-> return AdViewHolder(
               LayoutInflater.from(parent.context).inflate(R.layout.ad_baner,parent,false)
           )
            else->{
                return QViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.baner,parent,false)
                )
            }
        }

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)){
            ITEM_TYPE_QUESTION->{
                val article = differ.get(position) as Item
                holder.itemView.apply {
                    Glide.with(this)
                        .load(article.owner.profile_image).circleCrop().into(profileimg)
                    tv_title.text = article.title
                    tv_ownersname.text = "author : "+article.owner.display_name

                    var sdf  = SimpleDateFormat("ddMMyyyy")
                    val date : Date = sdf.parse(article.creation_date.toString())
                    tv_publishedon.text = "date : "+date.toString()
                    setOnClickListener{
                        onItemClickListener?.let {
                            it(article)
                        }
                    }
                }
            }
            ITEM_TYPE_AD->{
                val adRequest = AdRequest.Builder().build()
                holder.itemView.banner_item2.loadAd(adRequest)
            }
        }
    }
    private var onItemClickListener : ((Item)->Unit)?=null

    fun setOnItemClickListener(listener: (Item)->Unit)
    {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return differ.size
    }


}