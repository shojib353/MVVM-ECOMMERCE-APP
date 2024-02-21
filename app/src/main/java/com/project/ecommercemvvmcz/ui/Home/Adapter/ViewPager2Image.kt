package com.project.ecommercemvvmcz.ui.Home.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.project.ecommercemvvmcz.databinding.ViewPagerImgItemBinding

class ViewPager2Image:RecyclerView.Adapter<ViewPager2Image.ViewPager2ImageViewHolder>() {


inner class ViewPager2ImageViewHolder(val binding:ViewPagerImgItemBinding):ViewHolder(binding.root) {
        fun bind(imagePath:String){
            Glide.with(itemView).load(imagePath).into(binding.imgProductDetails)

        }
}

    private val diffCallback=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }
val differ=AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImageViewHolder {
        return ViewPager2ImageViewHolder(
            ViewPagerImgItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    override fun onBindViewHolder(holder: ViewPager2ImageViewHolder, position: Int) {
        val img=differ.currentList[position]
        holder.bind(img)
    }
}
