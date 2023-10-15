package org.aldev.aseli.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aldev.UserQuery
import org.aldev.aseli.misc.Client


class UserFragmentViewModel : PostsFragmentViewModel() {
    val followersTotal = MutableLiveData(0)
    val followingTotal = MutableLiveData(0)
    val profileImage = MutableLiveData<String>()
    fun getUserData(){
        viewModelScope.launch { getUserDataCoroutine() }
    }
    private suspend fun getUserDataCoroutine(){
        val user = client.query(UserQuery()).execute()
        profileImage.value = user.data!!.user.profile_image ?: Client.randomImageUrl
    }
}