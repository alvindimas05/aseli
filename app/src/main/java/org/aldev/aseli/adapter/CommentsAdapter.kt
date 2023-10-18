package org.aldev.aseli.adapter

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import org.aldev.GetPostsQuery
import org.aldev.aseli.databinding.ItemCommentBinding
import org.aldev.aseli.misc.Client
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.CommentsAdapterCommentViewModel
import org.aldev.aseli.views.HomeView

class CommentsAdapter (
    private val avt: HomeView,
    val comments: MutableList<GetPostsQuery.Comment>
) : RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder(ItemCommentBinding.inflate(
        avt.layoutInflater, parent, false
    ))
    override fun getItemCount() = comments.size
    override fun onBindViewHolder(holder: CommentHolder, i: Int) {
        val comment = comments[i]
        val binding = holder.binding
        val viewModel = ViewModelProvider(avt)[CommentsAdapterCommentViewModel::class.java]

        binding.commentUsername.text = comment.username
        binding.commentTime.text = comment.time
        binding.commentValue.text = comment.comment

        setProfileImage(viewModel, holder, comment.username)

        YoYo.with(Techniques.FadeInDown).duration(700).playOn(binding.root)
    }
    private fun setProfileImage(viewModel: CommentsAdapterCommentViewModel, holder: CommentHolder, username: String){
        viewModel.setClient(SessionHandler(avt).getSessionKey())
        viewModel.setProfileImage(username){
            Glide.with(avt).load(if(it == null) Client.randomImageUrl else "${Client.imagesUrl}/${it}").into(holder.binding.commentProfile)
        }
    }
    class CommentHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)
}