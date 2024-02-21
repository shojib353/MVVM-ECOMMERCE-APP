package com.project.ecommercemvvmcz.ui.Billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.ui.Home.Address.AddressData
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BilingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

): ViewModel(){
    private val _address=MutableStateFlow<UiState<List<AddressData>>>(UiState.Loading())
    val address=_address.asStateFlow()

    init {
        getUserAddress()
    }

    fun getUserAddress(){
        viewModelScope.launch {
                _address.emit(UiState.Loading())
        }
        firestore.collection("user").document(auth.uid!!)
            .collection("address").addSnapshotListener { value, error ->
                if (error!=null){viewModelScope.launch { _address.emit(UiState.Failure(error.message)) }
                return@addSnapshotListener
                }
                val address=value?.toObjects(AddressData::class.java)
                viewModelScope.launch { _address.emit(UiState.Success(address!!)) }
            }
    }
}