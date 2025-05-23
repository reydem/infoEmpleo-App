package com.example.infoempleo.login.ui



import androidx.lifecycle.ViewModel
import com.example.infoempleo.login.data.network.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepo: LoginRepository
): ViewModel() { }