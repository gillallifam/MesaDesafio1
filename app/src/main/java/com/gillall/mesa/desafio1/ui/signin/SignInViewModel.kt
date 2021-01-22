package com.gillall.mesa.desafio1.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.gillall.mesa.desafio1.mesa.MesaApi
import com.gillall.mesa.desafio1.mesa.Token
import com.gillall.mesa.desafio1.mesa.UserSignIn
import java.lang.Exception

class SignInViewModel : ViewModel() {
    var token = MutableLiveData<Token>()

    init {

    }

    fun login(user: String, pass: String) {
        viewModelScope.launch {
            try {
                val userData = UserSignIn(user, pass)
                token.value = MesaApi.service.signIn(userData)
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}