package com.project.ecommercemvvmcz.ui.Home.Address

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.AddressRvItemBinding

class AddressAdapter:RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder
        (val binding:AddressRvItemBinding)
        :ViewHolder(binding.root){
        fun bind(address: AddressData, isSeleceted: Boolean) {
            binding.apply {
                buttonAddress.text=address.addressTitle
                if (isSeleceted){
                    buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.blue))
                }else{ buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.white))
                }

            }

        }

    }
    private val diffutil=object :DiffUtil.ItemCallback<AddressData>(){
        override fun areItemsTheSame(oldItem: AddressData, newItem: AddressData): Boolean {
            return oldItem.addressTitle==newItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: AddressData, newItem: AddressData): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffutil)


    var selecetAddress=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context)
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address=differ.currentList[position]
        holder.bind(address,selecetAddress==position)
        holder.binding.buttonAddress.setOnClickListener {
            if (selecetAddress >= 0)
                notifyItemChanged(selecetAddress)
            selecetAddress=holder.adapterPosition
            notifyItemChanged(selecetAddress)
            onclick?.invoke(address)
        }
    }
    var onclick:((AddressData)->Unit)?=null
}