package com.project.ecommercemvvmcz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.ecommercemvvmcz.databinding.FragmentProductDetailsBinding
import com.project.ecommercemvvmcz.ui.Home.Adapter.ViewPager2Image
import com.project.ecommercemvvmcz.ui.Home.Model.CartProduct
import com.project.ecommercemvvmcz.ui.Home.Model.Products
import com.project.ecommercemvvmcz.ui.Home.Model.DetailsViewModel
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProductDetails : Fragment(){


    private val args by navArgs<ProductDetailsArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Image() }
    private val DViewmode by viewModels<DetailsViewModel>()







    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProductDetailsBinding.inflate(inflater)



        val product=args.product

        setupViewPager()
        lifecycleScope.launch { DViewmode.isExit(CartProduct(product,1)) }
        lifecycleScope.launchWhenStarted {
            DViewmode.aaddToCart.collectLatest {
                when(it){
                    is UiState.Loading -> {
                        binding.btnAddToCart.text="ADD TO CART"



                    }
                    is UiState.Success ->{
                        binding.btnAddToCart.text="ALREADY ADDED IN CART"

                    }
                    is UiState.Failure ->{
                        binding.btnAddToCart.text="Faild to Add"
                    }

                }
            }
        }



        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            tvname.text=product.name
            tvdescription.text=product.description
            tvprice.text="$ ${product.price}"

        }
        viewPagerAdapter.differ.submitList(product.images)

        binding.btnAddToCart.setOnClickListener {
            DViewmode.addUpdateProductInCart(CartProduct(product,1))
        }
        lifecycleScope.launchWhenStarted {
            DViewmode.addToCart.collectLatest {
                when(it){
                    is UiState.Loading -> {
                        binding.btnAddToCart.text="ADD TO CART"



                    }
                    is UiState.Success ->{
                        binding.btnAddToCart.text="ALREADY ADDED IN CART"

                    }
                    is UiState.Failure ->{
                        binding.btnAddToCart.text="Faild to Add"
                    }

                }
            }
        }




        return binding.root
    }



    private fun setupViewPager() {
        binding.apply {
            pagerproductImages.adapter = viewPagerAdapter


        }

    }

}

