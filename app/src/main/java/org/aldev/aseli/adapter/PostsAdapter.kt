package org.aldev.aseli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.aldev.GetPostsQuery
import org.aldev.aseli.R
import org.aldev.aseli.databinding.ItemPostBinding
import org.aldev.aseli.misc.Client
import org.aldev.aseli.viewmodels.PostsFragmentViewModel

class PostsAdapter  (
    private val layoutInflater: LayoutInflater,
    private var posts: List<GetPostsQuery.Post>,
    private val viewModel: PostsFragmentViewModel
) : RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostHolder(ItemPostBinding.inflate(
        layoutInflater, parent, false
    ))
    override fun getItemCount() = posts.size
    override fun onBindViewHolder(holder: PostHolder, i: Int) {
        val post = posts[i]
        val binding = holder.binding

        binding.itemPostUsername.text = post.username
        binding.itemPostDescription.text = post.description
        binding.itemPostTitle.text = post.title

        binding.itemPostRil.text = post.ril.toString()
        binding.itemPostFek.text = post.fek.toString()
        binding.itemPostCommentTotal.text = post.comments_total.toString()

        setButtonRil(binding.root.context, binding.itemPostRilBtn, post.user_ril)
        setButtonFek(binding.root.context, binding.itemPostFekBtn, post.user_fek)

        binding.itemPostRilBtn.setOnClickListener { viewModel.sendRil(post.id) { ril -> updateRil(binding, ril!!) } }
        binding.itemPostFekBtn.setOnClickListener { viewModel.sendFek(post.id) { fek -> updateFek(binding, fek!!) } }

        Glide.with(holder.binding.root).load("${Client.imagesUrl}/${post.image}")
            .into(holder.binding.itemPostImage)
    }
    private fun setButtonRil(ctx: Context, btn: ImageButton, ril: Boolean) {
        val img = DrawableCompat.wrap(btn.drawable)
        img.setTint(ctx.getColor(if(ril) R.color.green else R.color.white))
    }
    private fun setButtonFek(ctx: Context, btn: ImageButton, ril: Boolean) {
        val img = DrawableCompat.wrap(btn.drawable)
        img.setTint(ctx.getColor(if(ril) R.color.red else R.color.white))
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