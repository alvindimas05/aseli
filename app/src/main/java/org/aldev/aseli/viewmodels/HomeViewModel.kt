package org.aldev.aseli.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aldev.GetPostsQuery
import org.aldev.aseli.misc.Client

class HomeViewModel : ViewModel() {
    private val client = Client.setClient()
    val posts = MutableLiveData<List<GetPostsQuery.Post>?>()
    fun getPosts(){
        viewModelScope.launch { getPostsCoroutine() }
    }
    private suspend fun getPostsCoroutine(){
        try {
            val res = client.query(GetPostsQuery()).execute()
            posts.value = res.data?.posts
        } catch (e: Exception){
            Log.w("Failed Get Posts", e)
            posts.value = null
        }
    }
}