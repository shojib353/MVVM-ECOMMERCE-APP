package com.project.ecommercemvvmcz.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.ecommercemvvmcz.databinding.FragmentAllOrdersBinding
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class AllOrdersFragment : Fragment() {
    private lateinit var binding: FragmentAllOrdersBinding
    val viewModel by viewModels<AllOrdersViewModel>()
    val allordersAdapter by lazy { AllOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrdersRv()

        lifecycleScope.launchWhenStarted {
            viewModel.allOrders.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                             allordersAdapter.differ.submitList(it.data)
                        if (it.data.isNullOrEmpty()) {
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }
                    is UiState.Failure -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarAllOrders.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }


       allordersAdapter.onClick = {
            val action = AllOrdersFragmentDirections
                .actionAllOrdersFragmentToOrderDetailFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun setupOrdersRv() {
        binding.rvAllOrders.apply {
            adapter = allordersAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }
}