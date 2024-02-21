package com.project.ecommercemvvmcz.ui.Home.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ecommercemvvmcz.databinding.CategoryItemBinding
import com.project.ecommercemvvmcz.ui.Home.Model.Products


class CategoryAdapter :RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    inner class CategoryViewHolder(private val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(products: Products){
            binding.apply {
                Glide.with(itemView).load(products.images[0]).into(catImg)
                tvCatName.text=products.name
            }
        }
    }
    private val deffcallback=object : DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id==newItem.id
        }

    }
    val differ=AsyncListDiffer(this,deffcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false

            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
    }


}


