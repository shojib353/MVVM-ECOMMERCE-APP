package com.project.ecommercemvvmcz.ui.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentCartBinding
import com.project.ecommercemvvmcz.ui.Home.Adapter.CartItemAdaptar
import com.project.ecommercemvvmcz.ui.Home.Model.FirebaseCommon
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CartFragment :Fragment() {
    private lateinit var binding:FragmentCartBinding
    private val cartItemAdaptar by lazy { CartItemAdaptar() }
    private val viewModel by activityViewModels<CartViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        binding=FragmentCartBinding.inflate(layoutInflater)






        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCartRv()

        binding.buyNow.setOnClickListener { findNavController().navigate(R.id.action_cartFragment2_to_billing) }

        cartItemAdaptar.onPlusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.INCREASE)
        }
        cartItemAdaptar.onMinasClick={
            if (it.quantity<=1)
                    {viewModel.deleteProduct(it.products.id)
            }else{viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)}
        }
        cartItemAdaptar.onDeleteClick={
            viewModel.deleteProduct(it.products.id)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest {

            it?.let{
                binding.TPrice.text=it.toString()
            }

            }}


        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is UiState.Success->{
                        cartItemAdaptar.differ.submitList(it.data)
                    }
                    is UiState.Failure->{}
                    is UiState.Loading->{}

                }
            }

        }

    }

    private fun setCartRv() {
        binding.CRv.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter=cartItemAdaptar
        }
    }

}
