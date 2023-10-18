package org.aldev.aseli.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aldev.SendCommentMutation

class PostAdapterViewModel : ProfileViewModel() {
    fun sendComment(post_id: String, comment: String){
        viewModelScope.launch { sendCommentCoroutine(post_id, comment) }
    }
    private suspend fun sendCommentCoroutine(post_id: String, comment: String){
        client.mutation(SendCommentMutation(post_id, comment)).execute()
    }
}