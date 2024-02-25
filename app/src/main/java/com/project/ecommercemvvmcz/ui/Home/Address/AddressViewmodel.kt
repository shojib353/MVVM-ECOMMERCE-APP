package com.project.ecommercemvvmcz.ui.Home.Address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

):ViewModel() {
    private val _addNewAddress=MutableStateFlow<UiState<AddressData>>(UiState.Loading())
    val addNewAddress=_addNewAddress.asStateFlow()

    private val _error=MutableSharedFlow<String>()
    val error=_error.asSharedFlow()

    fun addAddress(address: AddressData){
        val validateInput=validInput(address)

        if (validateInput){
            viewModelScope.launch { _addNewAddress.emit(UiState.Loading()) }

            firestore.collection("user").document(auth.uid!!)
                .collection("address").document().set(address)
                .addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(UiState.Success(address)) }
                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(UiState.Failure(it.message.toString())) }

                }

        }else{
            viewModelScope.launch { _error.emit("All Field Are Required") }
        }
    }

    private fun validInput(address: AddressData): Boolean {

        return address.addressTitle.trim().isNotEmpty()&&
                address.city.trim().isNotEmpty()&&
                address.phone.trim().isNotEmpty()&&
                address.state.trim().isNotEmpty()&&
                address.fullname.trim().isNotEmpty()&&
                address.street.trim().isNotEmpty()



    }
}