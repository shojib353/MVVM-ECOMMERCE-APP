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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentBillingBinding
import com.project.ecommercemvvmcz.ui.Home.Address.AddressAdapter
import com.project.ecommercemvvmcz.ui.Home.Address.AddressData
import com.project.ecommercemvvmcz.ui.Home.Address.AddressDatas
import com.project.ecommercemvvmcz.ui.Home.Model.CartProduct
import com.project.ecommercemvvmcz.ui.order.Order
import com.project.ecommercemvvmcz.ui.order.OrderStatus
import com.project.ecommercemvvmcz.ui.order.OrderViewModel
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@Suppress("DEPRECATION")
@AndroidEntryPoint
class Billing : Fragment(){
    private lateinit var binding: FragmentBillingBinding
    //private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val viewModel by viewModels<BilingViewModel>()

    private val args by navArgs<BillingArgs>()
    private var products= emptyList<CartProduct>()
    private var total=0

    private var selecetedAddress:AddressData?=null
    private val orderViwModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products=args.cproducts.toList()
        total=args.totalPrice
    }

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

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billing_to_address)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it)
                {
                    is UiState.Loading->{

                    }
                    is UiState.Success->{

                        binding.rvAddress.setText("${it.data!!.fullAddress}")

                        binding.edphone.setText("${it.data!!.phone}")

                    }
                    is UiState.Failure->{

                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    else->{Unit}
                }
            }
        }




        lifecycleScope.launchWhenStarted {
            orderViwModel.order.collectLatest {
                when(it)
                {
                    is UiState.Loading->{

                    }
                    is UiState.Success->{
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(binding.root,"Your Order Was Placed",Snackbar.LENGTH_LONG).show()
                    }
                    is UiState.Failure->{
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    else->{Unit}
                }
            }
        }



        billingProductAdapter.differ.submitList(products)
        binding.tvTotalPrice.text=total.toString()


        binding.buttonPlaceOrder.setOnClickListener {
            if (binding.rvAddress.text.isNotEmpty() && binding.edphone.text.isNotEmpty()) {
                showOrderConfirmationDialog()
                setAddressRv()


            }else{
                Toast.makeText(requireContext(),"Please Select An Address",Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }


        }
    }

    private fun showOrderConfirmationDialog() {
        binding.apply {
            val Address= rvAddress.text.toString()
            val phone= edphone.text.toString()


            val address= AddressDatas(Address,phone)
            val order=Order(
                viewModel.authid!!,
                OrderStatus.Ordered.status,
                total,
                products,
                address

            )
            orderViwModel.placeOrder(order)



        }



    }

    private fun setAddressRv() {
        binding.apply {
            rvAddress
            val Address= rvAddress.text.toString()
            val phone= edphone.text.toString()


            val address= AddressDatas(Address,phone)

            viewModel.addAddress(address)


        }

    }

    private fun setBilingRv() {
        binding.rvProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter=billingProductAdapter
        }

    }
}

