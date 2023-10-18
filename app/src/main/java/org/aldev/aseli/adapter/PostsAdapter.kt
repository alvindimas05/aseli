package org.aldev.aseli.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import org.aldev.GetPostsQuery
import org.aldev.aseli.R
import org.aldev.aseli.databinding.ItemPostBinding
import org.aldev.aseli.misc.Client
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.PostAdapterViewModel
import org.aldev.aseli.viewmodels.PostsFragmentViewModel
import org.aldev.aseli.views.HomeView

class PostsAdapter(
    private val avt: HomeView,
    private val posts: List<GetPostsQuery.Post>,
    private val viewModel: PostsFragmentViewModel
) : RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    private lateinit var postViewModel: PostAdapterViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostHolder(ItemPostBinding.inflate(
        avt.layoutInflater, parent, false
    ))
    override fun getItemCount() = posts.size
    override fun onBindViewHolder(holder: PostHolder, i: Int) {
        postViewModel = ViewModelProvider(avt)[PostAdapterViewModel::class.java]

        val post = posts[i]
        val binding = holder.binding

        binding.itemPostUsername.text = post.username
        binding.itemPostDescription.text = post.description
        binding.itemPostTitle.text = post.title

        binding.itemPostRil.text = post.ril.toString()
        binding.itemPostFek.text = post.fek.toString()
        binding.itemPostCommentTotal.text = post.comments_total.toString()
        binding.itemPostTime.text = post.time

        setButtonRil(binding.root.context, binding.itemPostRilBtn, post.user_ril, false)
        setButtonFek(binding.root.context, binding.itemPostFekBtn, post.user_fek, false)

        binding.itemPostRilBtn.setOnClickListener { viewModel.sendRil(post.id) { ril -> updateRil(binding, ril!!) } }
        binding.itemPostFekBtn.setOnClickListener { viewModel.sendFek(post.id) { fek -> updateFek(binding, fek!!) } }

        Glide.with(avt).load("${Client.imagesUrl}/${post.image}").into(holder.binding.itemPostImage)
        setProfileImage(holder, post.username)

        val commentsAdapter = CommentsAdapter(avt, post.comments.toMutableList())
        binding.itemCommentsList.commentsList.apply {
            adapter = commentsAdapter
            layoutManager = LinearLayoutManager(avt)
        }

        setCommentsButton(binding, post, commentsAdapter/*binding.itemPost, binding.itemCommentsList.root, binding.itemPostCommentBtn, binding.itemCommentsList.btnExit*/)

        YoYo.with(Techniques.FadeInDown).duration(800).playOn(binding.root)
    }
    @SuppressLint("SetTextI18n")
    private fun setCommentsButton(binding: ItemPostBinding, post: GetPostsQuery.Post, commentsAdapter: CommentsAdapter/*post: LinearLayout, commentsList: LinearLayout, btnComment: ImageButton, btnExit: ImageButton*/){
        binding.itemCommentsList.btnSendComment.setOnClickListener {
            val comment = binding.itemCommentsList.commentInput.text.toString()
            commentsAdapter.comments.add(
                GetPostsQuery.Comment(SessionHandler(avt).getUsername(), "Just now", comment)
            )
            commentsAdapter.notifyItemInserted(commentsAdapter.itemCount - 1)
            postViewModel.sendComment(post.id, comment)

            binding.itemPostCommentTotal.text = (binding.itemPostCommentTotal.text.toString().toInt() + 1).toString()
            binding.itemCommentsList.commentInput.text.clear()
        }
        binding.itemPostCommentBtn.setOnClickListener {
//            post.visibility = View.GONE
            val isVisible = binding.itemCommentsList.root.visibility == View.GONE
            if(isVisible) binding.itemCommentsList.root.visibility = View.VISIBLE

            var yoyo = YoYo.with(if(isVisible) Techniques.FadeIn else Techniques.FadeOut).duration(500)
            if(!isVisible) yoyo = yoyo.onEnd { binding.itemCommentsList.root.visibility = View.GONE }
            yoyo.playOn(binding.itemCommentsList.root)

            YoYo.with(Techniques.Bounce).duration(700).playOn(binding.itemPostCommentBtn)
        }
//        btnExit.setOnClickListener {
//            post.visibility = View.VISIBLE
//            commentsList.visibility = View.GONE
//        }
    }
    private fun setProfileImage(holder: PostHolder, username: String){
        postViewModel.setClient(SessionHandler(avt).getSessionKey())
        postViewModel.setProfileImage(username) {
            Glide.with(avt).load(if(it == null) Client.randomImageUrl else "${Client.imagesUrl}/${it}").into(holder.binding.itemPostProfil)
        }
    }
    private fun setButtonRil(ctx: Context, btn: ImageButton, ril: Boolean, anim: Boolean = true) {
        val img = DrawableCompat.wrap(btn.drawable)
        img.setTint(ctx.getColor(if(ril) R.color.green else R.color.white))

        if(anim) YoYo.with(if(ril) Techniques.Tada else Techniques.Swing).duration(700).playOn(btn)
    }
    private fun setButtonFek(ctx: Context, btn: ImageButton, fek: Boolean, anim: Boolean = true) {
        val img = DrawableCompat.wrap(btn.drawable)
        img.setTint(ctx.getColor(if(fek) R.color.red else R.color.white))

        if(anim) YoYo.with(if(fek) Techniques.Swing else Techniques.Tada).duration(700).playOn(btn)
    }
    private fun updateRil(binding: ItemPostBinding, ril: Boolean){
        val rilVal = binding.itemPostRil.text.toString().toInt() + if(ril) 1 else -1
        binding.itemPostRil.text = rilVal.toString()
        setButtonRil(binding.root.context, binding.itemPostRilBtn, ril)
    }
    private fun updateFek(binding: ItemPostBinding, fek: Boolean){
        val rilVal = binding.itemPostFek.text.toString().toInt() + if(fek) 1 else -1
        binding.itemPostFek.text = rilVal.toString()
        setButtonFek(binding.root.context, binding.itemPostFekBtn, fek)
    }
    class PostHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
}