package com.project.ecommercemvvmcz.ui.Billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.ui.Home.Address.AddressData
import com.project.ecommercemvvmcz.ui.Home.Address.AddressDatas
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BilingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

): ViewModel(){
    private val _address=MutableStateFlow<UiState<AddressDatas>>(UiState.Loading())
    val address=_address.asStateFlow()

    val authid=auth.uid

    init {
        getUserAddress()
    }

    fun getUserAddress(){
        viewModelScope.launch {
                _address.emit(UiState.Loading())
        }
        firestore.collection("user").document(auth.uid!!)
            .collection("address").document(auth.uid!!).get().

            addOnSuccessListener {
                val address=it.toObject(AddressDatas::class.java)

                if (address!=null){viewModelScope.launch { _address.emit(UiState.Success(address!!)) }
                }else{
                    viewModelScope.launch { _address.emit(UiState.Loading()) }
                }


            }.addOnFailureListener {
                viewModelScope.launch { _address.emit(UiState.Failure(it.message)) }
            }

    }


    private val _addNewAddress=MutableStateFlow<UiState<AddressDatas>>(UiState.Loading())
    val addNewAddress=_addNewAddress.asStateFlow()

    private val _error= MutableSharedFlow<String>()
    val error=_error.asSharedFlow()

    fun addAddress(address: AddressDatas){
        val validateInput=validInput(address)

        if (validateInput){
            viewModelScope.launch { _addNewAddress.emit(UiState.Loading()) }

            firestore.collection("user").document(auth.uid!!)
                .collection("address").document(auth.uid!!).set(address)
                .addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(UiState.Success(address)) }
                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(UiState.Failure(it.message.toString())) }

                }

        }else{
            viewModelScope.launch { _error.emit("All Field Are Required") }
        }
    }

    private fun validInput(address: AddressDatas): Boolean {

        return address.fullAddress.trim().isNotEmpty()&&
                address.phone.trim().isNotEmpty()



    }
}