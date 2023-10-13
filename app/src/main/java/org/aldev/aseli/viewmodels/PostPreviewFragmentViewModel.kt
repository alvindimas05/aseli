package org.aldev.aseli.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.toUpload
import kotlinx.coroutines.launch
import org.aldev.CreatePostMutation
import org.aldev.aseli.misc.Client
import java.io.File

class PostPreviewFragmentViewModel : ViewModel() {
    private lateinit var client: ApolloClient
    val result = MutableLiveData(false)
    fun setClient(authKey: String){
        client = Client.setClient(authKey)
    }
    fun postImage(image: File, title: String, description: String){
        viewModelScope.launch { postImageCoroutine(image, title, description) }
    }
    private suspend fun postImageCoroutine(image: File, title: String, description: String){
        client.mutation(CreatePostMutation(image.toUpload("application/json"), title, description)).execute()
        result.value = true
    }
}