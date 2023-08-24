package com.example.myapplication

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRetrofitBinding

class MyRetrofitViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root)

class MyRetrofitAdapter(val context: Context, val datas: MutableList<Review>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyRetrofitViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyRetrofitViewHolder).binding

        //holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.list_item_animation)

        val model = datas!![position]
        binding.reviewTitle.text = model.title

//        var stars: MutableList<ImageView> = mutableListOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
//        for(i in 0..4){
//            if(model.star > i)
//                stars[i].setImageResource(R.drawable.star_24)
//            else
//                stars[i].setImageResource(R.drawable.star_border_24)
//        }

        //binding.ratingBar.setOnRatingBarChangeListener{}
        binding.ratingBar.rating = model.star.toFloat()

        binding.reviewDetail.text = model.content

    }
}