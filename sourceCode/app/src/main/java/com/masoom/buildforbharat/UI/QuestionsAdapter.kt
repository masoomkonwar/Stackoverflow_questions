package com.masoom.buildforbharat.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.masoom.buildforbharat.R
import com.masoom.buildforbharat.models.Item
import kotlinx.android.synthetic.main.baner.view.*
import java.text.SimpleDateFormat
import java.util.*

class QuestionsAdapter : RecyclerView.Adapter<QuestionsAdapter.QViewHolder>() {


    inner class QViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    val ITEM_TYPE_AD = 0
    val ITEM_TYPE_QUESTION = 1

    override fun getItemViewType(position: Int): Int {
        when(position){
            0->return ITEM_TYPE_AD
            else->return ITEM_TYPE_QUESTION
        }
    }
    private val differcallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differcallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QViewHolder {
        return QViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.baner,parent,false)
        )
    }

    override fun onBindViewHolder(holder: QViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.owner.profile_image).into(profileimg)
            tv_title.text = article.title
            tv_ownersname.text = "author :"+article.owner.display_name

            var sdf  = SimpleDateFormat("ddMMyyyy")
            val date : Date = sdf.parse(article.creation_date.toString())
            tv_publishedon.text = "date:"+date.toString()
            setOnClickListener{
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }
    private var onItemClickListener : ((Item)->Unit)?=null

    fun setOnItemClickListener(listener: (Item)->Unit)
    {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}