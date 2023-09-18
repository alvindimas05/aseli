package org.aldev.aseli.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aldev.RegisterUserMutation
import org.aldev.aseli.misc.Client

class RegisterViewModel : ViewModel() {
    private val client = Client.setClient()
    val registerFailed = MutableLiveData(false)
    var failedType = ""

    var username = ""
    var password = ""
    var verification = ""
    fun register(){
        if(!validateInput()) return
        viewModelScope.launch { registerCoroutine() }
    }
    private fun validateInput(): Boolean {
        if(username.isEmpty()){
            failedType = "empty username"
            registerFailed.value = true
            return false
        }
        if(password.isEmpty()){
            failedType = "empty password"
            registerFailed.value = true
            return false
        }
        if(verification.isEmpty()){
            failedType = "empty verification"
            registerFailed.value = true
            return false
        }
        if(password != verification){
            failedType = "verification not match"
            registerFailed.value = true
            return false
        }
        return true
    }
    private suspend fun registerCoroutine(){
        val mutation = RegisterUserMutation(username, password, verification)
        val res = client.mutation(mutation).execute()

        failedType = res.data!!.registerUser.reason.toString()
        registerFailed.value = !res.data!!.registerUser.success
    }
}