package com.project.ecommercemvvmcz.ui.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.ecommercemvvmcz.MainActivity
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentHomeBinding
import com.project.ecommercemvvmcz.ui.Home.Adapter.BestDealsAdaptet
import com.project.ecommercemvvmcz.ui.Home.Adapter.CategoryAdapter
import com.project.ecommercemvvmcz.ui.Home.Adapter.ProductAdapter
import com.project.ecommercemvvmcz.ui.Home.Model._ViewModel
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var bestDealsAdapter: BestDealsAdaptet
    private lateinit var productAdapter: ProductAdapter
    private val viewModel :_ViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)
        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_search)
        }









        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCategoryProduct()
        rvBestDealProduct()
        rvProduct()
        bestDealsAdapter.onclick={
            val b=Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetails2,b)
        }

        productAdapter.onclick={
            val b=Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetails2,b)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.categoryProduct.collectLatest {
                when(it){
                    is UiState.Loading ->{
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is UiState.Success ->{
                        categoryAdapter.differ.submitList(it.data)
                        binding.progressBar.visibility = View.GONE
                    }
                    is UiState.Failure->{
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }




        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProduct.collectLatest {
                when(it){
                    is UiState.Loading ->{
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is UiState.Success ->{
                        bestDealsAdapter.differ.submitList(it.data)
                        binding.progressBar.visibility = View.GONE
                    }
                    is UiState.Failure->{
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }




        lifecycleScope.launchWhenStarted {
            viewModel.bestProduct.collectLatest {
                when(it){
                    is UiState.Loading ->{
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is UiState.Success ->{
                        productAdapter.differ.submitList(it.data)
                        binding.progressBar.visibility = View.GONE
                    }
                    is UiState.Failure->{
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun rvProduct() {

        productAdapter= ProductAdapter()

        binding.rvBestProducts.apply {
            layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter=productAdapter
        }

    }

    private fun rvBestDealProduct() {

        bestDealsAdapter= BestDealsAdaptet()

        binding.rcBestDeals.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=bestDealsAdapter
        }

    }

    private fun rvCategoryProduct() {

        categoryAdapter= CategoryAdapter()

        binding.rvCategory.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=categoryAdapter
        }
    }


}