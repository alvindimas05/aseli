package org.aldev.aseli.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aldev.LoginUserMutation
import org.aldev.aseli.misc.Client

class LoginViewModel : ViewModel() {
    private val client = Client.setClient()
    val loginFailed = MutableLiveData(false)
    var failedType = ""
    var authKey = ""

    var username = ""
    var password = ""
    fun login(){
        if(!validateInput()) return
        viewModelScope.launch { loginCoroutine() }
    }
    private fun validateInput(): Boolean {
        if(username.isEmpty()){
            failedType = "empty username"
            loginFailed.value = true
            return false
        }
        if(password.isEmpty()){
            failedType = "empty password"
            loginFailed.value = true
            return false
        }
        return true
    }
    private suspend fun loginCoroutine(){
        val mutation = LoginUserMutation(username, password)
        val res = client.mutation(mutation).execute()

        authKey = res.data!!.loginUser.auth_key.toString()
        failedType = res.data!!.loginUser.reason.toString()
        loginFailed.value = !res.data!!.loginUser.success
    }
}