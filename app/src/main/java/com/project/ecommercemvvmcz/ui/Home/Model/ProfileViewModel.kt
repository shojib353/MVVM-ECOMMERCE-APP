package com.project.ecommercemvvmcz.ui.Home.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.ui.userAccount.User
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _user = MutableStateFlow<UiState<User>>(UiState.Loading())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _user.emit(UiState.Loading())
        }
        firestore.collection("user").document(auth.uid!!)
            .get().addOnSuccessListener {

                    val user = it.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch {
                            _user.emit(UiState.Success(user))
                        }
                    }
                }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(UiState.Failure(it.message.toString()))
                }
            }
            }
    }



