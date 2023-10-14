package org.aldev.aseli.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.launch
import org.aldev.UserQuery
import org.aldev.aseli.misc.Client

class PostAdapterViewModel : ViewModel() {
    private lateinit var client: ApolloClient
    fun setClient(authKey: String){
        client = Client.setClient(authKey)
    }
    fun setProfileImage(username: String, callback: (profileImage: String?) -> Unit){
        viewModelScope.launch { setProfileImageCoroutine(username, callback) }
    }
    private suspend fun setProfileImageCoroutine(username: String, callback: (profileImage: String?) -> Unit){
        val user = client.query(UserQuery(Optional.present(username))).execute()
        callback(user.data!!.user.profile_image)
    }
}