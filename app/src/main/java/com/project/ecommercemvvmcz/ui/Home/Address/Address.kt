package com.project.ecommercemvvmcz.ui.Home.Address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentAddressBinding
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@Suppress("DEPRECATION")
@AndroidEntryPoint
class Address : Fragment() {
    private lateinit var binding:FragmentAddressBinding
    val viewmodel by viewModels<AddressViewmodel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewmodel.addNewAddress.collectLatest {
                when(it){
                    is UiState.Loading->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                    }
                    is UiState.Success->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is UiState.Failure->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    else->{Unit}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.error.collectLatest { Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle= edAddressTitle.text.toString()
                val fullName= edFullName.text.toString()
                val street= edStreet.text.toString()
                val phone= edPhone.text.toString()
                val city= edCity.text.toString()
                val state= edState.text.toString()

                val address=AddressData(addressTitle,fullName,street,phone,city,state)
                viewmodel.addAddress(address)
            }
        }
    }

}