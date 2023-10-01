package org.aldev.aseli.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import kotlinx.coroutines.launch
import org.aldev.GetPostsQuery
import org.aldev.SendFekMutation
import org.aldev.SendRilMutation
import org.aldev.aseli.misc.Client

class HomeViewModel : ViewModel() {
    private lateinit var client: ApolloClient
    val posts = MutableLiveData<List<GetPostsQuery.Post>?>()
    fun setClient(authKey: String){
        client = Client.setClient(authKey)
    }
    fun getPosts(){
        viewModelScope.launch { getPostsCoroutine() }
    }
    fun sendRil(post_id: String, handleResult: (it: Boolean?) -> Unit){
        viewModelScope.launch { handleResult(sendRilCoroutine(post_id)) }
    }
    fun sendFek(post_id: String, handleResult: (it: Boolean?) -> Unit){
        viewModelScope.launch { handleResult(sendFekCoroutine(post_id)) }
    }
    private suspend fun sendRilCoroutine(post_id: String): Boolean? {
        return try {
            val res = client.mutation(SendRilMutation(post_id)).execute()
            res.data?.sendRil
        } catch (e: Exception){
            Log.w("Failed Ril Post", e)
            null
        }
    }
    private suspend fun sendFekCoroutine(post_id: String): Boolean? {
        return try {
            val res = client.mutation(SendFekMutation(post_id)).execute()
            res.data?.sendFek
        } catch (e: Exception){
            Log.w("Failed Fek Post", e)
            null
        }
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