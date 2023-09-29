package org.aldev.aseli.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.aldev.GetPostsQuery
import org.aldev.aseli.databinding.ItemPostBinding
import org.aldev.aseli.misc.Client

class PostsAdapter  (
    private val layoutInflater: LayoutInflater,
    private var posts: List<GetPostsQuery.Post>
) : RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostHolder(ItemPostBinding.inflate(
        layoutInflater, parent, false
    ))
    override fun getItemCount() = posts.size
    override fun onBindViewHolder(holder: PostHolder, i: Int) {
        val post = posts[i]
        val binding = holder.binding

        binding.postDetailsDescription.text = post.description
        binding.postDetailsRil.text = post.ril.toString()
        binding.postDetailsRil.text = post.fek.toString()

        Glide.with(holder.binding.root).load("${Client.imagesUrl}/${post.image}")
            .into(holder.binding.postDetailsImage)
    }
    class PostHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
}