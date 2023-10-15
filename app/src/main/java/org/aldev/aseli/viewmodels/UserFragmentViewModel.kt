package org.aldev.aseli.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.toUpload
import kotlinx.coroutines.launch
import org.aldev.ChangeProfileImageMutation
import org.aldev.UserQuery
import org.aldev.aseli.misc.Client
import java.io.File


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
    fun changeProfileImage(image: File, mimeType: String){
        viewModelScope.launch { changeProfileImageCoroutine(image, mimeType) }
    }
    private suspend fun changeProfileImageCoroutine(image: File, mimeType: String){
        val res = client.mutation(ChangeProfileImageMutation(image.toUpload(mimeType))).execute()
        profileImage.value = res.data!!.changeProfileImage
    }
}