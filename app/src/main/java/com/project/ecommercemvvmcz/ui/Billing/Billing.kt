package com.project.ecommercemvvmcz.ui.Billing

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
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentBillingBinding
import com.project.ecommercemvvmcz.ui.Home.Address.AddressAdapter
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@Suppress("DEPRECATION")
@AndroidEntryPoint
class Billing : Fragment(){
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val viewModel by viewModels<BilingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBilingRv()
        setAddressRv()
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billing_to_address)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it)
                {
                    is UiState.Loading->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is UiState.Success->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        addressAdapter.differ.submitList(it.data)
                    }
                    is UiState.Failure->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    else->{Unit}
                }
            }
        }
    }

    private fun setAddressRv() {
        binding.rvAddress.apply {
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            adapter=addressAdapter
        }

    }

    private fun setBilingRv() {
        binding.rvProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter=billingProductAdapter
        }

    }
}

